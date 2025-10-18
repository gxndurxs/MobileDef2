package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// @Database - аннотация, которая регистрирует все наши таблицы (entities).
// version = 1 - версия нашей БД. Если мы изменим структуру таблиц, нужно будет увеличить версию.
@Database(entities = {HabitEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // Этот метод связывает нашу БД с нашим DAO.
    public abstract HabitDao habitDao();
}