package com.example.lab4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class activity_add_bookstore extends AppCompatActivity {

    EditText etName, etBooks, etPrice;
    CheckBox cbOpen24;
    Spinner spType;
    Button btnSave, btnPickDate;
    Calendar selectedCalendar;
    boolean dateChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bookstore);

        etName = findViewById(R.id.etName);
        etBooks = findViewById(R.id.etBooks);
        etPrice = findViewById(R.id.etPrice);
        cbOpen24 = findViewById(R.id.cbOpen24);
        spType = findViewById(R.id.spType);
        btnSave = findViewById(R.id.btnSave);
        btnPickDate = findViewById(R.id.btnPickDate);
        selectedCalendar = Calendar.getInstance();

        String[] types = {"ONLINE", "PHYSICAL", "HYBRID"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);

        btnPickDate.setOnClickListener(v -> {
            int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);
            int month = selectedCalendar.get(Calendar.MONTH);
            int year = selectedCalendar.get(Calendar.YEAR);

            DatePickerDialog dialog = new DatePickerDialog(
                    activity_add_bookstore.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                        selectedCalendar.set(Calendar.MONTH, selectedMonth);
                        selectedCalendar.set(Calendar.YEAR, selectedYear);

                        String formattedDate = String.format(Locale.getDefault(), "Data: %02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);

                        btnPickDate.setText(formattedDate);
                        dateChosen = true;
                    },
                    year, month, day
            );
            dialog.show();
        });

        btnSave.setOnClickListener(v -> {

                String name = etName.getText().toString().trim();
                String booksText = etBooks.getText().toString().trim();
                String priceText = etPrice.getText().toString().trim();

                if (name.isEmpty() || booksText.isEmpty() || priceText.isEmpty() || !dateChosen) {
                    Toast.makeText(this, "Completeaza toate campurile!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int books = Integer.parseInt(booksText);
                double price = Double.parseDouble(priceText);
                boolean open = cbOpen24.isChecked();

                String selected = spType.getSelectedItem().toString();
                BookStore.StoreType type = BookStore.StoreType.valueOf(selected);
                Date openingDate = selectedCalendar.getTime();

                BookStore store = new BookStore(name, books, open, type, price, openingDate);

                Intent intent = new Intent();
                intent.putExtra("bookstore", store);

                setResult(RESULT_OK, intent);
                finish();
        });
    }
}