package ru.mirea.ostrovskiy.Lesson9;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.ostrovskiy.Lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.ostrovskiy.Lesson9.domain.GetFavoriteFilmUseCase;
import ru.mirea.ostrovskiy.Lesson9.domain.SaveFilmToFavoriteUseCase;
import ru.mirea.ostrovskiy.Lesson9.domain.models.Movie;
import ru.mirea.ostrovskiy.Lesson9.domain.repository.MovieRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим элементы интерфейса
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        // Создаем репозиторий
        MovieRepository movieRepository = new MovieRepositoryImpl(this);

        // Обработчик кнопки "Save Movie"
        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Создаем UseCase и выполняем сохранение
                Boolean result = new SaveFilmToFavoriteUseCase(movieRepository)
                        .execute(new Movie(2, text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });

        // Обработчик кнопки "Get Movie"
        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Создаем UseCase и получаем фильм
                Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Get result: %s", movie.getName()));
            }
        });
    }
}