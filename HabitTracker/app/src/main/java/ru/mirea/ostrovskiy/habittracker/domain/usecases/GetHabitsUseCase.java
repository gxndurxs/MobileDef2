package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import java.util.List;

public class GetHabitsUseCase {
    private HabitRepository habitRepository;

    public GetHabitsUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<Habit> execute() {
        return habitRepository.getHabits();
    }
}