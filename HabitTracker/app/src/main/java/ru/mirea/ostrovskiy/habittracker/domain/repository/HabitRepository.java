package ru.mirea.ostrovskiy.habittracker.domain.repository;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import java.util.List;

public interface HabitRepository {
    List<Habit> getHabits();
    boolean saveHabit(Habit habit);
}
