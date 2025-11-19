package ru.mirea.ostrovskiy.habittracker.network;

import com.google.gson.annotations.SerializedName;

public class IdeaDto {
    @SerializedName("title")
    private String title;
    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;
    public String getTitle() { return title; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}