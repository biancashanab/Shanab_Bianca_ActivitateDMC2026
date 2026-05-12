package com.example.lab11;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ChartView chartView = findViewById(R.id.chartView);
        ArrayList<Integer> valuesList = getIntent().getIntegerArrayListExtra("valuesList");
        String chartType = getIntent().getStringExtra("chartType");
        
        if (valuesList != null) {
            chartView.setData(valuesList, chartType);
        }
    }
}