package ru.mirea.ostrovskiy.habittracker.data.network;

import java.util.Arrays;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;

public class NetworkApi {

    public List<Habit> getHabitsFromServer() {
        return Arrays.asList(
                new Habit(0, "Выпить стакан воды (из сети)", "Каждое утро после пробуждения", "Ежедневно", 0),
                new Habit(0, "Сделать зарядку (из сети)", "15 минут легкой гимнастики", "По будням", 0),
                new Habit(0, "Читать книгу (из сети)", "Хотя бы 20 страниц перед сном", "До 01.01.2026", 0)
        );
    }
}