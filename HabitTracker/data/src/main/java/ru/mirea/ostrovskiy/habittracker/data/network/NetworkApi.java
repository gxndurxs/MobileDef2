package ru.mirea.ostrovskiy.habittracker.data.network;

import java.util.Arrays;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;

// Мы вернули этот класс к его первоначальной, простой версии.
// Он больше не использует Retrofit и не ходит в интернет.
public class NetworkApi {

    // Он снова просто отдает заранее созданный список "привычек".
    public List<Habit> getHabitsFromServer() {
        return Arrays.asList(
                new Habit(101, "Выпить стакан воды (из сети)", "Каждое утро после пробуждения"),
                new Habit(102, "Сделать зарядку (из сети)", "15 минут легкой гимнастики"),
                new Habit(103, "Читать книгу (из сети)", "Хотя бы 20 страниц перед сном")
        );
    }
}