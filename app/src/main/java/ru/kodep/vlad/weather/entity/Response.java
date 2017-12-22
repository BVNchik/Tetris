package ru.kodep.vlad.weather.entity;

/**
 * Created by vlad on 21.12.17
 */

import java.util.List;

/**
 * Created by vlad on 21.12.17
 */

public class Response {

    private String cod;
    private int message;
    private int cnt;
    private List<DayForecast> list;

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<DayForecast> getList() {
        return list;
    }
}
