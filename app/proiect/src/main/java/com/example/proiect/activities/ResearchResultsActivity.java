package com.example.proiect.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private LinearLayout llLoading, llEmpty;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.label_research_threads);
        }

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
        llLoading = findViewById(R.id.llLoadingResults);
        llEmpty = findViewById(R.id.llEmptyResults);
        Button btnDate = findViewById(R.id.btnDateFilter);
        Button btnClear = findViewById(R.id.btnClearFilters);

        adapter = new PaperAdapter(this, filteredPapers);
        lvPapers.setAdapter(adapter);

        lvPapers.setOnItemClickListener((parent, view, position, id) -> {
            PaperItem paper = filteredPapers.get(position);
            Intent intent = new Intent(ResearchResultsActivity.this, PaperDetailsActivity.class);
            intent.putExtra("PAPER_ID", paper.getId());
            startActivity(intent);
        });

        btnDate.setOnClickListener(v -> showYearPicker());
        btnClear.setOnClickListener(v -> clearFilters());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data to show updated ratings/favorites when coming back from details
        loadData(getIntent().getBooleanExtra("SHOW_FAVORITES", false));
    }

    private void clearFilters() {
        etSearch.setText("");
        spSource.setSelection(0);
        spSort.setSelection(0);
        cbFavorites.setChecked(false);
        filterYear = -1;
        applyFilters();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_add_thread) {
            // Putem să refolosim logica de generare thread și aici pentru consistență
            showAddThreadDialog();
            return true;
        } else if (id == R.id.action_refresh) {
            loadData(getIntent().getBooleanExtra("SHOW_FAVORITES", false));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddThreadDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Nouă Temă de Cercetare");

        final EditText input = new EditText(this);
        input.setHint("Introdu subiectul de cercetare");
        input.setPadding(50, 40, 50, 40);
        builder.setView(input);

        builder.setPositiveButton("Generează", (dialog, which) -> {
            String query = input.getText().toString().trim();
            if (!query.isEmpty()) {
                createNewThread(query);
            }
        });
        builder.setNegativeButton("Anulează", null);
        builder.show();
    }

    private void createNewThread(String query) {
        String threadId = "custom_" + System.currentTimeMillis();
        com.example.proiect.models.ResearchThread newThread = new com.example.proiect.models.ResearchThread();
        newThread.setThreadId(threadId);
        newThread.setTitle("Research: " + (query.length() > 20 ? query.substring(0, 17) + "..." : query));
        newThread.setQuery(query);
        newThread.setMode("research");
        newThread.setPlanner("v2");
        newThread.setUpdatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
        
        dbHelper.insertOrUpdateThread(newThread);
        
        PaperItem dummy = new PaperItem();
        dummy.setId("p_" + threadId);
        dummy.setThreadId(threadId);
        dummy.setTitle("Advances in " + query);
        dummy.setAuthors("Autonomous Agent");
        dummy.setYear(2024);
        dummy.setSource("Academic Engine AI");
        dummy.setCitationCount(0);
        dummy.setAbstractText("Articol generat pentru tema: " + query);
        dbHelper.insertOrUpdatePaper(dummy);
        
        Toast.makeText(this, "Temă generată! O poți vedea în lista principală.", Toast.LENGTH_LONG).show();
    }

    private void loadData(boolean showFavorites) {
        llLoading.setVisibility(View.VISIBLE);
        llEmpty.setVisibility(View.GONE);
        lvPapers.setVisibility(View.GONE);

        // Simulăm o mică întârziere pentru UX local
        new android.os.Handler().postDelayed(() -> {
            llLoading.setVisibility(View.GONE);
            if (showFavorites) {
                allPapers = dbHelper.loadFavoritePapers(currentUserId);
                cbFavorites.setChecked(true);
            } else if (threadId != null) {
                allPapers = dbHelper.loadPapersForThread(threadId, currentUserId);
            } else {
                allPapers = dbHelper.loadAllPapers(currentUserId);
            }
            setupFilters(); // Refresh filters based on loaded papers
            applyFilters();
        }, 300);
    }

    private void setupFilters() {
        // Setup Source Spinner
        Set<String> sources = new HashSet<>();
        for (PaperItem p : allPapers) {
            if (p.getSource() != null) {
                sources.add(p.getSource());
            }
        }
        List<String> sourceList = new ArrayList<>(sources);
        Collections.sort(sourceList);
        // Adăugăm "Toate Sursele" la începutul listei deja sortate
        sourceList.add(0, getString(R.string.filter_all_sources));
        
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sourceList);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSource.setAdapter(sourceAdapter);
        spSource.setSelection(0);

        // Setup Sort Spinner
        String[] sortOptions = {
                getString(R.string.sort_year_newest),
                getString(R.string.sort_year_oldest),
                getString(R.string.sort_citations)
        };
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
        String allSourcesLabel = getString(R.string.filter_all_sources);
        String selectedSource = spSource.getSelectedItem() != null ? spSource.getSelectedItem().toString() : allSourcesLabel;
        boolean favoritesOnly = cbFavorites.isChecked();

        filteredPapers.clear();
        for (PaperItem p : allPapers) {
            boolean matchesSearch = p.getTitle().toLowerCase().contains(query) || 
                                    p.getAuthors().toLowerCase().contains(query);
            
            boolean matchesSource = selectedSource.equals(allSourcesLabel) || 
                                    (p.getSource() != null && p.getSource().equals(selectedSource));

            boolean matchesFavorite = !favoritesOnly || dbHelper.isFavorite(currentUserId, p.getId());
            boolean matchesYear = filterYear == -1 || p.getYear() == filterYear;

            if (matchesSearch && matchesSource && matchesFavorite && matchesYear) {
                filteredPapers.add(p);
            }
        }

        sortPapers();
        adapter.notifyDataSetChanged();

        if (filteredPapers.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            lvPapers.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            lvPapers.setVisibility(View.VISIBLE);
        }
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
        datePickerDialog.setTitle(R.string.title_select_year);
        datePickerDialog.show();
    }
}
