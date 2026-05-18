package com.example.proiect.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.utils.PreferencesManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabaseHelper dbHelper;
    private String targetPaperId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.label_map);
        }

        dbHelper = new AppDatabaseHelper(this);
        targetPaperId = getIntent().getStringExtra("PAPER_ID");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        loadMarkers();

        if (targetPaperId != null) {
            centerOnPaper(targetPaperId);
        } else {
            // Default center (e.g., Bucharest) if no specific paper is selected
            LatLng defaultLoc = new LatLng(44.4268, 26.1025);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 5));
        }
    }

    private void loadMarkers() {
        String email = PreferencesManager.getLoggedUserEmail(this);
        int userId = dbHelper.getUserIdByEmail(email);
        List<PaperItem> allPapers = dbHelper.loadAllPapers(userId);
        
        if (allPapers.isEmpty()) {
            Toast.makeText(this, R.string.err_no_locations, Toast.LENGTH_SHORT).show();
            return;
        }

        int markerCount = 0;
        for (PaperItem paper : allPapers) {
            if (paper.getLat() != null && paper.getLng() != null) {
                LatLng pos = new LatLng(paper.getLat(), paper.getLng());
                String snippet = (paper.getInstitution() != null ? paper.getInstitution() : "") + 
                                (paper.getCountry() != null ? ", " + paper.getCountry() : "");
                
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(paper.getTitle())
                        .snippet(snippet.trim().startsWith(",") ? snippet.substring(1).trim() : snippet));
                markerCount++;
            }
        }
        
        if (markerCount == 0) {
            Toast.makeText(this, R.string.err_no_locations, Toast.LENGTH_SHORT).show();
        }
    }

    private void centerOnPaper(String paperId) {
        PaperItem paper = dbHelper.loadPaperById(paperId);
        if (paper != null && paper.getLat() != null && paper.getLng() != null) {
            LatLng pos = new LatLng(paper.getLat(), paper.getLng());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
            
            if (paper.getInstitution() != null) {
                Toast.makeText(this, getString(R.string.label_location_prefix) + paper.getInstitution(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
