package com.example.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etType, etBooks;
    private EditText etSearchName, etMinBooks, etMaxBooks;
    private EditText etDeleteValue, etLetter;
    private Button btnInsert, btnLoadAll, btnSearchByName, btnInterval;
    private Button btnDeleteGreater, btnDeleteLess, btnIncrement;
    private Button btnOpenImages;
    private ListView listViewStores;

    private ArrayAdapter<BookStore> adapter;
    private final List<BookStore> data = new ArrayList<>();

    private AppDatabase db;
    private BookStoreDao dao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        dao = db.bookStoreDao();

        initViews();
        initList();
        initActions();

        loadAllStores();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etBooks = findViewById(R.id.etBooks);

        etSearchName = findViewById(R.id.etSearchName);
        etMinBooks = findViewById(R.id.etMinBooks);
        etMaxBooks = findViewById(R.id.etMaxBooks);

        etDeleteValue = findViewById(R.id.etDeleteValue);
        etLetter = findViewById(R.id.etLetter);

        btnInsert = findViewById(R.id.btnInsert);
        btnLoadAll = findViewById(R.id.btnLoadAll);
        btnSearchByName = findViewById(R.id.btnSearchByName);
        btnInterval = findViewById(R.id.btnInterval);
        btnDeleteGreater = findViewById(R.id.btnDeleteGreater);
        btnDeleteLess = findViewById(R.id.btnDeleteLess);
        btnIncrement = findViewById(R.id.btnIncrement);
        btnOpenImages = findViewById(R.id.btnOpenImages);

        listViewStores = findViewById(R.id.listViewStores);
    }

    private void initList() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listViewStores.setAdapter(adapter);
    }

    private void initActions() {
        btnInsert.setOnClickListener(v -> insertStore());
        btnLoadAll.setOnClickListener(v -> loadAllStores());
        btnSearchByName.setOnClickListener(v -> searchByName());
        btnInterval.setOnClickListener(v -> searchByInterval());
        btnDeleteGreater.setOnClickListener(v -> deleteGreaterThan());
        btnDeleteLess.setOnClickListener(v -> deleteLessThan());
        btnIncrement.setOnClickListener(v -> incrementByLetter());

        btnOpenImages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookImagesActivity.class);
            startActivity(intent);
        });
    }

    private void insertStore() {
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String booksText = etBooks.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) || TextUtils.isEmpty(booksText)) {
            toast("Complete all fields for insert.");
            return;
        }

        int books = Integer.parseInt(booksText);

        executor.execute(() -> {
            dao.insert(new BookStore(name, type, books));
            runOnUiThread(() -> {
                clearInsertFields();
                toast("Inserted successfully.");
            });
            loadAllStores();
        });
    }

    private void loadAllStores() {
        executor.execute(() -> {
            List<BookStore> result = dao.getAll();
            runOnUiThread(() -> updateList(result));
        });
    }

    private void searchByName() {
        String name = etSearchName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            toast("Enter a name.");
            return;
        }

        executor.execute(() -> {
            BookStore store = dao.getByName(name);
            List<BookStore> result = new ArrayList<>();
            if (store != null) {
                result.add(store);
            }
            runOnUiThread(() -> updateList(result));
        });
    }

    private void searchByInterval() {
        String minText = etMinBooks.getText().toString().trim();
        String maxText = etMaxBooks.getText().toString().trim();

        if (TextUtils.isEmpty(minText) || TextUtils.isEmpty(maxText)) {
            toast("Enter min and max.");
            return;
        }

        int min = Integer.parseInt(minText);
        int max = Integer.parseInt(maxText);

        executor.execute(() -> {
            List<BookStore> result = dao.getByBooksInterval(min, max);
            runOnUiThread(() -> updateList(result));
        });
    }

    private void deleteGreaterThan() {
        String valueText = etDeleteValue.getText().toString().trim();

        if (TextUtils.isEmpty(valueText)) {
            toast("Enter delete threshold.");
            return;
        }

        int value = Integer.parseInt(valueText);

        executor.execute(() -> {
            int deleted = dao.deleteWhereBooksGreaterThan(value);
            runOnUiThread(() -> toast("Deleted: " + deleted));
            loadAllStores();
        });
    }

    private void deleteLessThan() {
        String valueText = etDeleteValue.getText().toString().trim();

        if (TextUtils.isEmpty(valueText)) {
            toast("Enter delete threshold.");
            return;
        }

        int value = Integer.parseInt(valueText);

        executor.execute(() -> {
            int deleted = dao.deleteWhereBooksLessThan(value);
            runOnUiThread(() -> toast("Deleted: " + deleted));
            loadAllStores();
        });
    }

    private void incrementByLetter() {
        String letter = etLetter.getText().toString().trim();

        if (TextUtils.isEmpty(letter)) {
            toast("Enter a letter.");
            return;
        }

        executor.execute(() -> {
            int updated = dao.incrementBooksForNamesStartingWith(letter);
            runOnUiThread(() -> toast("Updated: " + updated));
            loadAllStores();
        });
    }

    private void updateList(List<BookStore> result) {
        data.clear();
        data.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void clearInsertFields() {
        etName.setText("");
        etType.setText("");
        etBooks.setText("");
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}