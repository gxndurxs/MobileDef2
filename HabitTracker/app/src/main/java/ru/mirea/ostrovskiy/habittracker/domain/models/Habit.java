package ru.mirea.ostrovskiy.habittracker.domain.models;

public class Habit {
    private final int id;
    private final String name;
    private final String description;

    public Habit(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}