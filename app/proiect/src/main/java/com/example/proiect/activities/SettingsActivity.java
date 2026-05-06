package com.example.proiect.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.proiect.R;
import com.example.proiect.utils.PreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spDefaultMode, spDefaultPlanner, spDefaultSort;
    private SwitchCompat swAutoSave, swDarkMode;
    private CheckBox cbRecentOnly;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void initViews() {
        spDefaultMode = findViewById(R.id.spDefaultMode);
        spDefaultPlanner = findViewById(R.id.spDefaultPlanner);
        spDefaultSort = findViewById(R.id.spDefaultSort);
        swAutoSave = findViewById(R.id.swAutoSave);
        swDarkMode = findViewById(R.id.swDarkMode);
        cbRecentOnly = findViewById(R.id.cbRecentOnly);
        btnSave = findViewById(R.id.btnSaveSettings);
    }

    private void loadSettings() {
        // Load values from PreferencesManager
        String mode = PreferencesManager.getDefaultMode(this);
        String planner = PreferencesManager.getDefaultPlanner(this);
        String sort = PreferencesManager.getDefaultSort(this);
        boolean autoSave = PreferencesManager.isAutoSaveResults(this);
        boolean recentOnly = PreferencesManager.isRecentPapersOnly(this);
        boolean darkMode = PreferencesManager.isDarkMode(this);

        // Set Spinners
        setSpinnerValue(spDefaultMode, mode, R.array.modes_array);
        setSpinnerValue(spDefaultPlanner, planner, R.array.planners_array);
        setSpinnerValue(spDefaultSort, sort, R.array.sort_array);

        // Set Switches/CheckBox
        swAutoSave.setChecked(autoSave);
        cbRecentOnly.setChecked(recentOnly);
        swDarkMode.setChecked(darkMode);
    }

    private void setSpinnerValue(Spinner spinner, String value, int arrayResId) {
        String[] options = getResources().getStringArray(arrayResId);
        for (int i = 0; i < options.length; i++) {
            if (options[i].equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {
        String selectedMode = spDefaultMode.getSelectedItem().toString();
        String selectedPlanner = spDefaultPlanner.getSelectedItem().toString();
        String selectedSort = spDefaultSort.getSelectedItem().toString();
        boolean autoSave = swAutoSave.isChecked();
        boolean recentOnly = cbRecentOnly.isChecked();
        boolean darkMode = swDarkMode.isChecked();

        // Save to PreferencesManager
        PreferencesManager.saveDefaultMode(this, selectedMode);
        PreferencesManager.saveDefaultPlanner(this, selectedPlanner);
        PreferencesManager.saveDefaultSort(this, selectedSort);
        PreferencesManager.saveAutoSaveResults(this, autoSave);
        PreferencesManager.saveRecentPapersOnly(this, recentOnly);
        PreferencesManager.saveDarkMode(this, darkMode);

        Toast.makeText(this, "Setări salvate cu succes!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
