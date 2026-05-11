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

        // Validare câmpuri goale
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.err_empty_email));
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.err_empty_password));
            return;
        }

        // Validare format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.err_invalid_email));
            return;
        }

        // Validare lungime minimă (opțional pentru login, dar util pentru UX)
        if (password.length() < 8) {
            etPassword.setError(getString(R.string.err_password_short));
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnGoToRegister.setEnabled(false);

        // Simulăm o mică întârziere pentru UX (ProgressBar)
        new android.os.Handler().postDelayed(() -> {
            int userId = dbHelper.verifyLogin(email, password);
            pbLogin.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnGoToRegister.setEnabled(true);

            if (userId != -1) {
                PreferencesManager.saveLoggedUserEmail(this, email);
                Toast.makeText(this, R.string.msg_login_success, Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.msg_login_failed, Toast.LENGTH_SHORT).show();
            }
        }, 1200);
    }
}
