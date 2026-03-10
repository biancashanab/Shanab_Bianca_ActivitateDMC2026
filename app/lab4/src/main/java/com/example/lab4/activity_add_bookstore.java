package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class activity_add_bookstore extends AppCompatActivity {

    EditText etName, etBooks, etPrice;
    CheckBox cbOpen24;
    Spinner spType;
    Button btnSave;

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

        String[] types = {"ONLINE", "PHYSICAL"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString();
            int books = Integer.parseInt(etBooks.getText().toString());
            double price = Double.parseDouble(etPrice.getText().toString());
            boolean open = cbOpen24.isChecked();
            String selected = spType.getSelectedItem().toString();
            BookStore.StoreType type = BookStore.StoreType.valueOf(selected);

            BookStore store = new BookStore(name, books, open, type, price);

            Intent intent = new Intent();
            intent.putExtra("name", store.getName());
            intent.putExtra("books", store.getNumberOfBooks());
            intent.putExtra("open", store.isOpen24h());
            intent.putExtra("price", store.getAveragePrice());
            intent.putExtra("type", store.getStoreType().toString());

            setResult(RESULT_OK, intent);
            finish();
        });
    }
}