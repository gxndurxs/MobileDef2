package ru.mirea.ostrovskiy.habittracker.data.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Этот класс представляет блок "current_condition"
public class CurrentCondition {
    @SerializedName("temp_C")
    private String tempC;

    @SerializedName("weatherDesc")
    private List<WeatherDescription> weatherDesc;

    public String getTempC() {
        return tempC;
    }

    public List<WeatherDescription> getWeatherDesc() {
        return weatherDesc;
    }
}