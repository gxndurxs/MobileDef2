package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class GetUserNameUseCase {
    private final HabitRepository habitRepository;

    public GetUserNameUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public String execute() {
        return habitRepository.getUserName();
    }
}