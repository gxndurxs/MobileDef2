package ru.mirea.ostrovskiy.Lesson9.domain;

import ru.mirea.ostrovskiy.Lesson9.domain.models.Movie;
import ru.mirea.ostrovskiy.Lesson9.domain.repository.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private MovieRepository movieRepository;

    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        return movieRepository.saveMovie(movie);
    }
}