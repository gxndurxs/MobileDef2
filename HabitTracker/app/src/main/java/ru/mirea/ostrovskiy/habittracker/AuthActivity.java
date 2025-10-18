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

    private EditText editTextEmail;
    private EditText editTextPassword;

    private LoginUserUseCase loginUserUseCase;
    private RegisterUserUseCase registerUserUseCase;
    private CheckUserLoggedInUseCase checkUserLoggedInUseCase;
    private SaveUserNameUseCase saveUserNameUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthRepository authRepository = new AuthRepositoryImpl();
        HabitRepository habitRepository = new HabitRepositoryImpl(getApplicationContext());

        loginUserUseCase = new LoginUserUseCase(authRepository);
        registerUserUseCase = new RegisterUserUseCase(authRepository);
        checkUserLoggedInUseCase = new CheckUserLoggedInUseCase(authRepository);
        saveUserNameUseCase = new SaveUserNameUseCase(habitRepository);

        if (checkUserLoggedInUseCase.execute()) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_auth);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                loginUserUseCase.execute(email, password, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        saveUserNameUseCase.execute(email);
                        Toast.makeText(AuthActivity.this, "Вход успешен!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(AuthActivity.this, "Ошибка входа: " + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        buttonRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                registerUserUseCase.execute(email, password, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        saveUserNameUseCase.execute(email);
                        Toast.makeText(AuthActivity.this, "Регистрация успешна. Теперь можете войти.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(AuthActivity.this, "Ошибка регистрации: " + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void navigateToMain() {
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }
}