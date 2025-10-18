package ru.mirea.ostrovskiy.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast; // <-- Импорт для Toast
import androidx.appcompat.app.AlertDialog; // <-- Импорт для AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.data.repository.AuthRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.AuthRepository;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetHabitsUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetUserNameUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase; // <-- НОВЫЙ ИМПОРТ
import ru.mirea.ostrovskiy.habittracker.domain.usecases.LogoutUserUseCase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HabitTrackerApp_Main";

    // UseCases
    private GetHabitsUseCase getHabitsUseCase;
    private LogoutUserUseCase logoutUserUseCase;
    private GetUserNameUseCase getUserNameUseCase;
    private GetWeatherUseCase getWeatherUseCase; // <-- НОВЫЙ USECASE

    // View-элементы
    private ProgressBar progressBar;
    private RecyclerView recyclerViewHabits;
    private Button buttonLogout;
    private Button buttonWeather; // <-- НОВАЯ КНОПКА
    private TextView textViewWelcome;

    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Находим View-элементы ---
        progressBar = findViewById(R.id.progressBar);
        recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonWeather = findViewById(R.id.buttonWeather); // <-- НАХОДИМ КНОПКУ
        textViewWelcome = findViewById(R.id.textViewWelcome);

        // --- Инициализация ---
        setupRecyclerView();
        initializeDependencies();

        // --- Устанавливаем приветствие ---
        String userName = getUserNameUseCase.execute();
        textViewWelcome.setText("Здравствуйте, " + userName + "!");
        textViewWelcome.setVisibility(View.VISIBLE);

        // --- Обработчики ---
        buttonLogout.setOnClickListener(v -> {
            logoutUserUseCase.execute();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
        });

        // НОВЫЙ ОБРАБОТЧИК ДЛЯ КНОПКИ ПОГОДЫ
        buttonWeather.setOnClickListener(v -> {
            // Показываем Toast, что загрузка началась
            Toast.makeText(this, "Загружаю погоду...", Toast.LENGTH_SHORT).show();
            // Вызываем UseCase
            getWeatherUseCase.execute("Moscow", new GetWeatherUseCase.WeatherCallback() {
                @Override
                public void onSuccess(String temperature, String description) {
                    // При успехе показываем AlertDialog
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Погода в Москве")
                            .setMessage("Температура: " + temperature + "°C\n" + "Описание: " + description)
                            .setPositiveButton("OK", null)
                            .show();
                }

                @Override
                public void onError(String message) {
                    // При ошибке тоже показываем AlertDialog
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Ошибка")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
        });

        // --- Загрузка данных ---
        loadHabits();
    }

    private void setupRecyclerView() {
        habitAdapter = new HabitAdapter();
        recyclerViewHabits.setAdapter(habitAdapter);
        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeDependencies() {
        HabitRepository habitRepository = new HabitRepositoryImpl(getApplicationContext());
        AuthRepository authRepository = new AuthRepositoryImpl();
        getHabitsUseCase = new GetHabitsUseCase(habitRepository);
        logoutUserUseCase = new LogoutUserUseCase(authRepository);
        getUserNameUseCase = new GetUserNameUseCase(habitRepository);
        getWeatherUseCase = new GetWeatherUseCase(habitRepository); // <-- ИНИЦИАЛИЗИРУЕМ USECASE
    }

    private void loadHabits() {
        Log.d(TAG, "Calling getHabitsUseCase.execute()...");
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewHabits.setVisibility(View.GONE);

        getHabitsUseCase.execute(new HabitRepository.HabitCallback() {
            @Override
            public void onHabitsLoaded(List<Habit> habits) {
                Log.d(TAG, "onHabitsLoaded SUCCESS! Habits count: " + habits.size());
                progressBar.setVisibility(View.GONE);
                recyclerViewHabits.setVisibility(View.VISIBLE);
                habitAdapter.setHabits(habits);
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError! Message: " + message);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}