package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class GetHabitsUseCase {
    private final HabitRepository habitRepository;

    public GetHabitsUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(HabitRepository.HabitCallback callback) {
        habitRepository.getHabits(callback);
    }
}