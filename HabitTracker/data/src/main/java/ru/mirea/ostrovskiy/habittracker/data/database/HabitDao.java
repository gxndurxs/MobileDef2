package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Dao;
import androidx.room.Delete; // <-- НОВЫЙ ИМПОРТ
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update; // <-- НОВЫЙ ИМПОРТ

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habits")
    List<HabitEntity> getAllHabits();

    @Insert
    void insertHabit(HabitEntity habit);

    // --- НОВЫЙ МЕТОД ДЛЯ ОБНОВЛЕНИЯ ---
    // Room сам найдет запись по id и обновит ее поля
    @Update
    void updateHabit(HabitEntity habit);

    // --- НОВЫЙ МЕТОД ДЛЯ УДАЛЕНИЯ ---
    @Delete
    void deleteHabit(HabitEntity habit);
}