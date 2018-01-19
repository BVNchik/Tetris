package ru.kodep.vlad.weather;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.kodep.vlad.weather.entity.City;
import ru.kodep.vlad.weather.entity.Response;

/**
 * Created by vlad on 14.12.17
 */
class WeatherData {
    private static final String OPEN_WEATHER_MAP_API_DAY_GEO = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=63a61c1386a276b62870f02488e79398";
    private static final String OPEN_WEATHER_MAP_API_WEEK_GEO = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&appid=63a61c1386a276b62870f02488e79398";
    private static final String OPEN_WEATHER_MAP_API_WEEK_ID = "http://api.openweathermap.org/data/2.5/forecast/daily?id=%s&units=metric&appid=63a61c1386a276b62870f02488e79398";
    private static final String OPEN_WEATHER_MAP_API_DAY_NAME_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=63a61c1386a276b62870f02488e79398";
    //Единственный метод класса, который делает запрос на сервер и получает от него данные
    //Возвращает объект JSON или null
    static City getJSONDataDayGeo(Context context, String lat, String lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_DAY_GEO, lat, lon));
            return new Gson().fromJson(new InputStreamReader(createConnection(context, url).getInputStream()), City.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static Response getJSONDataWeekGeo(Context context, String lat, String lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_WEEK_GEO, lat, lon));
            return new Gson().fromJson(new InputStreamReader(createConnection(context,url).getInputStream()), Response.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static City getJSONDataDayNameCity(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_DAY_NAME_CITY, city));
            return new Gson().fromJson(new InputStreamReader(createConnection(context, url).getInputStream()), City.class);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    static Response getJSONDataWeekId(Context context, String id) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API_WEEK_ID, id));
            return new Gson().fromJson(new InputStreamReader(createConnection(context,url).getInputStream()), Response.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    private static HttpURLConnection createConnection(Context context, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));
        return connection;
    }
}
