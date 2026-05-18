package com.example.proiect.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.User;
import com.example.proiect.utils.PreferencesManager;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvStatThreads, tvStatFavorites, tvStatRated, tvStatAvgRating;
    private EditText etNewPassword;
    private AppDatabaseHelper dbHelper;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profilul Meu");
        }

        dbHelper = new AppDatabaseHelper(this);
        String email = PreferencesManager.getLoggedUserEmail(this);
        currentUserId = dbHelper.getUserIdByEmail(email);

        initViews();
        loadUserData();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvStatThreads = findViewById(R.id.tvStatThreads);
        tvStatFavorites = findViewById(R.id.tvStatFavorites);
        tvStatRated = findViewById(R.id.tvStatRated);
        tvStatAvgRating = findViewById(R.id.tvStatAvgRating);
        etNewPassword = findViewById(R.id.etProfileNewPassword);
        Button btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(v -> updatePassword());
    }

    private void loadUserData() {
        User user = dbHelper.getUserById(currentUserId);
        if (user != null) {
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
        }

        // Statistics
        int threadCount = dbHelper.loadThreads().size();
        int favoriteCount = dbHelper.loadFavoritePapers(currentUserId).size();
        int ratedCount = dbHelper.getRatedPapersCount(currentUserId);
        float avgRating = dbHelper.getAverageRating(currentUserId);

        tvStatThreads.setText(String.valueOf(threadCount));
        tvStatFavorites.setText(String.valueOf(favoriteCount));
        tvStatRated.setText(String.valueOf(ratedCount));
        tvStatAvgRating.setText(String.format(Locale.getDefault(), "%.1f / 5.0", avgRating));
    }

    private void updatePassword() {
        String newPass = etNewPassword.getText().toString().trim();
        if (newPass.length() < 8) {
            etNewPassword.setError(getString(R.string.err_password_short));
            return;
        }

        if (dbHelper.updatePassword(currentUserId, newPass)) {
            Toast.makeText(this, "Parola a fost actualizată cu succes!", Toast.LENGTH_SHORT).show();
            etNewPassword.setText("");
        } else {
            Toast.makeText(this, "Eroare la actualizarea parolei.", Toast.LENGTH_SHORT).show();
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
}