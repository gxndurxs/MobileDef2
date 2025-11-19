package ru.mirea.ostrovskiy.habittracker.data.network;

import com.google.gson.annotations.SerializedName;

public class HabitNetworkDto {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("completed")
    private boolean completed;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }
}