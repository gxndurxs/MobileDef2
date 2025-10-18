package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;

public class CheckUserLoggedInUseCase {
    private final AuthRepository authRepository;

    public CheckUserLoggedInUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean execute() {
        return authRepository.isLoggedIn();
    }
}