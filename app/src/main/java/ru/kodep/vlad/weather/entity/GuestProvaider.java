package ru.kodep.vlad.weather.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.kodep.vlad.weather.DBHelper;

/**
 * Created by vlad on 22.01.18
 */

public class GuestProvaider {

    private static final String WEATHER_TABLE = "WeatherTable";


    private SQLiteDatabase db;
    private Context context;

    public GuestProvaider(Context context) {
        this.context = context;

    }

    public void open() {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public Cursor query(String selection, String[] selectionArgs) {

        return db.query(WEATHER_TABLE, null, selection,
                selectionArgs, null, null, null);
    }

    public long insert(ContentValues values) {

        return db.insert(WEATHER_TABLE, null, values);
    }

    public int update(ContentValues values, String selection,
                      String[] selectionArgs) {
        return db.update(WEATHER_TABLE, values, selection, selectionArgs);
    }
}
