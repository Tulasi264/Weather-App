package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import com.example.weather_app.model.WeatherForecastModel;
import com.example.weather_app.adapter.WeatherForecastAdapter;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    Button search;
    TextView show;
    RecyclerView recyclerView;

    // Declare forecastList here (before using it)
    List<WeatherForecastModel> forecastList = new ArrayList<>();
    WeatherForecastAdapter adapter; // Declare adapter globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show= findViewById(R.id.weatherDetails);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with an empty list
        adapter = new WeatherForecastAdapter(forecastList);
        recyclerView.setAdapter(adapter); // Set adapter initially

        search.setOnClickListener(v -> {
            String city = cityName.getText().toString().trim();
            if (!city.isEmpty()) {
//                fetchCurrentWeather(city); // Fetch current weather
                fetchWeatherForecast(city); // Fetch 5-day forecast
            } else {
                Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Fetch Current Weather using AsyncTask
    private void fetchCurrentWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=e0c95ee1274a18ddcd84ad4d65713987&units=metric";

        new getWeather().execute(url);
    }

    // Fetch Weather Forecast using Volley
    private void fetchWeatherForecast(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=8e80e1e4be52e28db47b67ec2bb3562d&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray list = response.getJSONArray("list");

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
                Set<String> addedDates = new HashSet<>();

                forecastList.clear(); // Clear previous data before adding new data

                for (int i = 0; i < list.length(); i++) {
                    JSONObject forecastObject = list.getJSONObject(i);
                    String dateTime = forecastObject.getString("dt_txt");

                    Date date = inputFormat.parse(dateTime);
                    String formattedDate = outputFormat.format(date);

                    if (!addedDates.contains(formattedDate) && dateTime.contains("12:00:00")) {
                        addedDates.add(formattedDate);

                        JSONObject main = forecastObject.getJSONObject("main");
                        String temperature = String.valueOf(main.getDouble("temp"));

                        JSONArray weatherArray = forecastObject.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");

                        forecastList.add(new WeatherForecastModel(formattedDate, temperature, description, icon));
                    }
                }

                // Update RecyclerView adapter
                WeatherForecastAdapter adapter = new WeatherForecastAdapter(forecastList);
                recyclerView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(MainActivity.this, "Failed to load forecast", Toast.LENGTH_SHORT).show();
        });

        queue.add(request);
    }



// AsyncTask to fetch current weather
    class getWeather extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                java.net.URL url = new java.net.URL(urls[0]);
                java.net.HttpURLConnection urlConnection = (java.net.HttpURLConnection) url.openConnection();
                urlConnection.connect();

                java.io.InputStream inputStream = urlConnection.getInputStream();
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main = jsonObject.getJSONObject("main");

                double tempCelsius = main.getDouble("temp");
                double feelsLikeCelsius = main.getDouble("feels_like");
                double tempMinCelsius = main.getDouble("temp_min");
                double tempMaxCelsius = main.getDouble("temp_max");

                int pressure = main.getInt("pressure");
                int humidity = main.getInt("humidity");

                String weatherInfo = "Temperature: " + String.format("%.2f", tempCelsius) + "°C\n" +
                        "Feels Like: " + String.format("%.2f", feelsLikeCelsius) + "°C\n" +
                        "Min Temperature: " + String.format("%.2f", tempMinCelsius) + "°C\n" +
                        "Max Temperature: " + String.format("%.2f", tempMaxCelsius) + "°C\n" +
                        "Pressure: " + pressure + " hPa\n" +
                        "Humidity: " + humidity + "%";

                show.setText(weatherInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
