// Файл: data/src/main/java/ru/mirea/ostrovskiy/habittracker/data/network/NetworkApi.java

package ru.mirea.ostrovskiy.habittracker.data.network;

import java.util.Arrays;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;

public class NetworkApi {

    public List<Habit> getHabitsFromServer() {
        // --- ИСПРАВЛЕНО: Теперь мы передаем все 5 аргументов в конструктор ---
        // Мы просто добавляем тестовые данные для дедлайна и начальный прогресс 0.
        return Arrays.asList(
                new Habit(101, "Выпить стакан воды (из сети)", "Каждое утро после пробуждения", "Ежедневно", 0),
                new Habit(102, "Сделать зарядку (из сети)", "15 минут легкой гимнастики", "По будням", 0),
                new Habit(103, "Читать книгу (из сети)", "Хотя бы 20 страниц перед сном", "До 01.01.2026", 0)
        );
    }
}