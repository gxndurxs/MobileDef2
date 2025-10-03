package ru.mirea.ostrovskiy.habittracker.data.repository;

import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import java.util.Arrays;
import java.util.List;

public class HabitRepositoryImpl implements HabitRepository {

    @Override
    public List<Habit> getHabits() {
        // Возвращаем тестовые данные как требуется в задании
        return Arrays.asList(
                new Habit(1, "Утренняя зарядка", "Ежедневная 15-минутная зарядка"),
                new Habit(2, "Чтение книги", "Читать 30 минут в день"),
                new Habit(3, "Прогулка", "Гулять 1 час на свежем воздухе")
        );
    }

    @Override
    public boolean saveHabit(Habit habit) {
        // Простая заглушка - всегда возвращаем true
        return true;
    }
}
