package ru.kodep.vlad.weather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vlad on 14.12.17
 */

public class CityPreference {

    //Вспомогательный класс для хранения выбранного города
    private SharedPreferences prefs;

    public CityPreference(Context activity) {
        prefs = activity.getSharedPreferences("city",Activity.MODE_PRIVATE);
    }

    // Возвращаем город по умолчанию, если SharedPreferences пустые
    public String getCity() {
        return prefs.getString("city", "Moscow");
    }

    public void setCity(String city) { prefs.edit().putString("city", city).apply();
    }
}



