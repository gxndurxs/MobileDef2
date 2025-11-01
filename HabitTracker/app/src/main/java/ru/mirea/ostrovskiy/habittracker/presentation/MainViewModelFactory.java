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

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public MainViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            HabitRepository habitRepository = new HabitRepositoryImpl(context);
            AuthRepository authRepository = new AuthRepositoryImpl();
            GetHabitsUseCase getHabitsUseCase = new GetHabitsUseCase(habitRepository);
            LogoutUserUseCase logoutUserUseCase = new LogoutUserUseCase(authRepository);
            GetUserNameUseCase getUserNameUseCase = new GetUserNameUseCase(habitRepository);
            GetWeatherUseCase getWeatherUseCase = new GetWeatherUseCase(habitRepository);

            return (T) new MainViewModel(getHabitsUseCase, logoutUserUseCase, getUserNameUseCase, getWeatherUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}