package ru.kodep.vlad.weather.entity;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import ru.kodep.vlad.weather.DBHelper;

/**
 * Created by vlad on 22.01.18
 */

@SuppressLint("Registered")
public class GuestProvaider extends ContentProvider {
    final String LOG_TAG = "myLogs";
    // // Константы для БД
    // БД
    static final String DB_NAME = "WeatherTable";
    static final int DB_VERSION = 1;

    // Таблица
    static final String WEATHER_TABLE = "WeatherTable";

    // Поля
    static final String WEATHER_ID = "_id";
    static final String WEATHER_CITYNAME = "cityname";
    static final String WEATHER_DATA = "data";
    static final String WEATHER_TEMP = "temp";
    static final String WEATHER_HUMIDITY = "humidity";
    static final String WEATHER_PRESSURE = "pressure";
    static final String WEATHER_SPEED = "speed";


    static final String AUTHORITY = "ru.kodep.vlad.weather.WeatherData";

    // path
    static final String WEATHER_PATH = "WeatherTable";

    // Общий Uri
    public static final Uri WEATHER_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + WEATHER_PATH);

    // Типы данных
    // набор строк
    static final String WEATHER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WEATHER_PATH;

    // одна строка
    static final String WEATHER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WEATHER_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_WEATHER = 1;

    // Uri с указанным ID
    static final int URI_WEATHER_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, WEATHER_PATH, URI_WEATHER);
        uriMatcher.addURI(AUTHORITY, WEATHER_PATH + "/#", URI_WEATHER_ID);
    }
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.i("GuestProv", "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }
    // чтение
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_WEATHER: // общий Uri
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = WEATHER_CITYNAME + " ASC";
                }
                break;
            case URI_WEATHER_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = WEATHER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WEATHER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(WEATHER_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(),
                WEATHER_CONTENT_URI);
        return cursor;
    }

    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_WEATHER)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(WEATHER_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(WEATHER_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WEATHER:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_WEATHER_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WEATHER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WEATHER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(WEATHER_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WEATHER:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_WEATHER_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = WEATHER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + WEATHER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(WEATHER_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(@NonNull Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_WEATHER:
                return WEATHER_CONTENT_TYPE;
            case URI_WEATHER_ID:
                return WEATHER_CONTENT_ITEM_TYPE;
        }
        return null;
    }

}
