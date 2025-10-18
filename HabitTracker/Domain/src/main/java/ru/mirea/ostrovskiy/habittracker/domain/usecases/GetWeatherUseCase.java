package ru.mirea.ostrovskiy.habittracker.domain.usecases;

// Обратите внимание, что импорта WeatherResponse здесь больше НЕТ

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

// Этот UseCase будет отвечать за получение погоды.
public class GetWeatherUseCase {

    // Callback для асинхронного возврата результата
    public interface WeatherCallback {
        void onSuccess(String temperature, String description);
        void onError(String message);
    }

    private final HabitRepository habitRepository;

    public GetWeatherUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(String city, WeatherCallback callback) {
        // Мы просто передаем вызов в репозиторий
        habitRepository.getWeather(city, callback);
    }
}