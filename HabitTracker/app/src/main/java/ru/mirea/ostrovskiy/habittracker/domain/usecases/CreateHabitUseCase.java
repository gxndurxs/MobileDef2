package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class CreateHabitUseCase {
    private HabitRepository habitRepository;

    public CreateHabitUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public boolean execute(Habit habit) {
        return habitRepository.saveHabit(habit);
    }
}
