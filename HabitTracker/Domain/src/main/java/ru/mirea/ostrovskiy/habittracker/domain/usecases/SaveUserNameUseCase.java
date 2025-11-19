package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class SaveUserNameUseCase {
    private final HabitRepository habitRepository;

    public SaveUserNameUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void execute(String email, String firstName, String lastName) {
        habitRepository.saveInitialUserData(email, firstName, lastName);
    }
}