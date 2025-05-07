package com.example.weather_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather_app.R;
import com.example.weather_app.model.WeatherForecastModel;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private List<WeatherForecastModel> forecastList;

    public WeatherForecastAdapter(List<WeatherForecastModel> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherForecastModel forecast = forecastList.get(position);
        holder.dateTextView.setText(forecast.getDate());
        holder.temperatureTextView.setText(forecast.getTemperature() + "°C");
        holder.conditionTextView.setText(forecast.getWeatherCondition());

        // Load weather icon using Glide
        Glide.with(holder.weatherIcon.getContext())
                .load("https://openweathermap.org/img/w/" + forecast.getIcon() + ".png")
                .into(holder.weatherIcon);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, temperatureTextView, conditionTextView;
        ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateText);
            temperatureTextView = itemView.findViewById(R.id.tempText);
            conditionTextView = itemView.findViewById(R.id.conditionText);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
        }
    }
}
