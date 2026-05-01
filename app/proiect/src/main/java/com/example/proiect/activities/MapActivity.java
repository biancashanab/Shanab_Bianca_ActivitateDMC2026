package com.example.proiect.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabaseHelper dbHelper;
    private String targetPaperId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
        // Load all papers to show institutions on the map
        // We can load all papers from the DB
        List<PaperItem> allPapers = dbHelper.loadAllPapers();
        
        if (allPapers.isEmpty()) {
            Toast.makeText(this, "No papers with locations found", Toast.LENGTH_SHORT).show();
            return;
        }

        for (PaperItem paper : allPapers) {
            if (paper.getLat() != 0 || paper.getLng() != 0) {
                LatLng pos = new LatLng(paper.getLat(), paper.getLng());
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(paper.getTitle())
                        .snippet(paper.getInstitution() + ", " + paper.getCountry()));
            }
        }
    }

    private void centerOnPaper(String paperId) {
        PaperItem paper = dbHelper.loadPaperById(paperId);
        if (paper != null && (paper.getLat() != 0 || paper.getLng() != 0)) {
            LatLng pos = new LatLng(paper.getLat(), paper.getLng());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
            
            // Optionally show toast with paper info
            Toast.makeText(this, "Location: " + paper.getInstitution(), Toast.LENGTH_SHORT).show();
        }
    }
}
