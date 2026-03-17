package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_CODE_ADD = 1;
    Button btnAdd;
    ListView listViewBookStores;
    ArrayList<BookStore> bookStoreList;
    ArrayAdapter<BookStore> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        listViewBookStores = findViewById(R.id.listViewBookStores);

        bookStoreList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookStoreList);
        listViewBookStores.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        listViewBookStores.setOnItemClickListener((parent, view, position, id) -> {
            BookStore store = bookStoreList.get(position);
            Toast.makeText(MainActivity.this, store.toString(), Toast.LENGTH_SHORT).show();
        });

        listViewBookStores.setOnItemLongClickListener((parent, view, position, id) -> {
            bookStoreList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "BookStore șters!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK && data != null) {
            BookStore store = (BookStore) data.getSerializableExtra("bookstore");
            if (store != null) {
                bookStoreList.add(store);
                adapter.notifyDataSetChanged();
            }
        }
    }
}