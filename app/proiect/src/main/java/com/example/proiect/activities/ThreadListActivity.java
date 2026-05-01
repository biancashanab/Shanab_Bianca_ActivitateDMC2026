package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.adapters.ThreadAdapter;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.example.proiect.models.ResearchThread;
import com.example.proiect.network.ApiClient;
import com.example.proiect.network.ResearchExportResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThreadListActivity extends AppCompatActivity {

    private ListView lvThreads;
    private ProgressBar pbThreads;
    private AppDatabaseHelper dbHelper;
    
    // URL-ul catre fisierul JSON public (exemplu raw github)
    private static final String REMOTE_JSON_URL = "https://raw.githubusercontent.com/username/repo/main/research_results.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_list);

        lvThreads = findViewById(R.id.lvThreads);
        pbThreads = findViewById(R.id.pbThreads);
        Button btnRefresh = findViewById(R.id.btnRefreshThreads);
        dbHelper = new AppDatabaseHelper(this);

        btnRefresh.setOnClickListener(v -> fetchResearchData());

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

    private void fetchResearchData() {
        pbThreads.setVisibility(View.VISIBLE);
        
        ApiClient.getApiService().getResearchResults(REMOTE_JSON_URL).enqueue(new Callback<ResearchExportResponse>() {
            @Override
            public void onResponse(Call<ResearchExportResponse> call, Response<ResearchExportResponse> response) {
                pbThreads.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    saveDataToSQLite(response.body().getThreads());
                    Toast.makeText(ThreadListActivity.this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                    loadLocalData();
                } else {
                    Toast.makeText(ThreadListActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                    loadLocalData(); // Fallback la date locale
                }
            }

            @Override
            public void onFailure(Call<ResearchExportResponse> call, Throwable t) {
                pbThreads.setVisibility(View.GONE);
                Toast.makeText(ThreadListActivity.this, "Network error. Loading local data.", Toast.LENGTH_SHORT).show();
                loadLocalData(); // Fallback la date locale
            }
        });
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
            Toast.makeText(this, "No local data found. Please Refresh.", Toast.LENGTH_SHORT).show();
        }
    }
}
