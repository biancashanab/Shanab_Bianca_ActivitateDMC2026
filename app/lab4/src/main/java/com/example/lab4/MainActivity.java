package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_ADD = 1;
    static final int REQUEST_CODE_EDIT = 2;

    Button btnAdd;
    ListView listViewBookStores;
    ArrayList<BookStore> bookStoreList;
    BookStoreAdapter adapter;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        listViewBookStores = findViewById(R.id.listViewBookStores);

        bookStoreList = new ArrayList<>();

        adapter = new BookStoreAdapter(this, bookStoreList);
        listViewBookStores.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        listViewBookStores.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            BookStore selectedStore = bookStoreList.get(position);

            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            intent.putExtra("bookstore", selectedStore);
            intent.putExtra("isEdit", true);
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        });

        listViewBookStores.setOnItemLongClickListener((parent, view, position, id) -> {
            bookStoreList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "BookStore șters!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            BookStore store = data.getParcelableExtra("bookstore");

            if (store == null) return;

            if (requestCode == REQUEST_CODE_ADD) {
                bookStoreList.add(store);
                adapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_CODE_EDIT) {
                if (selectedPosition >= 0 && selectedPosition < bookStoreList.size()) {
                    bookStoreList.set(selectedPosition, store);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}