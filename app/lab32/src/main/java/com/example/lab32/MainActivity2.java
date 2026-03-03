package com.example.lab32;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "LC_Second";
    private static final int REQ_THIRD = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.i(TAG, "onCreate()");

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);

            Bundle b = new Bundle();
            b.putString("msg", "Salut din SecondActivity!");
            b.putInt("x", 7);
            b.putInt("y", 5);

            intent.putExtras(b);

            startActivityForResult(intent, REQ_THIRD);
        });
    }

    @Override protected void onStart()  { super.onStart();  logAll("onStart()"); }
    @Override protected void onResume() { super.onResume(); logAll("onResume()"); }
    @Override protected void onPause()  { logAll("onPause()"); super.onPause(); }
    @Override protected void onStop()   { logAll("onStop()");  super.onStop();  }

    private void logAll(String method) {
        Log.e(TAG, method + " -> Log.e");
        Log.w(TAG, method + " -> Log.w");
        Log.d(TAG, method + " -> Log.d");
        Log.i(TAG, method + " -> Log.i");
        Log.v(TAG, method + " -> Log.v");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_THIRD && resultCode == RESULT_OK && data != null) {
            String backMsg = data.getStringExtra("back_msg");
            int sum = data.getIntExtra("sum", 0);

            Toast.makeText(this, backMsg + " | suma=" + sum, Toast.LENGTH_LONG).show();
        }
    }
}