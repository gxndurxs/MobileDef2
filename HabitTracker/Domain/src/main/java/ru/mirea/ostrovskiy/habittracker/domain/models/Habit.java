package ru.mirea.ostrovskiy.habittracker.domain.models;

public class Habit {
    private final int id;
    private final String name;
    private final String description;
    private final String deadline;
    private final int progress;

    public Habit(int id, String name, String description, String deadline, int progress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.progress = progress;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDeadline() { return deadline; }
    public int getProgress() { return progress; }
}