package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HabitEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
}