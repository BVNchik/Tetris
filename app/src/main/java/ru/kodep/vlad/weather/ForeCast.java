package ru.kodep.vlad.weather;

import android.database.Cursor;

import static ru.kodep.vlad.weather.DBHelper.DATA;
import static ru.kodep.vlad.weather.DBHelper.HUMIDITY;
import static ru.kodep.vlad.weather.DBHelper.PRESSURE;
import static ru.kodep.vlad.weather.DBHelper.SPEED;
import static ru.kodep.vlad.weather.DBHelper.TEMPS;

/**
 * Created by vlad on 14.12.17
 */

class ForeCast {


    private Long dt;
    private Double humidity;
    private Double temp;
    private Double speed;
    private Double pressure;

    private ForeCast(Long dt, Double temp, Double speed, Double humidity, Double pressure) {
        this.dt = dt;
        this.temp = temp;
        this.speed = speed;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    static ForeCast fromCursor(Cursor cursor) {

            int dtColIndex = cursor.getColumnIndex(DATA);
            int tempColIndex = cursor.getColumnIndex(TEMPS);
            int humidityColIndex = cursor.getColumnIndex(HUMIDITY);
            int pressureColIndex = cursor.getColumnIndex(PRESSURE);
            int speedColIndex = cursor.getColumnIndex(SPEED);

            Long dt = cursor.getLong(dtColIndex);
            Double temp = cursor.getDouble(tempColIndex);
            Double humidity = cursor.getDouble(humidityColIndex);
            Double speed = cursor.getDouble(speedColIndex);
            Double pressure = cursor.getDouble(pressureColIndex);
        return new ForeCast(dt, temp, speed, humidity, pressure);
    }

    Long getDt() {
        return dt;
    }

    private void setDt(long dt) {
        this.dt = dt;
    }

    Double getTemp() {
        return temp;
    }

    private void setTemp(Double temp) {
        this.temp = temp;
    }

    Double getSpeed() {
        return speed;
    }

    private void setSpeed(Double speed) {
        this.speed = speed;
    }

    Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
}

