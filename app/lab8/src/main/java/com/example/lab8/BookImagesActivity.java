package com.example.lab8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookImagesActivity extends AppCompatActivity {

    private final List<ImageItem> items = new ArrayList<>();
    private ImageItemAdapter adapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_images);

        ListView listView = findViewById(R.id.listViewImages);

        initData();

        adapter = new ImageItemAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(BookImagesActivity.this, WebPageActivity.class);
            intent.putExtra("url", items.get(position).getDetailUrl());
            startActivity(intent);
        });

        executorService = Executors.newFixedThreadPool(4);
        loadImagesWithExecutors();
    }

    private void initData() {
        items.add(new ImageItem(
                "Bookstore exterior",
                "Exterior image for a bookstore-related item.",
                "https://assets.simpleviewinc.com/simpleview/image/upload/c_fill,h_600,q_75,w_950/v1/clients/oklahoma/Untitled_design_38__581e7f0f-5578-43df-a90a-a7167a8f270d.png",
                "https://simple.wikipedia.org/wiki/Bookstore"
        ));

        items.add(new ImageItem(
                "Cărturești Carusel, Bucharest",
                "A famous bookstore from Bucharest.",
                "https://wp.expatexplore.com/wp-content/uploads/2022/02/EuropeBookshops_Bucharest-768x512.jpg",
                "https://theculturetrip.com/europe/romania/bucharest/articles/the-top-10-things-to-do-and-see-in-bucharest"
        ));

        items.add(new ImageItem(
                "Independent bookstore interior",
                "Interior view of a bookstore space.",
                "https://images.squarespace-cdn.com/content/v1/638fe9e1f5da3c1c403645c3/5093de2f-1796-47d6-ae5a-100583b9cea3/0DC62BF2-2A59-4F60-9DB8-D26E5425A060.jpeg",
                "https://www.timeout.com/london/books/londons-best-bookshops"
        ));

        items.add(new ImageItem(
                "Modern large bookstore",
                "A bookstore-like modern retail reading space.",
                "https://static01.nyt.com/images/2024/08/06/multimedia/06friss-pbgl/06friss-pbgl-articleLarge.jpg?quality=75&auto=webp&disable=upscale",
                "https://www.timeout.com/newyork/news/one-of-the-worlds-most-beautiful-bookshops-is-in-manhattan-082525"
        ));

        items.add(new ImageItem(
                "Replace this image before submission",
                "This last URL should ideally be replaced with another bookstore image.",
                "https://cdn.pixabay.com/photo/2016/02/16/21/07/christmas-background-1204029_640.jpg",
                "https://simple.wikipedia.org/wiki/Bookstore"
        ));
    }

    private void loadImagesWithExecutors() {
        for (ImageItem item : items) {
            executorService.execute(() -> {
                Bitmap bitmap = downloadBitmap(item.getImageUrl());
                item.setBitmap(bitmap);
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            });
        }
    }

    private Bitmap downloadBitmap(String urlString) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception ignored) {
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}