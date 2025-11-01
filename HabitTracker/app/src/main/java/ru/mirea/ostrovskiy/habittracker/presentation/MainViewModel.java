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

        screenTitle.addSource(userName, name -> {
            List<Habit> currentHabits = habits.getValue();
            updateTitle(name, currentHabits);
        });

        screenTitle.addSource(habits, habitList -> {
            String currentName = userName.getValue();
            updateTitle(currentName, habitList);
        });
    }
    private void updateTitle(String name, List<Habit> habitList) {
        if (name == null || habitList == null) {
            return;
        }
        int habitsCount = habitList.size();
        screenTitle.setValue("Здравствуйте, " + name + "! Количество привычек: " + habitsCount);
    }

    public LiveData<List<Habit>> getHabits() {
        return habits;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<WeatherState> getWeatherState() {
        return weatherState;
    }

    public LiveData<Boolean> getLogoutEvent() {
        return logoutEvent;
    }

    public LiveData<String> getScreenTitle() {
        return screenTitle;
    }

    public void loadInitialData() {
        String name = getUserNameUseCase.execute();
        userName.setValue(name);
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
            public void onSuccess(String temperature, String description) {
                weatherState.setValue(new WeatherState(false, temperature, description, null));
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
        public final String temperature;
        public final String description;
        public final String error;

        WeatherState(boolean isLoading, String temperature, String description, String error) {
            this.isLoading = isLoading;
            this.temperature = temperature;
            this.description = description;
            this.error = error;
        }
    }
}