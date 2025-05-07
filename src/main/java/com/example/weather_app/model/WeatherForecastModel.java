package com.example.weather_app.model;

public class WeatherForecastModel {
    private String date;
    private String temperature;
    private String weatherCondition;
    private String icon;

    public WeatherForecastModel(String date, String temperature, String weatherCondition, String icon) {
        this.date = date;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.icon = icon;
    }

    public String getDate() { return date; }
    public String getTemperature() { return temperature; }
    public String getWeatherCondition() { return weatherCondition; }
    public String getIcon() { return icon; }
}

