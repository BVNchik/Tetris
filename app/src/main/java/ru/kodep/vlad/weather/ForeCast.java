package ru.kodep.vlad.weather;

/**
 * Created by vlad on 14.12.17
 */

public class ForeCast {


    private String city, humidity, pressure;
    private  double gradus;


    ForeCast(String city, double gradus, String humidity, String pressure) {
        this.city = city;
        this.gradus = gradus;
        this.humidity = humidity;
        this.pressure = pressure;
    }
    public String getCity() { return city; }

    public void setCity(String city) {
        this.city = city;
    }

    public double getGradus() {
        return gradus;
    }

    public void setGradus(Double gradus) {
        this.gradus = gradus;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

}

