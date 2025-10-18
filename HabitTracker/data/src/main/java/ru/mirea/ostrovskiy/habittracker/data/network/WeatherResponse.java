package ru.mirea.ostrovskiy.habittracker.data.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Этот класс представляет самый верхний уровень JSON-ответа
public class WeatherResponse {
    // @SerializedName говорит Gson, как называется это поле в JSON
    @SerializedName("current_condition")
    private List<CurrentCondition> currentCondition;

    public List<CurrentCondition> getCurrentCondition() {
        return currentCondition;
    }
}