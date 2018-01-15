package ru.kodep.vlad.weather;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.kodep.vlad.weather.entity.City;

/**
 * Created by vlad on 14.12.17
 */
class WeatherData {
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=63a61c1386a276b62870f02488e79398";

    //Единственный метод класса, который делает запрос на сервер и получает от него данные
    //Возвращает объект JSON или null
    static City getJSONData(Context context, String cityName) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, cityName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

            return new Gson().fromJson(new InputStreamReader(connection.getInputStream()), City.class);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
