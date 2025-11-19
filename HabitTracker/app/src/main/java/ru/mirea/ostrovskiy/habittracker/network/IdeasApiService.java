package ru.mirea.ostrovskiy.habittracker.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IdeasApiService {
    @GET("photos")
    Call<List<IdeaDto>> getIdeas();
}