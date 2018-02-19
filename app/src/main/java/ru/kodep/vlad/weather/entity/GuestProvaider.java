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
    private DBHelper dbHelper;

    public GuestProvaider(Context context) {
        this.context = context;

    }
public void close() {
    if (dbHelper != null) dbHelper.close();
}
    public void open() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public Cursor query(String selection, String[] selectionArgs) {

        return db.query(WEATHER_TABLE, null, selection,
                selectionArgs, null, null, null);
    }

    public void insert(ContentValues values) {

        db.insert(WEATHER_TABLE, null, values);
    }

    public void update(ContentValues values, String selection,
                       String[] selectionArgs) {
        db.update(WEATHER_TABLE, values, selection, selectionArgs);
    }
}
