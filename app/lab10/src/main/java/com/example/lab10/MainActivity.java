package com.example.lab10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Spinner spinnerDays;
    private Button buttonSearch;
    private TextView textViewResult;

    private static final String API_KEY = BuildConfig.ACCUWEATHER_API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        spinnerDays = findViewById(R.id.spinnerDays);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewResult = findViewById(R.id.textViewResult);

        String[] options = {"1 zi", "5 zile"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextCity.getText().toString().trim();

                if (city.isEmpty()) {
                    textViewResult.setText(R.string.introduceti_numele_unui_oras);
                    return;
                }

                String selectedOption = spinnerDays.getSelectedItem().toString();
                int days = getNumberOfDays(selectedOption);

                new WeatherTask().execute(city, String.valueOf(days));
            }
        });
    }

    private int getNumberOfDays(String selectedOption) {
        if (selectedOption.startsWith("5")) {
            return 5;
        }
        return 1;
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            textViewResult.setText("Se incarca...");
        }

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            int days = Integer.parseInt(params[1]);

            try {
                String cityKey = getCityKey(city);

                if (cityKey == null) {
                    return "Nu s-a gasit orasul introdus.";
                }

                return getForecast(city, cityKey, days);

            } catch (Exception e) {
                e.printStackTrace();
                return "Eroare: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResult.setText(result);
        }
    }

    private String getCityKey(String city) throws Exception {       // caut codul
        String encodedCity = URLEncoder.encode(city, "UTF-8");

        String urlString = "https://dataservice.accuweather.com/locations/v1/cities/search"
                + "?apikey=" + API_KEY
                + "&q=" + encodedCity;

        String jsonResponse = makeHttpRequest(urlString);

        JSONArray array = new JSONArray(jsonResponse);

        if (array.length() == 0) {
            return null;
        }

        JSONObject firstCity = array.getJSONObject(0);

        return firstCity.getString("Key");
    }

    //caut vremea
    private String getForecast(String city, String cityKey, int days) throws Exception {
        String methodName;

        if (days == 1) {
            methodName = "1day";
        } else {
            methodName = "5day";
        }

        String urlString = "https://dataservice.accuweather.com/forecasts/v1/daily/"
                + methodName
                + "/"
                + cityKey
                + "?apikey=" + API_KEY
                + "&metric=true"
                + "&language=ro-ro";

        String jsonResponse = makeHttpRequest(urlString);

        JSONObject root = new JSONObject(jsonResponse);
        JSONArray dailyForecasts = root.getJSONArray("DailyForecasts");

        StringBuilder result = new StringBuilder();

        result.append("Oraș: ").append(city).append("\n");
        result.append("Cod oraș: ").append(cityKey).append("\n");
        result.append("Prognoză pentru ").append(days).append(days == 1 ? " zi" : " zile").append(":\n\n");

        for (int i = 0; i < dailyForecasts.length(); i++) {
            JSONObject forecast = dailyForecasts.getJSONObject(i);

            String date = forecast.getString("Date");

            JSONObject temperature = forecast.getJSONObject("Temperature");
            JSONObject minimum = temperature.getJSONObject("Minimum");
            JSONObject maximum = temperature.getJSONObject("Maximum");

            double minValue = minimum.getDouble("Value");
            double maxValue = maximum.getDouble("Value");

            String minUnit = minimum.getString("Unit");
            String maxUnit = maximum.getString("Unit");

            result.append("Ziua ").append(i + 1).append("\n");
            result.append("Data: ").append(date.substring(0, 10)).append("\n");
            result.append("Temperatura minimă: ").append(minValue).append(" °").append(minUnit).append("\n");
            result.append("Temperatura maximă: ").append(maxValue).append(" °").append(maxUnit).append("\n\n");
        }

        return result.toString();
    }

    private String makeHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();

        BufferedReader reader;

        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
        } else {
            reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream())
            );
        }

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        if (responseCode < 200 || responseCode >= 300) {
            throw new Exception("HTTP " + responseCode + ": " + response);
        }

        return response.toString();
    }
}
