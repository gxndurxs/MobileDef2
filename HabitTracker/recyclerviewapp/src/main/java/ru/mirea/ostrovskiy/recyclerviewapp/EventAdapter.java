package ru.mirea.ostrovskiy.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<HistoricalEvent> eventList;

    public EventAdapter(List<HistoricalEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historical_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HistoricalEvent currentEvent = eventList.get(position);
        holder.imageView.setImageResource(currentEvent.getImageResourceId());
        holder.titleView.setText(currentEvent.getTitle());
        holder.descriptionView.setText(currentEvent.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleView;
        final TextView descriptionView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_event);
            titleView = itemView.findViewById(R.id.text_view_title);
            descriptionView = itemView.findViewById(R.id.text_view_description);
        }
    }
}