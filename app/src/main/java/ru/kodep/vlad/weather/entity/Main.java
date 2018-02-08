package ru.kodep.vlad.weather.entity;

/**
 * Created by vlad on 15.01.18
 */

public class Main {
    private Double temp;
    private Double pressure;
    private Double humidity;
    private Double tempMin;
    private Double tempMax;
   Double getTempMin() {return tempMin;}
   Double getTempMax() {return tempMax;}
    Double getHumidity() {
        return humidity;
    }
    Double getTemp() {
        return temp;
    }
    Double getPressure() {
        return pressure;
    }
}
