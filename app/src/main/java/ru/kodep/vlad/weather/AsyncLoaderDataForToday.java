package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import java.util.Calendar;

import ru.kodep.vlad.weather.entity.GuestProvaider;

/**
 * Created by vlad on 24.01.18
 */

class AsyncLoaderDataForToday extends CursorLoader{
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;


    private GuestProvaider guestProvaider;
private String mCity;
    AsyncLoaderDataForToday(Context context, GuestProvaider guestProvaider, String mCity) {
        super(context);
        this.guestProvaider = guestProvaider;
        this.mCity = mCity;
        Log.i("AsyncLoaderDataOnGeo", "Открылась в AL");
    }

    @SuppressLint("WrongConstant")
    @Override
    public Cursor loadInBackground() {
        Log.i("AsyncLoaderDataOnGeo", "loadInBack");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        String todayData = String.valueOf((cal.getTimeInMillis() / SECOND - 43199));
        String todayDatas = String.valueOf((cal.getTimeInMillis() / SECOND + 43199));
        Log.i("dasd", todayData + " - " + todayDatas);
        String selection = "cityname = ? AND data > ? AND data < ?";
        String citys = mCity;
        String[] selectionArgs = new String[]{citys, todayData, todayDatas};

        return guestProvaider.query(selection, selectionArgs);
    }
}
