// Файл: app/src/main/java/ru/mirea/ostrovskiy/habittracker/presentation/MainViewModelFactory.java

package ru.mirea.ostrovskiy.habittracker.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ostrovskiy.habittracker.data.repository.AuthRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetHabitsUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetUserNameUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.LogoutUserUseCase;

// Фабрика должна реализовывать интерфейс ViewModelProvider.Factory
public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    // Конструктор фабрики. Ей нужен Context для создания репозиториев.
    public MainViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            // Создаем все зависимости (репозитории и use cases) прямо здесь
            HabitRepository habitRepository = new HabitRepositoryImpl(context);
            AuthRepository authRepository = new AuthRepositoryImpl();
            GetHabitsUseCase getHabitsUseCase = new GetHabitsUseCase(habitRepository);
            LogoutUserUseCase logoutUserUseCase = new LogoutUserUseCase(authRepository);
            GetUserNameUseCase getUserNameUseCase = new GetUserNameUseCase(habitRepository);
            GetWeatherUseCase getWeatherUseCase = new GetWeatherUseCase(habitRepository);

            // Создаем и возвращаем нашу ViewModel, передавая ей все зависимости
            return (T) new MainViewModel(getHabitsUseCase, logoutUserUseCase, getUserNameUseCase, getWeatherUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}