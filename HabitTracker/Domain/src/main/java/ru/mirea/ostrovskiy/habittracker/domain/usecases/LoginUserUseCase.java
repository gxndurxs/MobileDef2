package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;

public class LoginUserUseCase {
    private final AuthRepository authRepository;

    public LoginUserUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.login(email, password, callback);
    }
}