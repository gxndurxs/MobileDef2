package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Аннотация @Entity говорит Room, что этот класс - это таблица в БД.
// tableName = "habits" - это название нашей таблицы.
@Entity(tableName = "habits")
public class HabitEntity {
    // @PrimaryKey говорит, что поле id - это уникальный ключ для каждой записи.
    // autoGenerate = true - значит, что id будет присваиваться автоматически.
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;

    // Пустой конструктор ОБЯЗАТЕЛЬНО нужен для Room.
    public HabitEntity() {}

    public HabitEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}