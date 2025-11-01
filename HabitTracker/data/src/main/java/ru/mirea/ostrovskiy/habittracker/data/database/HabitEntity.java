package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public String deadline;
    public int progress;

    public HabitEntity(String name, String description, String deadline, int progress) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.progress = progress;
    }
}