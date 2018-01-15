package ru.kodep.vlad.weather.entity;

/**
 * Created by vlad on 21.12.17
 */

public class City {

    private String name;
    private String id;
    private Long dt;
    private Main main;



    public Double getTemp() {
        return main.getTemp();
    }
    public Double getHumidity() {
        return main.getHumidity();
    }
    public Double getPressure() {
        return main.getPressure();
    }

    public Long getDt() {
        return dt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
