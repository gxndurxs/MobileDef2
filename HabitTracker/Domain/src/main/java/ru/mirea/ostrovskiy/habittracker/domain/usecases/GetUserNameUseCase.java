package ru.mirea.ostrovskiy.habittracker.domain.usecases;

import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;

public class GetUserNameUseCase {
    private final HabitRepository habitRepository;

    public GetUserNameUseCase(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public String execute() {
        if (habitRepository.isGuest()) {
            return "Гость";
        }

        String[] userProfile = habitRepository.getUserProfile();
        String firstName = userProfile[1];
        String lastName = userProfile[2];

        if (firstName != null && !firstName.isEmpty() && !firstName.equals("Пользователь")) {
            return firstName + " " + lastName;
        } else {
            return userProfile[0];
        }
    }
}