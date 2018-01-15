package ru.kodep.vlad.weather;

/**
 * Created by vlad on 14.12.17
 */

public class ForeCast {


    private Long dt;
    private Double humidity;
    private  Double temp;
    private  Double speed;
    private Double pressure;

    ForeCast(Long dt, Double temp, Double speed, Double humidity, Double pressure) {
        this.dt = dt;
        this.temp = temp;
        this.speed = speed;
        this.humidity = humidity;
        this.pressure = pressure;
    }
 public Long getDt() {return dt;}
 public Double getTemp() {return  temp;}
 public Double getSpeed() {return  speed;}
 public Double getHumidity() {return  humidity;}
 public Double getPressure() {return  pressure;}
}

