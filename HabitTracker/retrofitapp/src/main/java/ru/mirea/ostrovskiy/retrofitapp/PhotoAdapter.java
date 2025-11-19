package ru.mirea.ostrovskiy.retrofitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private final List<Photo> photoList;

    public PhotoAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo currentPhoto = photoList.get(position);
        holder.textViewTitle.setText(currentPhoto.getTitle());

        Picasso.get()
                .load(currentPhoto.getThumbnailUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewThumbnail;
        final TextView textViewTitle;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.image_view_thumbnail);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
        }
    }
}