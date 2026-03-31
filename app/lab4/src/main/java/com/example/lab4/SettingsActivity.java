package com.example.lab4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    EditText etTextSize;
    Spinner spColor;
    Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        etTextSize = findViewById(R.id.etTextSize);
        spColor = findViewById(R.id.spColor);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        String[] colors = {"#FF007F", "#000000", "#FF0000", "#0000FF", "#008000", "#800080"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                colors
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("app_settings", MODE_PRIVATE);

        float currentSize = preferences.getFloat("text_size", 18f);
        String currentColor = preferences.getString("text_color", "#FF007F");

        etTextSize.setText(String.valueOf(currentSize));

        int position = adapter.getPosition(currentColor);
        if (position >= 0) {
            spColor.setSelection(position);
        }

        btnSaveSettings.setOnClickListener(v -> {
            String sizeText = etTextSize.getText().toString().trim();

            if (sizeText.isEmpty()) {
                Toast.makeText(this, "Enter text size", Toast.LENGTH_SHORT).show();
                return;
            }

            float textSize = Float.parseFloat(sizeText);
            String selectedColor = spColor.getSelectedItem().toString();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("text_size", textSize);
            editor.putString("text_color", selectedColor);
            editor.apply();

            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}