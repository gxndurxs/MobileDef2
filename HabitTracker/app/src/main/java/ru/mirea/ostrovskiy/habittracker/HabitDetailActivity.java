package ru.mirea.ostrovskiy.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class HabitDetailActivity extends AppCompatActivity {
    private HabitRepository habitRepository;
    private Habit currentHabit;
    private TextView textProgressValue;
    private ProgressBar progressBar;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        habitRepository = new HabitRepositoryImpl(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textName = findViewById(R.id.textViewDetailName);
        TextView textDescription = findViewById(R.id.textViewDetailDescription);
        TextView textDeadline = findViewById(R.id.textViewDetailDeadline);
        progressBar = findViewById(R.id.progressBarDetail);
        textProgressValue = findViewById(R.id.textViewProgressValue);
        seekBar = findViewById(R.id.seekBarProgress);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            int id = intent.getIntExtra("HABIT_ID", -1);
            String name = intent.getStringExtra("HABIT_NAME");
            String description = intent.getStringExtra("HABIT_DESCRIPTION");
            String deadline = intent.getStringExtra("HABIT_DEADLINE");
            int progress = intent.getIntExtra("HABIT_PROGRESS", 0);

            currentHabit = new Habit(id, name, description, deadline, progress);
            textName.setText(name);
            textDescription.setText(description);
            textDeadline.setText(deadline);
            updateProgressViews(progress);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(name);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { if (fromUser) updateProgressViews(progress); }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                currentHabit = new Habit(currentHabit.getId(), currentHabit.getName(), currentHabit.getDescription(), currentHabit.getDeadline(), seekBar.getProgress());
                habitRepository.updateHabit(currentHabit);
                Toast.makeText(HabitDetailActivity.this, "Прогресс сохранен!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgressViews(int progress) {
        progressBar.setProgress(progress);
        seekBar.setProgress(progress);
        textProgressValue.setText(progress + "%");
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.menu_detail_habit, menu); return true; }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_edit) {
            Intent intent = new Intent(this, HabitEditActivity.class);
            intent.putExtra("HABIT_ID", currentHabit.getId());
            intent.putExtra("HABIT_NAME", currentHabit.getName());
            intent.putExtra("HABIT_DESCRIPTION", currentHabit.getDescription());
            intent.putExtra("HABIT_DEADLINE", currentHabit.getDeadline());
            intent.putExtra("HABIT_PROGRESS", currentHabit.getProgress());
            startActivity(intent);
            finish();
            return true;
        } else if (itemId == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Удалить привычку?")
                    .setMessage("Это действие необратимо.")
                    .setPositiveButton("Да", (dialog, which) -> {
                        habitRepository.deleteHabit(currentHabit);
                        Toast.makeText(this, "Привычка удалена", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Отмена", null).show();
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}