package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habits")
    List<HabitEntity> getAllHabits();

    @Insert
    void insertHabit(HabitEntity habit);

    @Update
    void updateHabit(HabitEntity habit);

    @Delete
    void deleteHabit(HabitEntity habit);
}