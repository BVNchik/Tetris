package ru.kodep.vlad.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vlad on 16.01.18
 */

public class DBHelper extends SQLiteOpenHelper {

    DBHelper(MainActivity context) {
// конструктор суперкласса
        super(context, "WeatherTable", null, 1);
    }

    public DBHelper(Context context) {
        super(context, "WeatherTable", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DBH", "--- onCreate database ---");
// создаем таблицу с полями
        db.execSQL("create table WeatherTable (" + "id integer primary key autoincrement," + "cityname text," +"data text," + "temps text," + "humidity text," +"pressure text,"+"speed text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

