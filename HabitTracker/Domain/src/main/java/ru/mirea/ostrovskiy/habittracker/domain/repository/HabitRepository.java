package ru.mirea.ostrovskiy.habittracker.domain.repository;

import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;

public interface HabitRepository {

    interface HabitCallback {
        void onHabitsLoaded(List<Habit> habits);
        void onError(String message);
    }

    void getHabits(HabitCallback callback);
    void addHabit(Habit habit);
    void updateHabit(Habit habit);
    void deleteHabit(Habit habit);

    void saveUserName(String name);
    String getUserName();
    void getWeather(String city, GetWeatherUseCase.WeatherCallback callback);
}