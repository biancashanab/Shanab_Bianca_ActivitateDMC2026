package com.example.lab32;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "LC_Third";

    private int x = 0;
    private int y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Log.i(TAG, "onCreate()");

        Bundle extras = getIntent().getExtras();
        String msg = "(no message)";

        if (extras != null) {
            msg = extras.getString("msg", "(no messages)");
            x = extras.getInt("x", 0);
            y = extras.getInt("y", 0);
        }

        Toast.makeText(this, msg + " | x=" + x + " y=" + y, Toast.LENGTH_LONG).show();

        Button btnBack = findViewById(R.id.button2);
        btnBack.setOnClickListener(v -> {
            int sum = x + y;

            Intent back = new Intent();
            back.putExtra("back_msg", "Inapoi din ThirdActivity!");
            back.putExtra("sum", sum);

            setResult(RESULT_OK, back);
            finish();
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
}