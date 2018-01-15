package ru.kodep.vlad.weather.entity;

import java.util.List;

public class Response {

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

    public List<DayForecast> getList() {
        return list;
    }
}
