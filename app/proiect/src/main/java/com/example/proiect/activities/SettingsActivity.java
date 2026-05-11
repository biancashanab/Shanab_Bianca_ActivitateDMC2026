package com.example.proiect.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.proiect.R;
import com.example.proiect.utils.PreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spDefaultSort;
    private SwitchCompat swAutoSave, swDarkMode;
    private CheckBox cbRecentOnly;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.label_settings);
        }

        initViews();
        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
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
        spDefaultSort = findViewById(R.id.spDefaultSort);
        swAutoSave = findViewById(R.id.swAutoSave);
        swDarkMode = findViewById(R.id.swDarkMode);
        cbRecentOnly = findViewById(R.id.cbRecentOnly);
        btnSave = findViewById(R.id.btnSaveSettings);
    }

    private void loadSettings() {
        // Load values from PreferencesManager
        String sort = PreferencesManager.getDefaultSort(this);
        boolean autoSave = PreferencesManager.isAutoSaveResults(this);
        boolean recentOnly = PreferencesManager.isRecentPapersOnly(this);
        boolean darkMode = PreferencesManager.isDarkMode(this);

        // Set Spinner
        setSpinnerValue(spDefaultSort, sort, R.array.sort_array);

        // Set Switches/CheckBox
        swAutoSave.setChecked(autoSave);
        cbRecentOnly.setChecked(recentOnly);
        swDarkMode.setChecked(darkMode);
    }

    private void setSpinnerValue(Spinner spinner, String value, int arrayResId) {
        if (value == null) return;
        String[] options = getResources().getStringArray(arrayResId);
        for (int i = 0; i < options.length; i++) {
            if (options[i].equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {
        String selectedSort = spDefaultSort.getSelectedItem().toString();
        boolean autoSave = swAutoSave.isChecked();
        boolean recentOnly = cbRecentOnly.isChecked();
        boolean darkMode = swDarkMode.isChecked();

        // Save to PreferencesManager
        PreferencesManager.saveDefaultSort(this, selectedSort);
        PreferencesManager.saveAutoSaveResults(this, autoSave);
        PreferencesManager.saveRecentPapersOnly(this, recentOnly);
        PreferencesManager.saveDarkMode(this, darkMode);

        // Apply dark mode immediately
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Toast.makeText(this, R.string.success_save, Toast.LENGTH_SHORT).show();
        finish();
    }
}
