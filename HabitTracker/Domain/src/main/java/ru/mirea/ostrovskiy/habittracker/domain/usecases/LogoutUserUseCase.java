package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;

public class LogoutUserUseCase {
    private final AuthRepository authRepository;

    public LogoutUserUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.logout();
    }
}