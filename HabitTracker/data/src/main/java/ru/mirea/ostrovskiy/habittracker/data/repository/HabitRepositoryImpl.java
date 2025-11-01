package ru.mirea.ostrovskiy.habittracker.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
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

    private static final String TAG = "HabitTrackerApp_Repo";
    private final SharedPreferences sharedPreferences;
    private final AppDatabase database;
    private final NetworkApi networkApi;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final WeatherApiService weatherApiService;

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

    @Override
    public void addHabit(Habit habit) {
        HabitEntity entity = new HabitEntity(habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress());
        executor.execute(() -> {
            database.habitDao().insertHabit(entity);
        });
    }

    @Override
    public void updateHabit(Habit habit) {
        HabitEntity entity = new HabitEntity(habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress());
        entity.id = habit.getId();
        executor.execute(() -> {
            database.habitDao().updateHabit(entity);
        });
    }

    @Override
    public void deleteHabit(Habit habit) {
        HabitEntity entity = new HabitEntity(habit.getName(), habit.getDescription(), habit.getDeadline(), habit.getProgress());
        entity.id = habit.getId();
        executor.execute(() -> {
            database.habitDao().deleteHabit(entity);
        });
    }


    @Override
    public void saveUserName(String name) {
        sharedPreferences.edit().putString("USER_NAME", name).apply();
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString("USER_NAME", "Гость");
    }

    @Override
    public void getWeather(String city, GetWeatherUseCase.WeatherCallback callback) {
        weatherApiService.getWeather(city).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    String temp = weather.getCurrentCondition().get(0).getTempC();
                    String desc = weather.getCurrentCondition().get(0).getWeatherDesc().get(0).getValue();
                    callback.onSuccess(temp, desc);
                } else {
                    callback.onError("Ошибка получения данных: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                callback.onError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    @Override
    public void getHabits(HabitCallback callback) {
        executor.execute(() -> {
            try {
                List<HabitEntity> entities = database.habitDao().getAllHabits();

                if (entities != null && !entities.isEmpty()) {
                    List<Habit> habits = new ArrayList<>();
                    for (HabitEntity entity : entities) {
                        habits.add(new Habit(entity.id, entity.name, entity.description, entity.deadline, entity.progress));
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onHabitsLoaded(habits));
                } else {
                    List<Habit> habitsFromServer = networkApi.getHabitsFromServer();
                    if (!habitsFromServer.isEmpty()) {
                        for (Habit serverHabit : habitsFromServer) {
                            HabitEntity entityToSave = new HabitEntity(serverHabit.getName(), serverHabit.getDescription(), serverHabit.getDeadline(), serverHabit.getProgress());
                            database.habitDao().insertHabit(entityToSave);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onHabitsLoaded(habitsFromServer));
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
            }
        });
    }
}