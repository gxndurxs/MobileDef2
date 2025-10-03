package ru.mirea.ostrovskiy.habittracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.ostrovskiy.habittracker.data.repository.HabitRepositoryImpl;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetHabitsUseCase;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        // Создаем репозиторий и use case
        HabitRepositoryImpl habitRepository = new HabitRepositoryImpl();
        GetHabitsUseCase getHabitsUseCase = new GetHabitsUseCase(habitRepository);

        // Получаем тестовые данные
        List<Habit> habits = getHabitsUseCase.execute();

        // Отображаем результат
        StringBuilder result = new StringBuilder("Мои привычки:\n");
        for (Habit habit : habits) {
            result.append("- ").append(habit.getName()).append("\n");
        }

        textView.setText(result.toString());
    }
}