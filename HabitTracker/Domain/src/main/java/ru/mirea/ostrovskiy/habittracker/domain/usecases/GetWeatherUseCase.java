package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class GetWeatherUseCase {

    public interface WeatherCallback {
        void onSuccess(String temperature, String description);
        void onError(String message);
    }

    private final HabitRepository habitRepository;

    public GetWeatherUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(String city, WeatherCallback callback) {
        habitRepository.getWeather(city, callback);
    }
}