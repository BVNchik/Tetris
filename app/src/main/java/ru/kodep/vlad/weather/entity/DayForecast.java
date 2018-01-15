package ru.kodep.vlad.weather.entity;

/**
 * Created by vlad on 21.12.17
 */

public class DayForecast {

    private long dt;
    private Temp temp;
    private Double pressure;
    private Double humidity;
    private Double speed;

    public Double getPressure() {
        return pressure;
    }
    public Double getHumidity() {
        return humidity;
    }
    public Double getSpeed() {
        return speed;
    }

    public long getDt() {
        return dt;
    }

    public Double getTemp() {
        return temp.getDay();
    }
}
