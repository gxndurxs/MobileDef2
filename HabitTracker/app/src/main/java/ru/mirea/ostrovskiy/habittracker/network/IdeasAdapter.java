package ru.mirea.ostrovskiy.habittracker.network;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import ru.mirea.ostrovskiy.habittracker.R;

public class IdeasAdapter extends RecyclerView.Adapter<IdeasAdapter.IdeaViewHolder> {
    private final List<IdeaDto> ideas;
    public IdeasAdapter(List<IdeaDto> ideas) { this.ideas = ideas; }

    @NonNull @Override
    public IdeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_idea, parent, false);
        return new IdeaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaViewHolder holder, int position) {
        IdeaDto idea = ideas.get(position);
        holder.title.setText(idea.getTitle());
        Picasso.get()
                .load(idea.getThumbnailUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.image);
    }

    @Override public int getItemCount() { return ideas.size(); }

    static class IdeaViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView title;
        public IdeaViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view_idea);
            title = itemView.findViewById(R.id.text_view_idea_title);
        }
    }
}