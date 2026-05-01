package com.example.proiect.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
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

        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.insertUser(name, email, password)) {
            Toast.makeText(this, "Account created! You can now login.", Toast.LENGTH_LONG).show();
            finish(); // Ne intoarcem la Login
        } else {
            Toast.makeText(this, "Registration failed. Email might already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
