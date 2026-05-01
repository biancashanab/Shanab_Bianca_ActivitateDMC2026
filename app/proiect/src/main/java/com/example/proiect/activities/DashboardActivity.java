package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.adapters.DashboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private GridView gvDashboard;
    private List<DashboardAdapter.DashboardItem> dashboardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        gvDashboard = findViewById(R.id.gvDashboard);
        prepareItems();

        DashboardAdapter adapter = new DashboardAdapter(this, dashboardItems);
        gvDashboard.setAdapter(adapter);

        gvDashboard.setOnItemClickListener((parent, view, position, id) -> {
            String label = dashboardItems.get(position).label;
            handleNavigation(label);
        });
    }

    private void prepareItems() {
        dashboardItems = new ArrayList<>();
        dashboardItems.add(new DashboardAdapter.DashboardItem("Research Threads", android.R.drawable.ic_menu_search));
        dashboardItems.add(new DashboardAdapter.DashboardItem("Saved Papers", android.R.drawable.ic_menu_save));
        dashboardItems.add(new DashboardAdapter.DashboardItem("Map", android.R.drawable.ic_dialog_map));
        dashboardItems.add(new DashboardAdapter.DashboardItem("Analytics", android.R.drawable.ic_menu_sort_by_size));
        dashboardItems.add(new DashboardAdapter.DashboardItem("Settings", android.R.drawable.ic_menu_preferences));
    }

    private void handleNavigation(String label) {
        Intent intent = null;
        switch (label) {
            case "Research Threads":
                intent = new Intent(this, ThreadListActivity.class);
                break;
            case "Saved Papers":
                // Deschidem ResearchResultsActivity in modul favorites
                intent = new Intent(this, ResearchResultsActivity.class);
                intent.putExtra("SHOW_FAVORITES", true);
                break;
            case "Map":
                intent = new Intent(this, MapActivity.class);
                break;
            case "Analytics":
                intent = new Intent(this, AnalyticsActivity.class);
                break;
            case "Settings":
                intent = new Intent(this, SettingsActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, label + " coming soon!", Toast.LENGTH_SHORT).show();
        }
    }
}
