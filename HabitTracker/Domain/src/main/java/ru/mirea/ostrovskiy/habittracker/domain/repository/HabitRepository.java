package ru.mirea.ostrovskiy.habittracker.domain.repository;

import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;

public interface HabitRepository {

    // Callback для асинхронного получения списка привычек
    interface HabitCallback {
        void onHabitsLoaded(List<Habit> habits);
        void onError(String message);
    }

    // Методы для работы со списком привычек
    void getHabits(HabitCallback callback);
    void addHabit(Habit habit);

    // --- НОВЫЕ МЕТОДЫ ДЛЯ РАБОТЫ С SharedPreferences ---
    void saveUserName(String name);
    String getUserName();

    void getWeather(String city, GetWeatherUseCase.WeatherCallback callback);
}