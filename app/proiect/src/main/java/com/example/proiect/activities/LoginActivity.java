package com.example.proiect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.utils.PreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;
    private ProgressBar pbLogin;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new AppDatabaseHelper(this);
        
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        pbLogin = findViewById(R.id.pbLogin);

        btnLogin.setOnClickListener(v -> handleLogin());
        
        btnGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Simulam o mica intarziere pentru UX (ProgressBar)
        new android.os.Handler().postDelayed(() -> {
            int userId = dbHelper.verifyLogin(email, password);
            pbLogin.setVisibility(View.GONE);
            btnLogin.setEnabled(true);

            if (userId != -1) {
                PreferencesManager.saveLoggedUserEmail(this, email);
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }
}
