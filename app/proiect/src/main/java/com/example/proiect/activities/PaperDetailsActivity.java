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
import com.example.proiect.utils.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.utils.PreferencesManager;

public class PaperDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthors, tvYear, tvSource, tvCitations, tvAbstract;
    private TextView tvInstitution, tvCountry, tvOpenAlex;
    private RatingBar rbRating;
    private Button btnFavorite, btnMap, btnOpenWeb;
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
        tvInstitution = findViewById(R.id.tvDetInstitution);
        tvCountry = findViewById(R.id.tvDetCountry);
        tvOpenAlex = findViewById(R.id.tvDetOpenAlex);
        rbRating = findViewById(R.id.rbPaperRating);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnMap = findViewById(R.id.btnShowOnMap);
        btnOpenWeb = findViewById(R.id.btnOpenWeb);
        Button btnShare = findViewById(R.id.btnSharePaper);

        isFavorite = dbHelper.isFavorite(currentUserId, paper.getId());
        updateFavoriteButton();

        btnFavorite.setOnClickListener(v -> toggleFavorite());
        
        // Hide map button if coordinates are missing
        if (paper.getLat() == null || paper.getLng() == null) {
            btnMap.setVisibility(android.view.View.GONE);
        } else {
            btnMap.setOnClickListener(v -> {
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("PAPER_ID", paper.getId());
                startActivity(intent);
            });
        }

        btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String url = resolveBestUrl();
            // Construim continutul pentru partajare, incluzand titlul si cel mai bun link
            String shareContent = getString(R.string.share_prefix) + paper.getTitle() + 
                    (url != null ? getString(R.string.share_link_label) + url : "");
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            sendIntent.setType("text/plain");
            // createChooser (fereastra care te lasă să alegi aplicatia)
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share_chooser_title)));
        });

        String finalUrl = resolveBestUrl();
        if (finalUrl == null) {
            btnOpenWeb.setVisibility(android.view.View.GONE);
        } else {
            btnOpenWeb.setOnClickListener(v -> {
                try {
                    // Deschidem link-ul valid (DOI sau URL) intr-un browser extern.
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(finalUrl));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.err_invalid_url, Toast.LENGTH_SHORT).show();
                }
            });
        }

        rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                dbHelper.saveRating(currentUserId, paper.getId(), rating);
                Toast.makeText(this, R.string.msg_rating_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metoda de fallback: prioritizam DOI (ca link), apoi URL, apoi null.
    private String resolveBestUrl() {
        String url = paper.getUrl();
        if (url != null && !url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
            return url;
        }
        
        String doi = paper.getDoi();
        if (doi != null && !doi.isEmpty()) {
            if (doi.startsWith("http")) return doi;
            return "https://doi.org/" + doi;
        }
        
        return null;
    }

    private void displayData() {
        tvTitle.setText(paper.getTitle());
        
        String authors = paper.getAuthors();
        tvAuthors.setText(getString(R.string.label_authors) + (authors.isEmpty() ? "N/A" : authors));
        
        tvYear.setText(getString(R.string.label_year) + paper.getYear());
        
        String source = paper.getSource();
        tvSource.setText(getString(R.string.label_source) + (source == null || source.isEmpty() ? "N/A" : source));
        
        if (paper.getCitationCount() == 0) {
            tvCitations.setText(R.string.label_citations_na);
        } else {
            tvCitations.setText(getString(R.string.label_citations) + paper.getCitationCount());
        }
        
        tvAbstract.setText(paper.getAbstractText() != null ? paper.getAbstractText() : "");
        
        // Handle Institution
        if (paper.getInstitution() != null && !paper.getInstitution().isEmpty()) {
            tvInstitution.setVisibility(android.view.View.VISIBLE);
            tvInstitution.setText(getString(R.string.label_institution) + paper.getInstitution());
        } else {
            tvInstitution.setVisibility(android.view.View.GONE);
        }

        // Handle Country
        if (paper.getCountry() != null && !paper.getCountry().isEmpty()) {
            tvCountry.setVisibility(android.view.View.VISIBLE);
            tvCountry.setText(getString(R.string.label_country) + paper.getCountry());
        } else {
            tvCountry.setVisibility(android.view.View.GONE);
        }

        // Handle OpenAlex ID
        if (paper.getOpenAlexId() != null && !paper.getOpenAlexId().isEmpty()) {
            tvOpenAlex.setVisibility(android.view.View.VISIBLE);
            tvOpenAlex.setText(getString(R.string.label_openalex) + paper.getOpenAlexId());
        } else {
            tvOpenAlex.setVisibility(android.view.View.GONE);
        }

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
