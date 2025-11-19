package ru.mirea.ostrovskiy.retrofitapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitApp";
    private ApiService apiService;
    private RecyclerView recyclerView;

    private final List<String> reliableImageUrls = new ArrayList<String>() {{
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/done/materialicons/48dp/1x/baseline_done_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/schedule/materialicons/48dp/1x/baseline_schedule_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/communication/email/materialicons/48dp/1x/baseline_email_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/favorite/materialicons/48dp/1x/baseline_favorite_black_48dp.png");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_todos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        loadPhotos();
    }

    private void loadPhotos() {
        apiService.getPhotos().enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Photo> photosFromServer = response.body();

                    for (int i = 0; i < photosFromServer.size(); i++) {
                        Photo photo = photosFromServer.get(i);
                        String newUrl = reliableImageUrls.get(i % reliableImageUrls.size());
                        photo.setThumbnailUrl(newUrl);
                    }

                    PhotoAdapter adapter = new PhotoAdapter(photosFromServer);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(MainActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Ошибка сети", t);
            }
        });
    }
}