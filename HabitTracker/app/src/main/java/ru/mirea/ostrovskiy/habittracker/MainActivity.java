package ru.mirea.ostrovskiy.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModel;
import ru.mirea.ostrovskiy.habittracker.presentation.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private HabitAdapter habitAdapter;
    private HabitRepository habitRepository;
    private boolean isGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitRepository = new HabitRepositoryImpl(this);
        isGuest = habitRepository.isGuest();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_profile_toolbar);
            getSupportActionBar().setTitle("");
        }

        RecyclerView recyclerViewHabits = findViewById(R.id.recyclerViewHabits);
        LinearLayout guestLayout = findViewById(R.id.guest_layout);

        setupRecyclerView(recyclerViewHabits);

        MainViewModelFactory factory = new MainViewModelFactory(this);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        findViewById(R.id.buttonLogout).setOnClickListener(v -> {
            mainViewModel.logout();
            habitRepository.setGuestStatus(false);
        });
        findViewById(R.id.buttonWeather).setOnClickListener(v -> mainViewModel.fetchWeather());
        findViewById(R.id.buttonNetworkIdeas).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NetworkIdeasActivity.class)));
        findViewById(R.id.button_authorize_guest).setOnClickListener(v -> {
            habitRepository.setGuestStatus(false);
            mainViewModel.logout();
        });

        if (isGuest) {
            recyclerViewHabits.setVisibility(View.GONE);
            guestLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHabits.setVisibility(View.VISIBLE);
            guestLayout.setVisibility(View.GONE);
            mainViewModel.getHabits().observe(this, habits -> habitAdapter.setHabits(habits));
        }

        mainViewModel.getIsLoading().observe(this, isLoading -> findViewById(R.id.progressBar).setVisibility(isLoading ? View.VISIBLE : View.GONE));
        mainViewModel.getScreenTitle().observe(this, title -> {
            TextView textViewWelcome = findViewById(R.id.textViewWelcome);
            textViewWelcome.setText(title);
            textViewWelcome.setVisibility(View.VISIBLE);
        });
        mainViewModel.getLogoutEvent().observe(this, loggedOut -> {
            if (loggedOut != null && loggedOut) {
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.loadInitialData();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        habitAdapter = new HabitAdapter();
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (isGuest) {
            menu.findItem(R.id.action_add_habit).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_habit) {
            startActivity(new Intent(this, HabitEditActivity.class));
            return true;
        } else if (itemId == android.R.id.home) {
            if (!isGuest) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                Toast.makeText(this, "Авторизуйтесь, чтобы посмотреть профиль", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}