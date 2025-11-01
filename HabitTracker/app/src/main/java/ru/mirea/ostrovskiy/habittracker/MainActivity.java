// Файл: app/src/main/java/ru/mirea/ostrovskiy/habittracker/MainActivity.java

package ru.mirea.ostrovskiy.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // <-- НОВЫЙ ВАЖНЫЙ ИМПОРТ
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModel; // <-- НОВЫЙ ИМПОРТ
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModelFactory; // <-- НОВЫЙ ИМПОРТ

public class MainActivity extends AppCompatActivity {

    // --- УДАЛЯЕМ ВСЕ UseCases. Теперь Activity о них не знает. ---
    // private GetHabitsUseCase getHabitsUseCase;
    // ... и остальные

    // --- ШАГ 1: Добавляем поле для нашей ViewModel ---
    private MainViewModel mainViewModel;

    // View-элементы остаются без изменений
    private ProgressBar progressBar;
    private RecyclerView recyclerViewHabits;
    private Button buttonLogout;
    private Button buttonWeather;
    private TextView textViewWelcome;
    private HabitAdapter habitAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        // Загружаем/обновляем данные каждый раз, когда экран становится активным
        mainViewModel.loadInitialData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим View-элементы (без изменений)
        progressBar = findViewById(R.id.progressBar);
        recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonWeather = findViewById(R.id.buttonWeather);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        setupRecyclerView(); // Настраиваем RecyclerView (без изменений)

        // --- ШАГ 2: Инициализируем ViewModel с помощью Фабрики ---
        // Мы передаем `this` (контекст Activity) в фабрику, чтобы она могла создать репозитории.
        MainViewModelFactory factory = new MainViewModelFactory(this);
        // `ViewModelProvider` - это системный класс, который правильно создает и хранит ViewModel.
        // Он гарантирует, что при повороте экрана мы получим ту же самую ViewModel, а не новую.
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);


        // --- ШАГ 3: Устанавливаем обработчики, которые теперь вызывают методы ViewModel ---
        buttonLogout.setOnClickListener(v -> {
            mainViewModel.logout(); // Сообщаем ViewModel, что пользователь хочет выйти
        });

        buttonWeather.setOnClickListener(v -> {
            mainViewModel.fetchWeather(); // Сообщаем ViewModel, что нужно загрузить погоду
        });

        // --- ШАГ 4: Подписываемся на изменения данных из ViewModel ---
        observeViewModel();


        findViewById(R.id.fabAddHabit).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HabitEditActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        habitAdapter = new HabitAdapter();
        recyclerViewHabits.setAdapter(habitAdapter);
        recyclerViewHabits.setLayoutManager(new LinearLayoutManager(this));

        // --- ДОБАВЬТЕ ЭТОТ БЛОК ---
        habitAdapter.setOnHabitClickListener(habit -> {
            // Создаем Intent для перехода на новый экран
            Intent intent = new Intent(MainActivity.this, HabitDetailActivity.class);

            // Кладем в Intent все данные о привычке, чтобы передать их на следующий экран
            intent.putExtra("HABIT_ID", habit.getId());
            intent.putExtra("HABIT_NAME", habit.getName());
            intent.putExtra("HABIT_DESCRIPTION", habit.getDescription());
            intent.putExtra("HABIT_DEADLINE", habit.getDeadline());
            intent.putExtra("HABIT_PROGRESS", habit.getProgress());

            // Запускаем новый экран
            startActivity(intent);
        });
    }

    // --- УДАЛЯЕМ МЕТОДЫ initializeDependencies() и loadHabits() ---
    // Вся эта логика теперь внутри ViewModel и Фабрики.

    // --- ШАГ 6: Создаем новый метод для всех подписок (observe) ---
    private void observeViewModel() {

        // Подписка на изменение списка привычек
        mainViewModel.getHabits().observe(this, habits -> {
            // Этот код выполнится каждый раз, когда список привычек в ViewModel изменится
            if (habits != null && !habits.isEmpty()) {
                recyclerViewHabits.setVisibility(View.VISIBLE);
                habitAdapter.setHabits(habits);
            } else {
                recyclerViewHabits.setVisibility(View.GONE);
            }
        });

        // Подписка на изменение состояния загрузки
        mainViewModel.getIsLoading().observe(this, isLoading -> {
            // Этот код выполнится при изменении флага isLoading в ViewModel
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        // Подписка на изменение имени пользователя
        mainViewModel.getScreenTitle().observe(this, title -> {
            textViewWelcome.setText(title);
            textViewWelcome.setVisibility(View.VISIBLE);
        });

        // Подписка на событие выхода из аккаунта
        mainViewModel.getLogoutEvent().observe(this, hasLoggedOut -> {
            // Этот код выполнится, когда ViewModel скажет, что нужно выйти
            if (hasLoggedOut != null && hasLoggedOut) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });

        // Подписка на состояние погоды
        mainViewModel.getWeatherState().observe(this, weatherState -> {
            // Этот код выполнится при любом изменении состояния погоды
            if (weatherState.isLoading) {
                Toast.makeText(this, "Загружаю погоду...", Toast.LENGTH_SHORT).show();
                return; // Выходим, если идет загрузка
            }
            if (weatherState.error != null) {
                // Если есть ошибка, показываем диалог с ошибкой
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Ошибка")
                        .setMessage(weatherState.error)
                        .setPositiveButton("OK", null)
                        .show();
            }
            if (weatherState.temperature != null && weatherState.description != null) {
                // Если есть данные, показываем диалог с погодой
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Погода в Москве")
                        .setMessage("Температура: " + weatherState.temperature + "°C\n" + "Описание: " + weatherState.description)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}