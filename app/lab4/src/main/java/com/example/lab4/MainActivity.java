package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    TextView tvResult;

    static final int REQUEST_CODE_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        tvResult = findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK)
        {
            String name = data.getStringExtra("name");
            int books = data.getIntExtra("books",0);
            boolean open = data.getBooleanExtra("open",false);
            double price = data.getDoubleExtra("price",0);
            String type = data.getStringExtra("type");

            String result = "Name: " + name + "\nBooks: " + books + "\nOpen 24h: " + open + "\nAverage price: " + price + "\nType: " + type;
            tvResult.setText(result);
        }
    }
}