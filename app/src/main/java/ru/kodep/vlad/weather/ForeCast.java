package ru.kodep.vlad.weather;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static ru.kodep.vlad.weather.DBHelper.CITYNAME;
import static ru.kodep.vlad.weather.DBHelper.DATA;
import static ru.kodep.vlad.weather.DBHelper.HUMIDITY;
import static ru.kodep.vlad.weather.DBHelper.PRESSURE;
import static ru.kodep.vlad.weather.DBHelper.SPEED;
import static ru.kodep.vlad.weather.DBHelper.TEMPS;

/**
 * Created by vlad on 14.12.17
 */

public class ForeCast implements Parcelable {

    public static final Parcelable.Creator<ForeCast> CREATOR = new Parcelable.Creator<ForeCast>() {

        @Override
        public ForeCast createFromParcel(Parcel source) {
            return new ForeCast(source);
        }

        @Override
        public ForeCast[] newArray(int size) {
            return new ForeCast[size];
        }
    };
    private String cityName;
    private Long dt;
    private Double humidity;
    private Double temp;
    private Double speed;
    private Double pressure;

    public ForeCast(String cityName, Long dt, Double temp, Double speed, Double humidity, Double pressure) {
        this.cityName = cityName;
        this.dt = dt;
        this.temp = temp;
        this.speed = speed;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    private ForeCast(Parcel in) {
        String[] data = new String[1];
        in.readStringArray(data);
        cityName = data[0];
        long[] dataL = new long[1];
        in.readLongArray(dataL);
        dt = dataL[0];
        double[] dataD = new double[4];
        in.readDoubleArray(dataD);
        temp = dataD[0];
        speed = dataD[3];
        humidity = dataD[1];
        pressure = dataD[2];

    }

    static ForeCast fromCursor(Cursor cursor) {
        int cityNameColIndex = cursor.getColumnIndex(CITYNAME);
        int dtColIndex = cursor.getColumnIndex(DATA);
        int tempColIndex = cursor.getColumnIndex(TEMPS);
        int humidityColIndex = cursor.getColumnIndex(HUMIDITY);
        int pressureColIndex = cursor.getColumnIndex(PRESSURE);
        int speedColIndex = cursor.getColumnIndex(SPEED);
        String cityName = cursor.getString(cityNameColIndex);
        Long dt = cursor.getLong(dtColIndex);
        Double temp = cursor.getDouble(tempColIndex);
        Double humidity = cursor.getDouble(humidityColIndex);
        Double pressure = cursor.getDouble(pressureColIndex);
        Double speed = cursor.getDouble(speedColIndex);
        return new ForeCast(cityName, dt, temp, speed, humidity, pressure);
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getDt() {
        return dt;
    }

    private void setDt(long dt) {
        this.dt = dt;
    }

    public Double getTemp() {
        return temp;
    }

    private void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getSpeed() {
        return speed;
    }

    private void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,  int flags) {
        dest.writeStringArray(new String[]{cityName});
        dest.writeLongArray(new long[]{dt});
        dest.writeDoubleArray(new double[]{temp, speed, humidity, pressure});
    }
}

