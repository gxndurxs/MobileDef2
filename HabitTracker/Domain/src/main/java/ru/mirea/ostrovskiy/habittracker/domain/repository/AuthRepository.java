package ru.mirea.ostrovskiy.habittracker.domain.repository;

public interface AuthRepository {

    interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    void login(String email, String password, AuthCallback callback);
    void register(String email, String password, AuthCallback callback);
    void logout();
    boolean isLoggedIn();
}