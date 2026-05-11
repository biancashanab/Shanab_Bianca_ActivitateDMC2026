package com.example.proiect.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private ProgressBar pbRegister;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new AppDatabaseHelper(this);

        etName = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnCreateAccount);
        pbRegister = findViewById(R.id.pbRegister);

        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validare câmpuri goale
        if (name.isEmpty()) {
            etName.setError(getString(R.string.err_empty_name));
            return;
        }
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

        // Validare lungime parolă (minim 8 caractere)
        if (password.length() < 8) {
            etPassword.setError(getString(R.string.err_password_short));
            return;
        }

        pbRegister.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        // Simulăm procesarea
        new android.os.Handler().postDelayed(() -> {
            if (dbHelper.insertUser(name, email, password)) {
                pbRegister.setVisibility(View.GONE);
                Toast.makeText(this, R.string.msg_register_success, Toast.LENGTH_LONG).show();
                finish();
            } else {
                pbRegister.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                Toast.makeText(this, R.string.err_register_failed, Toast.LENGTH_SHORT).show();
            }
        }, 1500);
    }
}
