package com.example.proiect.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomChartView extends View {
    private List<Integer> values = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int[] colors = {Color.parseColor("#1A237E"), Color.parseColor("#C5A059"), Color.parseColor("#2E7D32"), Color.parseColor("#D84315"), Color.parseColor("#6A1B9A")};

    public CustomChartView(Context context) {
        super(context);
    }

    public CustomChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(Map<Integer, Integer> data) {
        values.clear();
        labels.clear();
        for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
            labels.add(String.valueOf(entry.getKey()));
            values.add(entry.getValue());
        }
        invalidate();
    }

    // Clasa extinde View pentru a desena manual graficul folosind Canvas si Paint.
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int n = values.size();
        int barWidth = width / n;

        int max = 0;
        for (int value : values) {
            if (value > max) max = value;
        }

        for (int i = 0; i < n; i++) {
            int value = values.get(i);
            // Calculam inaltimea barei raportand valoarea curenta la maximul din set.
            float barHeight = max == 0 ? 0 : (float) value / max * (height - 150);

            paint.setColor(colors[i % colors.length]);
            canvas.drawRect(i * barWidth + 30f,
                            height - barHeight - 60f,
                            (i + 1) * barWidth - 30f,
                            height - 60f, paint);

            // Labels and Values
            // Setam alinierea centrata pentru a pozitiona textul deasupra/sub bare.
            paint.setColor(Color.BLACK);
            paint.setTextSize(35);
            paint.setTextAlign(Paint.Align.CENTER);
            
            // Year label
            canvas.drawText(labels.get(i), i * barWidth + (barWidth / 2f), height - 20f, paint);
            
            // Count value
            paint.setFakeBoldText(true);
            canvas.drawText(String.valueOf(value), i * barWidth + (barWidth / 2f), height - barHeight - 80f, paint);
            paint.setFakeBoldText(false);
        }
    }
}