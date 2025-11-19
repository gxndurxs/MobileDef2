package ru.mirea.ostrovskiy.habittracker.data.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("todos")
    Call<List<HabitNetworkDto>> getHabits();
}