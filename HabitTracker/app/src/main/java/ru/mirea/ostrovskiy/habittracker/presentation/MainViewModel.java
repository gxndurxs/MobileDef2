package ru.mirea.ostrovskiy.habittracker.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetHabitsUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetUserNameUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.LogoutUserUseCase;

public class MainViewModel extends ViewModel {
    private final GetHabitsUseCase getHabitsUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final GetUserNameUseCase getUserNameUseCase;
    private final GetWeatherUseCase getWeatherUseCase;
    private final MutableLiveData<List<Habit>> habits = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<WeatherState> weatherState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutEvent = new MutableLiveData<>();
    private final MediatorLiveData<String> screenTitle = new MediatorLiveData<>();

    public MainViewModel(GetHabitsUseCase getHabitsUseCase, LogoutUserUseCase logoutUserUseCase, GetUserNameUseCase getUserNameUseCase, GetWeatherUseCase getWeatherUseCase) {
        this.getHabitsUseCase = getHabitsUseCase;
        this.logoutUserUseCase = logoutUserUseCase;
        this.getUserNameUseCase = getUserNameUseCase;
        this.getWeatherUseCase = getWeatherUseCase;
        screenTitle.addSource(userName, name -> updateTitle(name, habits.getValue()));
        screenTitle.addSource(habits, habitList -> updateTitle(userName.getValue(), habitList));
    }

    private void updateTitle(String name, List<Habit> habitList) {
        if (name == null || habitList == null) return;
        screenTitle.setValue("Здравствуйте, " + name + "! Количество привычек: " + habitList.size());
    }

    public LiveData<List<Habit>> getHabits() { return habits; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getScreenTitle() { return screenTitle; }
    public LiveData<Boolean> getLogoutEvent() { return logoutEvent; }
    public LiveData<WeatherState> getWeatherState() { return weatherState; }

    public void loadInitialData() {
        userName.setValue(getUserNameUseCase.execute());
        loadHabits();
    }

    public void loadHabits() {
        isLoading.setValue(true);
        getHabitsUseCase.execute(new HabitRepository.HabitCallback() {
            @Override
            public void onHabitsLoaded(List<Habit> loadedHabits) {
                habits.setValue(loadedHabits);
                isLoading.setValue(false);
            }
            @Override
            public void onError(String message) {
                isLoading.setValue(false);
            }
        });
    }

    public void fetchWeather() {
        weatherState.setValue(new WeatherState(true, null, null, null));
        getWeatherUseCase.execute("Moscow", new GetWeatherUseCase.WeatherCallback() {
            @Override
            public void onSuccess(String temp, String desc) {
                weatherState.setValue(new WeatherState(false, temp, desc, null));
            }
            @Override
            public void onError(String message) {
                weatherState.setValue(new WeatherState(false, null, null, message));
            }
        });
    }

    public void logout() {
        logoutUserUseCase.execute();
        logoutEvent.setValue(true);
    }

    public static class WeatherState {
        public final boolean isLoading;
        public final String temperature, description, error;
        WeatherState(boolean isLoading, String temp, String desc, String err) {
            this.isLoading = isLoading; this.temperature = temp; this.description = desc; this.error = err;
        }
    }
}