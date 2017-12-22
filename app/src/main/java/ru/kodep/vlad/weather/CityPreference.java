package ru.kodep.vlad.weather;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by vlad on 14.12.17
 */

class CityPreference {

    //Вспомогательный класс для хранения выбранного города
    private SharedPreferences prefs;

    CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // Возвращаем город по умолчанию, если SharedPreferences пустые
    String getCity() {
        return prefs.getString("city", "Moscow");
    }

    void setCity(String city) {
        prefs.edit().putString("city", city).apply();
    }
}


