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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModel;
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    private ProgressBar progressBar;
    private RecyclerView recyclerViewHabits;
    private Button buttonLogout;
    private Button buttonWeather;
    private TextView textViewWelcome;
    private HabitAdapter habitAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.loadInitialData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonWeather = findViewById(R.id.buttonWeather);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        setupRecyclerView();

        MainViewModelFactory factory = new MainViewModelFactory(this);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        buttonLogout.setOnClickListener(v -> {
            mainViewModel.logout();
        });

        buttonWeather.setOnClickListener(v -> {
            mainViewModel.fetchWeather();
        });

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

        habitAdapter.setOnHabitClickListener(habit -> {
            Intent intent = new Intent(MainActivity.this, HabitDetailActivity.class);

            intent.putExtra("HABIT_ID", habit.getId());
            intent.putExtra("HABIT_NAME", habit.getName());
            intent.putExtra("HABIT_DESCRIPTION", habit.getDescription());
            intent.putExtra("HABIT_DEADLINE", habit.getDeadline());
            intent.putExtra("HABIT_PROGRESS", habit.getProgress());

            startActivity(intent);
        });
    }

    private void observeViewModel() {

        mainViewModel.getHabits().observe(this, habits -> {
            if (habits != null && !habits.isEmpty()) {
                recyclerViewHabits.setVisibility(View.VISIBLE);
                habitAdapter.setHabits(habits);
            } else {
                recyclerViewHabits.setVisibility(View.GONE);
            }
        });

        mainViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        mainViewModel.getScreenTitle().observe(this, title -> {
            textViewWelcome.setText(title);
            textViewWelcome.setVisibility(View.VISIBLE);
        });

        mainViewModel.getLogoutEvent().observe(this, hasLoggedOut -> {
            if (hasLoggedOut != null && hasLoggedOut) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });

        mainViewModel.getWeatherState().observe(this, weatherState -> {
            if (weatherState.isLoading) {
                Toast.makeText(this, "Загружаю погоду...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (weatherState.error != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Ошибка")
                        .setMessage(weatherState.error)
                        .setPositiveButton("OK", null)
                        .show();
            }
            if (weatherState.temperature != null && weatherState.description != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Погода в Москве")
                        .setMessage("Температура: " + weatherState.temperature + "°C\n" + "Описание: " + weatherState.description)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}