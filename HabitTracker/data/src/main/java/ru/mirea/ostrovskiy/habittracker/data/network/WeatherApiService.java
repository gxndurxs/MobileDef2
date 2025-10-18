package ru.mirea.ostrovskiy.habittracker.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherApiService {
    @GET("{city}?format=j1")
    Call<WeatherResponse> getWeather(@Path("city") String city);
}