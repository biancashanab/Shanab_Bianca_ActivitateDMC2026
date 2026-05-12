package com.example.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner spinnerCount = findViewById(R.id.spinnerCount);
        Spinner spinnerChartType = findViewById(R.id.spinnerChartType);
        
        LinearLayout row1 = findViewById(R.id.row1);
        LinearLayout row2 = findViewById(R.id.row2);
        LinearLayout row3 = findViewById(R.id.row3);
        LinearLayout row4 = findViewById(R.id.row4);
        LinearLayout row5 = findViewById(R.id.row5);

        EditText etValue1 = findViewById(R.id.etValue1);
        EditText etValue2 = findViewById(R.id.etValue2);
        EditText etValue3 = findViewById(R.id.etValue3);
        EditText etValue4 = findViewById(R.id.etValue4);
        EditText etValue5 = findViewById(R.id.etValue5);

        spinnerCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    row1.setVisibility(View.GONE);
                    row2.setVisibility(View.GONE);
                    row3.setVisibility(View.GONE);
                    row4.setVisibility(View.GONE);
                    row5.setVisibility(View.GONE);
                    return;
                }
                row1.setVisibility(position >= 1 ? View.VISIBLE : View.GONE);
                row2.setVisibility(position >= 2 ? View.VISIBLE : View.GONE);
                row3.setVisibility(position >= 3 ? View.VISIBLE : View.GONE);
                row4.setVisibility(position >= 4 ? View.VISIBLE : View.GONE);
                row5.setVisibility(position >= 5 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button btnShowChart = findViewById(R.id.btnShowChart);
        btnShowChart.setOnClickListener(v -> {
            ArrayList<Integer> valuesList = new ArrayList<>();
            int count = spinnerCount.getSelectedItemPosition();
            
            if (count == 0) {
                Toast.makeText(this, "Alege un numar de valori!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (count >= 1)
                addValueToList(valuesList, etValue1);
            if (count >= 2)
                addValueToList(valuesList, etValue2);
            if (count >= 3)
                addValueToList(valuesList, etValue3);
            if (count >= 4)
                addValueToList(valuesList, etValue4);
            if (count >= 5)
                addValueToList(valuesList, etValue5);

            if (valuesList.size() < count) {
                Toast.makeText(this, "Introduceti toate valorile!", Toast.LENGTH_SHORT).show();
                return;
            }

            String chartType = spinnerChartType.getSelectedItem().toString();

            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            intent.putIntegerArrayListExtra("valuesList", valuesList);
            intent.putExtra("chartType", chartType);
            startActivity(intent);
        });
    }

    private void addValueToList(ArrayList<Integer> list, EditText etValue) {
        String valStr = etValue.getText().toString().trim();
        if (!valStr.isEmpty()) {
            try {
                list.add(Integer.parseInt(valStr));
            } catch (NumberFormatException ignored) {}
        }
    }
}