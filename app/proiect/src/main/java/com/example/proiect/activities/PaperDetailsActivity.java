package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.label_abstract);
        }

        dbHelper = new AppDatabaseHelper(this);
        String userEmail = PreferencesManager.getLoggedUserEmail(this);
        currentUserId = dbHelper.getUserIdByEmail(userEmail);

        String paperId = getIntent().getStringExtra("PAPER_ID");
        paper = dbHelper.loadPaperById(paperId);

        if (paper == null) {
            Toast.makeText(this, R.string.err_paper_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        displayData();
        
        // Add to history
        dbHelper.addHistory(currentUserId, paper.getId());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Button btnShare = findViewById(R.id.btnSharePaper);
        Button btnOpenWeb = findViewById(R.id.btnOpenWeb);

        isFavorite = dbHelper.isFavorite(currentUserId, paper.getId());
        updateFavoriteButton();

        btnFavorite.setOnClickListener(v -> toggleFavorite());
        
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("PAPER_ID", paper.getId());
            startActivity(intent);
        });

        btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String shareContent = getString(R.string.share_prefix) + paper.getTitle() + getString(R.string.share_link_label) + paper.getUrl();
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share_chooser_title)));
        });

        btnOpenWeb.setOnClickListener(v -> {
            String url = paper.getUrl();
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
                startActivity(browserIntent);
            } else {
                Toast.makeText(this, R.string.err_invalid_url, Toast.LENGTH_SHORT).show();
            }
        });

        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                dbHelper.saveRating(currentUserId, paper.getId(), rating);
                Toast.makeText(this, R.string.msg_rating_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayData() {
        tvTitle.setText(paper.getTitle());
        tvAuthors.setText(getString(R.string.label_authors) + paper.getAuthors());
        tvYear.setText(getString(R.string.label_year) + paper.getYear());
        tvSource.setText(getString(R.string.label_source) + paper.getSource());
        tvCitations.setText(getString(R.string.label_citations) + paper.getCitationCount());
        tvAbstract.setText(paper.getAbstractText());
        
        float rating = dbHelper.loadRating(currentUserId, paper.getId());
        rbRating.setRating(rating);
    }

    private void toggleFavorite() {
        if (isFavorite) {
            dbHelper.removeFavorite(currentUserId, paper.getId());
            isFavorite = false;
            Toast.makeText(this, R.string.msg_fav_removed, Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addFavorite(currentUserId, paper.getId());
            isFavorite = true;
            Toast.makeText(this, R.string.msg_fav_added, Toast.LENGTH_SHORT).show();
        }
        updateFavoriteButton();
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setText(R.string.btn_remove_favorite);
        } else {
            btnFavorite.setText(R.string.btn_add_favorite);
        }
    }
}
