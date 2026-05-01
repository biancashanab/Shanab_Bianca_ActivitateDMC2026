package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.utils.PreferencesManager;

public class PaperDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthors, tvYear, tvSource, tvCitations, tvAbstract;
    private RatingBar rbRating;
    private Button btnFavorite, btnMap;
    private AppDatabaseHelper dbHelper;
    private PaperItem paper;
    private int currentUserId;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_details);

        dbHelper = new AppDatabaseHelper(this);
        String userEmail = PreferencesManager.getLoggedUserEmail(this);
        currentUserId = dbHelper.getUserIdByEmail(userEmail);

        String paperId = getIntent().getStringExtra("PAPER_ID");
        paper = dbHelper.loadPaperById(paperId);

        if (paper == null) {
            Toast.makeText(this, "Paper not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayData();
        
        // Add to history
        dbHelper.addHistory(currentUserId, paper.getId());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvDetTitle);
        tvAuthors = findViewById(R.id.tvDetAuthors);
        tvYear = findViewById(R.id.tvDetYear);
        tvSource = findViewById(R.id.tvDetSource);
        tvCitations = findViewById(R.id.tvDetCitations);
        tvAbstract = findViewById(R.id.tvDetAbstract);
        rbRating = findViewById(R.id.rbPaperRating);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMap = findViewById(R.id.btnShowOnMap);

        isFavorite = dbHelper.isFavorite(currentUserId, paper.getId());
        updateFavoriteButton();

        btnFavorite.setOnClickListener(v -> toggleFavorite());
        
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("PAPER_ID", paper.getId());
            startActivity(intent);
        });

        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                dbHelper.saveRating(currentUserId, paper.getId(), rating);
                Toast.makeText(this, "Rating saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayData() {
        tvTitle.setText(paper.getTitle());
        tvAuthors.setText("Authors: " + paper.getAuthors());
        tvYear.setText("Year: " + paper.getYear());
        tvSource.setText("Source: " + paper.getSource());
        tvCitations.setText("Citations: " + paper.getCitationCount());
        tvAbstract.setText(paper.getAbstractText());
        
        float rating = dbHelper.loadRating(currentUserId, paper.getId());
        rbRating.setRating(rating);
    }

    private void toggleFavorite() {
        if (isFavorite) {
            dbHelper.removeFavorite(currentUserId, paper.getId());
            isFavorite = false;
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addFavorite(currentUserId, paper.getId());
            isFavorite = true;
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
        updateFavoriteButton();
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setText("Remove Favorite");
        } else {
            btnFavorite.setText("Add Favorite");
        }
    }
}
