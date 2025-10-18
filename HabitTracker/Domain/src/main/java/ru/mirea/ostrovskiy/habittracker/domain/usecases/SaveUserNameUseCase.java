package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class SaveUserNameUseCase {
    private final HabitRepository habitRepository;

    public SaveUserNameUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(String name) {
        habitRepository.saveUserName(name);
    }
}