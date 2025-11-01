package ru.mirea.ostrovskiy.recyclerviewapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<HistoricalEvent> eventList = generateEventList();

        EventAdapter adapter = new EventAdapter(eventList);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<HistoricalEvent> generateEventList() {
        List<HistoricalEvent> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(new HistoricalEvent("Великие географические открытия", "Эпоха с XV по XVII век", R.drawable.ic_event_discovery));
            list.add(new HistoricalEvent("Промышленная революция", "Переход к машинному производству", R.drawable.ic_event_computer));
            list.add(new HistoricalEvent("Космическая гонка", "Соперничество СССР и США", R.drawable.ic_event_space));
        }
        return list;
    }
}