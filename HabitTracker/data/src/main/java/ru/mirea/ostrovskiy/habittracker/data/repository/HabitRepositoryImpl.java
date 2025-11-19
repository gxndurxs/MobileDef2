package ru.mirea.ostrovskiy.habittracker.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.room.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.ostrovskiy.habittracker.data.database.AppDatabase;
import ru.mirea.ostrovskiy.habittracker.data.database.HabitEntity;
import ru.mirea.ostrovskiy.habittracker.data.network.NetworkApi;
import ru.mirea.ostrovskiy.habittracker.data.network.WeatherApiService;
import ru.mirea.ostrovskiy.habittracker.data.network.WeatherResponse;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;

public class HabitRepositoryImpl implements HabitRepository {
    private final SharedPreferences sharedPreferences;
    private final AppDatabase database;
    private final NetworkApi networkApi;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final WeatherApiService weatherApiService;
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String USER_LAST_NAME = "USER_LAST_NAME";
    private static final String IS_GUEST = "IS_GUEST";

    public HabitRepositoryImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        this.database = Room.databaseBuilder(context, AppDatabase.class, "habit_database")
                .fallbackToDestructiveMigration()
                .build();
        this.networkApi = new NetworkApi();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wttr.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.weatherApiService = retrofit.create(WeatherApiService.class);
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    @Override
    public void getHabits(HabitCallback callback) {
        String userId = getCurrentUserId();
        if (isGuest() || userId == null) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onHabitsLoaded(new ArrayList<>()));
            return;
        }
        executor.execute(() -> {
            List<HabitEntity> entities = database.habitDao().getAllHabitsForUser(userId);
            List<Habit> habits = new ArrayList<>();
            for (HabitEntity entity : entities) {
                habits.add(new Habit(entity.id, entity.name, entity.description, entity.deadline, entity.progress));
            }
            new Handler(Looper.getMainLooper()).post(() -> callback.onHabitsLoaded(habits));
        });
    }

    @Override
    public void addHabit(Habit habit) {
        String userId = getCurrentUserId();
        if (userId == null) return;
        executor.execute(() -> database.habitDao().insertHabit(new HabitEntity(userId, habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress())));
    }

    @Override
    public void updateHabit(Habit habit) {
        String userId = getCurrentUserId();
        if (userId == null) return;
        HabitEntity entity = new HabitEntity(userId, habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress());
        entity.id = habit.getId();
        executor.execute(() -> database.habitDao().updateHabit(entity));
    }

    @Override
    public void deleteHabit(Habit habit) {
        String userId = getCurrentUserId();
        if (userId == null) return;
        HabitEntity entity = new HabitEntity(userId, habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress());
        entity.id = habit.getId();
        executor.execute(() -> database.habitDao().deleteHabit(entity));
    }

    @Override
    public void saveInitialUserData(String email, String firstName, String lastName) {
        sharedPreferences.edit()
                .putString(USER_EMAIL, email)
                .putString(USER_FIRST_NAME, firstName)
                .putString(USER_LAST_NAME, lastName)
                .apply();
    }

    @Override
    public String[] getUserProfile() {
        String email = sharedPreferences.getString(USER_EMAIL, "Гость");
        String firstName = sharedPreferences.getString(USER_FIRST_NAME, "Гость");
        String lastName = sharedPreferences.getString(USER_LAST_NAME, "");
        return new String[]{email, firstName, lastName};
    }

    @Override
    public void saveUserProfile(String firstName, String lastName) {
        sharedPreferences.edit()
                .putString(USER_FIRST_NAME, firstName)
                .putString(USER_LAST_NAME, lastName)
                .apply();
    }

    @Override
    public void setGuestStatus(boolean isGuest) {
        sharedPreferences.edit().putBoolean(IS_GUEST, isGuest).apply();
    }

    @Override
    public boolean isGuest() {
        return sharedPreferences.getBoolean(IS_GUEST, false);
    }

    @Override
    public void clearLocalDataOnLogout() {
        String userId = getCurrentUserId();
        if (userId != null) {
            executor.execute(() -> database.habitDao().deleteAllHabitsForUser(userId));
        }
        sharedPreferences.edit().clear().apply();
    }

    @Override
    public void getWeather(String city, GetWeatherUseCase.WeatherCallback callback) {
        weatherApiService.getWeather(city).enqueue(new Callback<WeatherResponse>() {
            @Override public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    String temp = weather.getCurrentCondition().get(0).getTempC();
                    String desc = weather.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue();
                    callback.onSuccess(temp, desc);
                } else {
                    callback.onError("Ошибка получения погоды: " + response.code());
                }
            }
            @Override public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                callback.onError("Ошибка сети (погода): " + t.getMessage());
            }
        });
    }
}