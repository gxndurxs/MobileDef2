package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;

public class RegisterUserUseCase {
    private final AuthRepository authRepository;

    public RegisterUserUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.register(email, password, callback);
    }
}