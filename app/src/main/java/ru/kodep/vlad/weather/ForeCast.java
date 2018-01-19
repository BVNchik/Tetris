package ru.kodep.vlad.weather;

/**
 * Created by vlad on 14.12.17
 */

class ForeCast {


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



    Long getDt() {return dt;}
 Double getTemp() {return  temp;}
 Double getSpeed() {return  speed;}
 Double getHumidity() {return  humidity;}
 Double getPressure() {return  pressure;}
}

