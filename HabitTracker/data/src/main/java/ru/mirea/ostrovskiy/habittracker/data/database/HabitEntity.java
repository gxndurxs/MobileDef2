package ru.mirea.ostrovskiy.habittracker.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userId;
    public String name;
    public String description;
    public String deadline;
    public int progress;

    public HabitEntity(String userId, String name, String description, String deadline, int progress) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.progress = progress;
    }
}