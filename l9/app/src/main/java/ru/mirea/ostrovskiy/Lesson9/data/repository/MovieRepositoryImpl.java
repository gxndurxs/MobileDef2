package ru.mirea.ostrovskiy.Lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ostrovskiy.Lesson9.domain.models.Movie;
import ru.mirea.ostrovskiy.Lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private SharedPreferences sharedPreferences;
    private static final String MOVIE_KEY = "favorite_movie";

    public MovieRepositoryImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveMovie(Movie movie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MOVIE_KEY, movie.getName());
        return editor.commit();
    }

    @Override
    public Movie getMovie() {
        String movieName = sharedPreferences.getString(MOVIE_KEY, "Default Movie");
        return new Movie(1, movieName);
    }
}