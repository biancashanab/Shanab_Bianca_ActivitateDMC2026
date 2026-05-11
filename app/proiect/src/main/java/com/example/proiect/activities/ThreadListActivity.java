package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proiect.R;
import com.example.proiect.adapters.ThreadAdapter;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.models.ResearchThread;
import com.example.proiect.network.ApiClient;
import com.example.proiect.network.ResearchExportResponse;
import com.example.proiect.utils.PreferencesManager;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThreadListActivity extends AppCompatActivity {

    private ListView lvThreads;
    private LinearLayout llLoading, llEmpty;
    private TextView tvEmptyMessage;
    private AppDatabaseHelper dbHelper;

    // URL-ul catre fisierul JSON public (exemplu raw github)
    private static final String REMOTE_JSON_URL = "https://raw.githubusercontent.com/username/repo/main/research_results.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.label_research_threads);
        }

        lvThreads = findViewById(R.id.lvThreads);
        llLoading = findViewById(R.id.llLoadingThreads);
        llEmpty = findViewById(R.id.llEmptyThreads);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessageThreads);
        Button btnRetry = findViewById(R.id.btnRetryThreads);
        
        dbHelper = new AppDatabaseHelper(this);

        btnRetry.setOnClickListener(v -> fetchResearchData());

        lvThreads.setOnItemClickListener((parent, view, position, id) -> {
            ResearchThread thread = (ResearchThread) parent.getItemAtPosition(position);
            Intent intent = new Intent(ThreadListActivity.this, ResearchResultsActivity.class);
            intent.putExtra("THREAD_ID", thread.getThreadId());
            intent.putExtra("THREAD_TITLE", thread.getTitle());
            startActivity(intent);
        });

        // Incarca datele locale la inceput
        loadLocalData();
        
        // Daca nu sunt date locale, incearca fetch automat
        if (dbHelper.loadThreads().isEmpty()) {
            fetchResearchData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_refresh) {
            fetchResearchData();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            PreferencesManager.clearAll(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchResearchData() {
        llLoading.setVisibility(View.VISIBLE);
        llEmpty.setVisibility(View.GONE);
        lvThreads.setVisibility(View.GONE);
        
        ApiClient.getApiService().getResearchResults(REMOTE_JSON_URL).enqueue(new Callback<ResearchExportResponse>() {
            @Override
            public void onResponse(Call<ResearchExportResponse> call, Response<ResearchExportResponse> response) {
                llLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    saveDataToSQLite(response.body().getThreads());
                    Toast.makeText(ThreadListActivity.this, "Date actualizate din sursa ONLINE", Toast.LENGTH_SHORT).show();
                    loadLocalData();
                } else {
                    handleFetchFailure("Eroare server (" + response.code() + "). Se încarcă date LOCAL FALLBACK.");
                }
            }

            @Override
            public void onFailure(Call<ResearchExportResponse> call, Throwable t) {
                llLoading.setVisibility(View.GONE);
                handleFetchFailure("Eroare rețea. Se încarcă date LOCAL FALLBACK.");
            }
        });
    }

    private void handleFetchFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (dbHelper.loadThreads().isEmpty()) {
            loadDataFromAssets();
        } else {
            loadLocalData();
        }
    }

    private void loadDataFromAssets() {
        try {
            InputStream is = getAssets().open("research_results.json");
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            ResearchExportResponse response = new Gson().fromJson(reader, ResearchExportResponse.class);
            
            if (response != null && response.getThreads() != null) {
                saveDataToSQLite(response.getThreads());
                Toast.makeText(this, "Date încărcate cu succes din ASSETS", Toast.LENGTH_SHORT).show();
            }
            loadLocalData();
        } catch (Exception e) {
            tvEmptyMessage.setText(R.string.error_loading_msg);
            llEmpty.setVisibility(View.VISIBLE);
            loadDemoData(); // Ultimul fallback
        }
    }

    private void loadDemoData() {
        // Fallback extrem daca nici assets nu merge
        ResearchThread t1 = new ResearchThread("t1", "Machine Learning Trends", "machine learning 2024", "research", "auto", "2024-05-20", null);
        dbHelper.insertOrUpdateThread(t1);

        PaperItem p1 = new PaperItem();
        p1.setId("p1"); p1.setThreadId("t1"); p1.setTitle("Deep Learning in 2024 (Demo)");
        p1.setAuthors("A. Ionescu"); p1.setYear(2024); p1.setSource("arXiv");
        p1.setCitationCount(50); p1.setAbstractText("A comprehensive study on LLMs.");
        dbHelper.insertOrUpdatePaper(p1);

        loadLocalData();
    }

    private void saveDataToSQLite(List<ResearchThread> threads) {
        if (threads == null) return;
        
        for (ResearchThread thread : threads) {
            dbHelper.insertOrUpdateThread(thread);
            
            if (thread.getPapers() != null) {
                for (PaperItem paper : thread.getPapers()) {
                    paper.setThreadId(thread.getThreadId());
                    dbHelper.insertOrUpdatePaper(paper);
                }
            }
        }
    }

    private void loadLocalData() {
        List<ResearchThread> threads = dbHelper.loadThreads();
        ThreadAdapter adapter = new ThreadAdapter(this, threads);
        lvThreads.setAdapter(adapter);
        
        if (threads.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            tvEmptyMessage.setText(R.string.empty_threads_msg);
            lvThreads.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            lvThreads.setVisibility(View.VISIBLE);
        }
    }
}
