package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_ADD = 1;
    static final int REQUEST_CODE_EDIT = 2;

    Button btnAdd, btnSettings;
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
        btnSettings = findViewById(R.id.btnSettings);
        listViewBookStores = findViewById(R.id.listViewBookStores);

        bookStoreList = FileHelper.readBookStoresFromFile(this, FileHelper.ALL_BOOKSTORES_FILE);

        adapter = new BookStoreAdapter(this, bookStoreList);
        listViewBookStores.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        // SETTINGS
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        listViewBookStores.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            BookStore selectedStore = bookStoreList.get(position);

            Intent intent = new Intent(MainActivity.this, activity_add_bookstore.class);
            intent.putExtra("bookstore", (Parcelable) selectedStore);
            intent.putExtra("isEdit", true);
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        });

        // FAVORITE
        listViewBookStores.setOnItemLongClickListener((parent, view, position, id) -> {
            BookStore favoriteStore = bookStoreList.get(position);
            FileHelper.appendBookStoreToFile(this, FileHelper.FAVORITES_FILE, favoriteStore);
            Toast.makeText(MainActivity.this, "BookStore added to favorites!", Toast.LENGTH_SHORT).show();
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
                    BookStore existingStore = bookStoreList.get(selectedPosition);

                    existingStore.setName(store.getName());
                    existingStore.setNumberOfBooks(store.getNumberOfBooks());
                    existingStore.setOpen24h(store.isOpen24h());
                    existingStore.setAveragePrice(store.getAveragePrice());
                    existingStore.setStoreType(store.getStoreType());
                    existingStore.setOpeningDate(store.getOpeningDate());

                    FileHelper.writeBookStoresToFile(this, FileHelper.ALL_BOOKSTORES_FILE, bookStoreList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}