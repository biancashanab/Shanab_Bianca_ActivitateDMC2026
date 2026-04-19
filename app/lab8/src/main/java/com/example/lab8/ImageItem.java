package com.example.lab8;

import android.graphics.Bitmap;

public class ImageItem {
    private final String title;
    private final String description;
    private final String imageUrl;
    private final String detailUrl;
    private Bitmap bitmap;

    public ImageItem(String title, String description, String imageUrl, String detailUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.detailUrl = detailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}