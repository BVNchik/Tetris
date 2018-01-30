package ru.kodep.vlad.weather.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import ru.kodep.vlad.weather.DBHelper;

/**
 * Created by vlad on 22.01.18
 */

public class GuestProvaider {
    public static final String WEATHER_DATA = "data";
    public static final String WEATHER_TEMP = "temp";
    public static final String WEATHER_HUMIDITY = "humidity";
    public static final String WEATHER_PRESSURE = "pressure";
    public static final String WEATHER_SPEED = "speed";
    // // Константы для БД
    // БД
    private static final String DB_NAME = "WeatherTable";
    private static final int DB_VERSION = 1;
    // Таблица
    private static final String WEATHER_TABLE = "WeatherTable";
    // Поля
    private static final String WEATHER_ID = "_id";
    private static final String WEATHER_CITYNAME = "cityname";
    private static final String AUTHORITY = "ru.kodep.vlad.weather.WeatherData";
    // path
    private static final String WEATHER_PATH = "WeatherTable";
    // Типы данных
    // набор строк
    static final String WEATHER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WEATHER_PATH;
    // одна строка
    static final String WEATHER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WEATHER_PATH;
    // Общий Uri
    private static final Uri WEATHER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + WEATHER_PATH);
    //// UriMatcher
    // общий Uri
    private static final int URI_WEATHER = 1;
    // Uri с указанным ID
    private static final int URI_WEATHER_ID = 2;
    // описание и создание UriMatcher
//    private static final UriMatcher uriMatcher;
//
//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY, WEATHER_PATH, URI_WEATHER);
//        uriMatcher.addURI(AUTHORITY, WEATHER_PATH + "/#", URI_WEATHER_ID);
//    }

    private final String LOG_TAG = "myLogs";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

public GuestProvaider(Context context){
    this.context =context;

}
    // открыть подключение
    public void open() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (dbHelper != null) dbHelper.close();
    }


    public Cursor query (String selection, String[] selectionArgs) {

        return db.query(WEATHER_TABLE, null, selection,
                selectionArgs, null, null, null);
    }

    public long insert(ContentValues values) {

        return db.insert(WEATHER_TABLE, null, values);
    }
    public int update (ContentValues values, String selection,
                      String[] selectionArgs) {
        return db.update(WEATHER_TABLE, values, selection, selectionArgs);
    }
}
