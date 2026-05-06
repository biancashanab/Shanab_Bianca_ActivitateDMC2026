package com.example.proiect.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.proiect.R;
import com.example.proiect.database.AppDatabaseHelper;
import com.example.proiect.models.PaperItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyticsActivity extends AppCompatActivity {

    private BarChart barChartYear;
    private PieChart pieChartSource;
    private HorizontalBarChart barChartCitations;
    private NestedScrollView scrollView;
    private TextView tvEmptyState;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        dbHelper = new AppDatabaseHelper(this);
        initViews();
        loadData();
    }

    private void initViews() {
        barChartYear = findViewById(R.id.barChartYear);
        pieChartSource = findViewById(R.id.pieChartSource);
        barChartCitations = findViewById(R.id.barChartCitations);
        scrollView = findViewById(R.id.scrollView);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        setupChartAppearance();
    }

    private void setupChartAppearance() {
        // Bar Chart Year
        barChartYear.getDescription().setEnabled(false);
        barChartYear.setDrawGridBackground(false);
        barChartYear.getLegend().setEnabled(false);

        // Pie Chart Source
        pieChartSource.getDescription().setEnabled(false);
        pieChartSource.setUsePercentValues(true);
        pieChartSource.setEntryLabelColor(Color.BLACK);
        pieChartSource.setCenterText("Surse Articole");
        pieChartSource.setCenterTextSize(16f);

        // Bar Chart Citations
        barChartCitations.getDescription().setEnabled(false);
        barChartCitations.setDrawGridBackground(false);
        barChartCitations.getLegend().setEnabled(false);
    }

    private void loadData() {
        Map<Integer, Integer> papersByYear = dbHelper.getPapersByYear();
        Map<String, Integer> papersBySource = dbHelper.getPapersBySource();
        List<PaperItem> topPapers = dbHelper.getTopPapersByCitations(5);

        if (papersByYear.isEmpty() && papersBySource.isEmpty() && topPapers.isEmpty()) {
            scrollView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            
            setupBarChartYear(papersByYear);
            setupPieChartSource(papersBySource);
            setupBarChartCitations(topPapers);
        }
    }

    private void setupBarChartYear(Map<Integer, Integer> data) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> years = new ArrayList<>();
        
        List<Integer> sortedYears = new ArrayList<>(data.keySet());
        Collections.sort(sortedYears);

        for (int i = 0; i < sortedYears.size(); i++) {
            int year = sortedYears.get(i);
            int count = data.get(year);
            entries.add(new BarEntry(i, count));
            years.add(String.valueOf(year));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Articole");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChartYear.setData(barData);

        XAxis xAxis = barChartYear.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < years.size()) {
                    return years.get(index);
                }
                return "";
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        barChartYear.animateY(1000);
        barChartYear.invalidate();
    }

    private void setupPieChartSource(Map<String, Integer> data) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Surse");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(3f);

        PieData pieData = new PieData(dataSet);
        pieChartSource.setData(pieData);
        pieChartSource.animateXY(1000, 1000);
        pieChartSource.invalidate();
    }

    private void setupBarChartCitations(List<PaperItem> topPapers) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        // Reverse to show the highest on top in HorizontalBarChart
        Collections.reverse(topPapers);

        for (int i = 0; i < topPapers.size(); i++) {
            PaperItem paper = topPapers.get(i);
            entries.add(new BarEntry(i, paper.getCitationCount()));
            
            String shortTitle = paper.getTitle();
            if (shortTitle.length() > 20) {
                shortTitle = shortTitle.substring(0, 17) + "...";
            }
            titles.add(shortTitle);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Citări");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChartCitations.setData(barData);

        XAxis xAxis = barChartCitations.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < titles.size()) {
                    return titles.get(index);
                }
                return "";
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        barChartCitations.animateY(1000);
        barChartCitations.invalidate();
    }
}
