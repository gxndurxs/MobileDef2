package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

// @Dao говорит Room, что это наш объект для доступа к данным.
@Dao
public interface HabitDao {
    // @Insert - простая команда для вставки новой записи.
    @Insert
    void insertHabit(HabitEntity habit);

    // @Query - для более сложных запросов. "SELECT * FROM habits" значит
    // "Выбери все (*) записи из таблицы habits".
    @Query("SELECT * FROM habits")
    List<HabitEntity> getAllHabits();
}
