package ru.kodep.vlad.weather.entity;

import java.util.List;

public class Response {
private City city;
    private String cod;
    private Double message;
    private int cnt;
    private List<DayForecast> list;

    public String getCod() {
        return cod;
    }

    public Double getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }
    public City getCity() {return city;}

    public List<DayForecast> getList() {
        return list;
    }
}
