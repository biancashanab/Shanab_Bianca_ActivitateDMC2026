package com.example.proiect.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.adapters.PaperAdapter;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.utils.PreferencesManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResearchResultsActivity extends AppCompatActivity {

    private EditText etSearch;
    private Spinner spSource, spSort;
    private CheckBox cbFavorites;
    private ListView lvPapers;
    private AppDatabaseHelper dbHelper;
    private List<PaperItem> allPapers = new ArrayList<>();
    private List<PaperItem> filteredPapers = new ArrayList<>();
    private PaperAdapter adapter;
    private String threadId;
    private int currentUserId;
    private int filterYear = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_results);

        dbHelper = new AppDatabaseHelper(this);
        String userEmail = PreferencesManager.getLoggedUserEmail(this);
        currentUserId = dbHelper.getUserIdByEmail(userEmail);

        threadId = getIntent().getStringExtra("THREAD_ID");
        boolean showFavorites = getIntent().getBooleanExtra("SHOW_FAVORITES", false);

        initViews();
        loadData(showFavorites);
        setupFilters();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearchPaper);
        spSource = findViewById(R.id.spSource);
        spSort = findViewById(R.id.spSort);
        cbFavorites = findViewById(R.id.cbFavoritesOnly);
        lvPapers = findViewById(R.id.lvPapers);
        Button btnDate = findViewById(R.id.btnDateFilter);

        adapter = new PaperAdapter(this, filteredPapers);
        lvPapers.setAdapter(adapter);

        lvPapers.setOnItemClickListener((parent, view, position, id) -> {
            PaperItem paper = filteredPapers.get(position);
            Intent intent = new Intent(ResearchResultsActivity.this, PaperDetailsActivity.class);
            intent.putExtra("PAPER_ID", paper.getId());
            startActivity(intent);
        });

        btnDate.setOnClickListener(v -> showYearPicker());
    }

    private void loadData(boolean showFavorites) {
        if (showFavorites) {
            allPapers = dbHelper.loadFavoritePapers(currentUserId);
            cbFavorites.setChecked(true);
        } else if (threadId != null) {
            allPapers = dbHelper.loadPapersForThread(threadId);
        } else {
            allPapers = dbHelper.loadAllPapers();
        }
        applyFilters();
    }

    private void setupFilters() {
        // Setup Source Spinner
        Set<String> sources = new HashSet<>();
        sources.add("All Sources");
        for (PaperItem p : allPapers) {
            sources.add(p.getSource());
        }
        List<String> sourceList = new ArrayList<>(sources);
        Collections.sort(sourceList);
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sourceList);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSource.setAdapter(sourceAdapter);

        // Setup Sort Spinner
        String[] sortOptions = {"Sort by Year (Newest)", "Sort by Year (Oldest)", "Sort by Citations"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(sortAdapter);

        // Listeners
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        spSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cbFavorites.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
    }

    private void applyFilters() {
        String query = etSearch.getText().toString().toLowerCase();
        String selectedSource = spSource.getSelectedItem() != null ? spSource.getSelectedItem().toString() : "All Sources";
        boolean favoritesOnly = cbFavorites.isChecked();

        filteredPapers.clear();
        for (PaperItem p : allPapers) {
            boolean matchesSearch = p.getTitle().toLowerCase().contains(query);
            boolean matchesSource = selectedSource.equals("All Sources") || p.getSource().equals(selectedSource);
            boolean matchesFavorite = !favoritesOnly || dbHelper.isFavorite(currentUserId, p.getId());
            boolean matchesYear = filterYear == -1 || p.getYear() == filterYear;

            if (matchesSearch && matchesSource && matchesFavorite && matchesYear) {
                filteredPapers.add(p);
            }
        }

        sortPapers();
        adapter.notifyDataSetChanged();
    }

    private void sortPapers() {
        int sortPos = spSort.getSelectedItemPosition();
        Collections.sort(filteredPapers, (p1, p2) -> {
            if (sortPos == 0) { // Year Newest
                return Integer.compare(p2.getYear(), p1.getYear());
            } else if (sortPos == 1) { // Year Oldest
                return Integer.compare(p1.getYear(), p2.getYear());
            } else { // Citations
                return Integer.compare(p2.getCitationCount(), p1.getCitationCount());
            }
        });
    }

    private void showYearPicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month, dayOfMonth) -> {
            filterYear = year1;
            applyFilters();
        }, year, 0, 1);
        
        // Hide day and month if possible or just use it as year picker
        datePickerDialog.setTitle("Select Year");
        datePickerDialog.show();
    }
}
