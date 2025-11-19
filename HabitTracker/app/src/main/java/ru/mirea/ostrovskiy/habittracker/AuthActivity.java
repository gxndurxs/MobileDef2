package ru.mirea.ostrovskiy.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.ostrovskiy.habittracker.data.repository.AuthRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.CheckUserLoggedInUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.LoginUserUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.RegisterUserUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.SaveUserNameUseCase;

public class AuthActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private LoginUserUseCase loginUserUseCase;
    private RegisterUserUseCase registerUserUseCase;
    private CheckUserLoggedInUseCase checkUserLoggedInUseCase;
    private SaveUserNameUseCase saveUserNameUseCase;
    private HabitRepository habitRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthRepository authRepository = new AuthRepositoryImpl();
        habitRepository = new HabitRepositoryImpl(getApplicationContext());
        loginUserUseCase = new LoginUserUseCase(authRepository);
        registerUserUseCase = new RegisterUserUseCase(authRepository);
        checkUserLoggedInUseCase = new CheckUserLoggedInUseCase(authRepository);
        saveUserNameUseCase = new SaveUserNameUseCase(habitRepository);

        if (checkUserLoggedInUseCase.execute() || habitRepository.isGuest()) {
            navigateToMain(false);
            return;
        }

        setContentView(R.layout.activity_auth);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        findViewById(R.id.buttonLogin).setOnClickListener(v -> handleLogin());
        findViewById(R.id.buttonRegister).setOnClickListener(v -> handleRegister());
        findViewById(R.id.buttonGuest).setOnClickListener(v -> {
            habitRepository.setGuestStatus(true);
            navigateToMain(true);
        });
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            loginUserUseCase.execute(email, password, new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess() {
                    habitRepository.setGuestStatus(false);
                    // Проверяем, есть ли уже имя пользователя. Если нет (первый вход), то сохраняем.
                    String[] profile = habitRepository.getUserProfile();
                    if (profile[1] == null || profile[1].equals("Гость") || profile[1].isEmpty()) {
                        saveUserNameUseCase.execute(email, "Пользователь", "");
                    }
                    Toast.makeText(AuthActivity.this, "Вход успешен!", Toast.LENGTH_SHORT).show();
                    navigateToMain(true);
                }
                @Override public void onError(String message) { Toast.makeText(AuthActivity.this, "Ошибка входа: " + message, Toast.LENGTH_LONG).show(); }
            });
        }
    }

    private void handleRegister() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            registerUserUseCase.execute(email, password, new AuthRepository.AuthCallback() {
                @Override public void onSuccess() { Toast.makeText(AuthActivity.this, "Регистрация успешна. Теперь можете войти.", Toast.LENGTH_LONG).show(); }
                @Override public void onError(String message) { Toast.makeText(AuthActivity.this, "Ошибка регистрации: " + message, Toast.LENGTH_LONG).show(); }
            });
        }
    }

    private void navigateToMain(boolean isNewLogin) {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        if (isNewLogin) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        finish();
    }
}