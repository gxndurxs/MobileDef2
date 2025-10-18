package ru.mirea.ostrovskiy.habittracker.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void login(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void register(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }
}