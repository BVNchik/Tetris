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
    String getId() { return prefs.getString("cityId", "5601538");}
   int getStart() { return (prefs.getInt("start", 0));}

    void setCity(String city) {
        prefs.edit().putString("city", city).apply();
    }
    void setId(String id) {
        prefs.edit().putString("cityId", id).apply();
    }
    void setStart(int start) { prefs.edit().putInt("start", (start)).apply();
    }
}



