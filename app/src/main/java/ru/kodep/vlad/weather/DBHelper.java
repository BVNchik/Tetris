package ru.kodep.vlad.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vlad on 16.01.18
 */

public class DBHelper extends SQLiteOpenHelper {
   static final String CITYNAME = "cityname";
    static final String DATA = "data";
    static final String TEMPS = "temps";
    static final String HUMIDITY = "humidity";
    static final String PRESSURE = "pressure";
    static final String SPEED = "speed";


    public DBHelper(Context context) {
// конструктор суперкласса
        super(context, "WeatherTable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBH", "--- onCreate database ---");
// создаем таблицу с полями
        db.execSQL("create table WeatherTable (" +
                "id integer primary key autoincrement," +
                CITYNAME + " text," +
                DATA +" text," +
                TEMPS + " text," +
                HUMIDITY + " text," +
                PRESSURE + " text," +
                SPEED + " text" + ");");   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

