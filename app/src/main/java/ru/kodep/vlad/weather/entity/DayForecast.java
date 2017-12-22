package ru.kodep.vlad.weather.entity;

import java.util.List;

/**
 * Created by vlad on 21.12.17
 */

public class DayForecast {

    private long dt;
    private Temp temp;
    private List<Weather> weather;
    private Double pressure;
    private int humidity;
    private Double speed;
    private double deg;
    private int clouds;
    private double snow;

    public long getDt() {
        return dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Double getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public Double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public int getClouds() {
        return clouds;
    }

    public double getSnow() {
        return snow;
    }
}
