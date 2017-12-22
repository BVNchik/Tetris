package ru.kodep.vlad.weather;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.kodep.vlad.weather.entity.Response;

/**
 * Created by vlad on 21.12.17
 */

public class WeatherDataForAWeek {

    static Response getJSONDataForAWeek(Context context, int id) {
        String idCity = String.valueOf(id);
        try {
            URL url = new URL(String.format("http://samples.openweathermap.org/data/2.5/forecast/daily?id=si&appid=63a61c1386a276b62870f02488e79398", idCity));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));
            Response response = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), Response.class);



            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
