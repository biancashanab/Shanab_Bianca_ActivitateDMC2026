package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proiect.R;
import com.example.proiect.adapters.DashboardAdapter;
import com.example.proiect.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private GridView gvDashboard;
    private List<DashboardAdapter.DashboardItem> dashboardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.label_dashboard);
        }

        gvDashboard = findViewById(R.id.gvDashboard);
        prepareItems();

        DashboardAdapter adapter = new DashboardAdapter(this, dashboardItems);
        gvDashboard.setAdapter(adapter);

        gvDashboard.setOnItemClickListener((parent, view, position, id) -> {
            String label = dashboardItems.get(position).label;
            handleNavigation(label);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            showLogoutConfirmation();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_refresh) {
            Toast.makeText(this, "Refreshing data...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareItems() {
        dashboardItems = new ArrayList<>();
        dashboardItems.add(new DashboardAdapter.DashboardItem("Profil", android.R.drawable.ic_menu_myplaces));
        dashboardItems.add(new DashboardAdapter.DashboardItem(getString(R.string.label_research_threads), android.R.drawable.ic_menu_search));
        dashboardItems.add(new DashboardAdapter.DashboardItem(getString(R.string.label_saved_papers), android.R.drawable.ic_menu_save));
        dashboardItems.add(new DashboardAdapter.DashboardItem(getString(R.string.label_map), android.R.drawable.ic_dialog_map));
        dashboardItems.add(new DashboardAdapter.DashboardItem(getString(R.string.label_analytics), android.R.drawable.ic_menu_sort_by_size));
        dashboardItems.add(new DashboardAdapter.DashboardItem(getString(R.string.label_settings), android.R.drawable.ic_menu_preferences));
    }

    private void handleNavigation(String label) {
        Intent intent = null;
        if (label.equals("Profil")) {
            intent = new Intent(this, ProfileActivity.class);
        } else if (label.equals(getString(R.string.label_research_threads))) {
            intent = new Intent(this, ThreadListActivity.class);
        } else if (label.equals(getString(R.string.label_saved_papers))) {
            intent = new Intent(this, ResearchResultsActivity.class);
            intent.putExtra("SHOW_FAVORITES", true);
        } else if (label.equals(getString(R.string.label_map))) {
            intent = new Intent(this, MapActivity.class);
        } else if (label.equals(getString(R.string.label_analytics))) {
            intent = new Intent(this, AnalyticsActivity.class);
        } else if (label.equals(getString(R.string.label_settings))) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        } else {
            String msg = String.format(getString(R.string.msg_coming_soon), label);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_logout)
                .setMessage("Sigur doriți să vă deconectați?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    PreferencesManager.clearAll(this);
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Anulează", null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_about)
                .setMessage("Academic Engine Mobile Lite\nVersiune 1.0\n\nProiect realizat pentru disciplina Sisteme Mobile.\n© 2024")
                .setPositiveButton("OK", null)
                .show();
    }
}
