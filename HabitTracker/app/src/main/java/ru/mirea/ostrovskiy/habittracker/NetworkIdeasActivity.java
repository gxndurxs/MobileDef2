package ru.mirea.ostrovskiy.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import ru.mirea.ostrovskiy.habittracker.network.IdeaDto;
import ru.mirea.ostrovskiy.habittracker.network.IdeasAdapter;
import ru.mirea.ostrovskiy.habittracker.network.IdeasApiService;

public class NetworkIdeasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private final List<String> reliableImageUrls = new ArrayList<String>() {{
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/done/materialicons/48dp/1x/baseline_done_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/schedule/materialicons/48dp/1x/baseline_schedule_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/communication/email/materialicons/48dp/1x/baseline_email_black_48dp.png");
        add("https://raw.githubusercontent.com/google/material-design-icons/master/png/action/favorite/materialicons/48dp/1x/baseline_favorite_black_48dp.png");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_ideas);

        recyclerView = findViewById(R.id.recycler_view_ideas);
        progressBar = findViewById(R.id.progress_bar_ideas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Идеи из сети");
        }

        loadIdeas();
    }

    private void loadIdeas() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IdeasApiService apiService = retrofit.create(IdeasApiService.class);

        apiService.getIdeas().enqueue(new Callback<List<IdeaDto>>() {
            @Override
            public void onResponse(Call<List<IdeaDto>> call, Response<List<IdeaDto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<IdeaDto> ideasFromServer = response.body();
                    for (int i = 0; i < ideasFromServer.size(); i++) {
                        ideasFromServer.get(i).setThumbnailUrl(reliableImageUrls.get(i % reliableImageUrls.size()));
                    }
                    recyclerView.setAdapter(new IdeasAdapter(ideasFromServer));
                } else {
                    Toast.makeText(NetworkIdeasActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<IdeaDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NetworkIdeasActivity.this, "Сетевая ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}