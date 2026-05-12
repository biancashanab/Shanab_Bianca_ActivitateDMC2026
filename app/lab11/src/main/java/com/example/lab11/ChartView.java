package com.example.lab11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChartView extends View {
    private ArrayList<Integer> data;
    private String chartType = "Column Chart";
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(ArrayList<Integer> data, String chartType) {
        this.data = data;
        this.chartType = chartType;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || data.isEmpty()) return;

        if ("Pie Chart".equals(chartType)) {
            drawPieChart(canvas);
        } else {
            drawColumnChart(canvas);
        }
    }

    private void drawColumnChart(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int n = data.size();
        int barWidth = width / n;

        int max = 0;
        for (int value : data) {
            if (value > max) max = value;
        }

        for (int i = 0; i < n; i++) {
            int value = data.get(i);
            float barHeight = max == 0 ? 0 : (float) value / max * (height - 200);      // dim max coloana

            paint.setColor(colors[i % colors.length]);
            canvas.drawRect(i * barWidth + 20f,             // spatiu 20 intre coloane
                            height - barHeight,
                            (i + 1) * barWidth - 20f,      // unde se termina coloana pe orizontala
                            height, paint);

            // etichetele
            paint.setColor(Color.LTGRAY);
            paint.setTextSize(45);
            canvas.drawText("Val " + (i + 1), i * barWidth + 30f, height - 30f, paint);
            canvas.drawText(String.valueOf(value), i * barWidth + 30f, height - barHeight, paint);
        }
    }

    private void drawPieChart(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 3f;
        
        RectF rectF = new RectF(width / 2f - radius,                        // patratul in care se incadreaza cercul
                                height / 2f - radius,
                                width / 2f + radius,
                                height / 2f + radius);

        float total = 0;
        for (int value : data) {
            total += value;             // suma tuturor nr
        }

        float startAngle = 0;
        for (int i = 0; i < data.size(); i++) {
            float sweepAngle = (total == 0) ? 0 : (data.get(i) / total) * 360f;     // fiecare cifra e o felie din 360 grade

            // desenez felia
            paint.setColor(colors[i % colors.length]);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);  // zona cerc, unghi unde incepe, cat de mare e

            // legenda
            paint.setTextSize(45);
            float legendY = height - 250 + (i * 50);
            
            // patratelul de culoare
            paint.setColor(colors[i % colors.length]);
            canvas.drawRect(50, legendY - 35, 85, legendY, paint);

            paint.setColor(Color.LTGRAY);
            canvas.drawText("Val " + (i + 1) + ": " + data.get(i), 110, legendY, paint);

            startAngle += sweepAngle;
        }
    }
}