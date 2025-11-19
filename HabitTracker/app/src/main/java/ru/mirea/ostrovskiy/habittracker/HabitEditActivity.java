package ru.mirea.ostrovskiy.habittracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class HabitEditActivity extends AppCompatActivity {
    private TextInputEditText editTextName, editTextDescription, editTextDeadline;
    private HabitRepository habitRepository;
    private Habit currentHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        habitRepository = new HabitRepositoryImpl(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextName = findViewById(R.id.editTextHabitName);
        editTextDescription = findViewById(R.id.editTextHabitDescription);
        editTextDeadline = findViewById(R.id.editTextHabitDeadline);

        if (getIntent().hasExtra("HABIT_ID")) {
            setTitle("Редактировать привычку");
            int id = getIntent().getIntExtra("HABIT_ID", -1);
            String name = getIntent().getStringExtra("HABIT_NAME");
            String description = getIntent().getStringExtra("HABIT_DESCRIPTION");
            String deadline = getIntent().getStringExtra("HABIT_DEADLINE");
            int progress = getIntent().getIntExtra("HABIT_PROGRESS", 0);
            currentHabit = new Habit(id, name, description, deadline, progress);
            editTextName.setText(name);
            editTextDescription.setText(description);
            editTextDeadline.setText(deadline);
        } else {
            setTitle("Новая привычка");
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.menu_edit_habit, menu); return true; }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveHabit();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveHabit() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String deadline = editTextDeadline.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentHabit != null) {
            habitRepository.updateHabit(new Habit(currentHabit.getId(), name, description, deadline, currentHabit.getProgress()));
            Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
        } else {
            habitRepository.addHabit(new Habit(0, name, description, deadline, 0));
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}