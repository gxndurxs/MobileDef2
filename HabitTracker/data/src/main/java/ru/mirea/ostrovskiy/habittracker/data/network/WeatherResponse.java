package ru.mirea.ostrovskiy.habittracker.data.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("current_condition")
    private List<CurrentCondition> currentCondition;

    public List<CurrentCondition> getCurrentCondition() {
        return currentCondition;
    }
}