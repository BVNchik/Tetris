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
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    //Единственный метод класса, который делает запрос на сервер и получает от него данные
    //Возвращает объект JSON или null
    static City getJSONData(Context context, String cityName) {
        cityName = "Moscow";
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, cityName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_maps_app_id));

            City city = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), City.class);
            return city;

//            BufferedReader bufferedReader = new BufferedReader();
//
//            StringBuilder json = new StringBuilder(1024);
//            String tmp = "";
//            while ((tmp = bufferedReader.readLine()) != null)
//                json.append(tmp).append("\n");
//            bufferedReader.close();
//            JSONObject data = new JSONObject(json.toString());
//            Log.d("Weather", "data " + data);
//            if (data.getInt("cod") != 200) return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
