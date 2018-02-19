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

public class AsyncLoaderDataForToday extends CursorLoader {
    private static final long SECOND = 1000;

    private GuestProvaider guestProvaider;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String mCity;

    public AsyncLoaderDataForToday(Context context, GuestProvaider guestProvaider, String mCity) {
        super(context);
        this.mCity = mCity;
        this.guestProvaider = guestProvaider;
        this.context = context;
    }


    @Override
    public Cursor loadInBackground() {
        Log.i("ForToday", "запустился For Today");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        String todayData = String.valueOf((cal.getTimeInMillis() / SECOND - 43199));
        String todayDatas = String.valueOf((cal.getTimeInMillis() / SECOND + 43199));
        Log.i("ForToday", todayData + "  " + todayDatas);
        String selection = "cityname = ? AND data > ? AND data < ?";
        String[] selectionArgs = new String[]{mCity, todayData, todayDatas};
        return guestProvaider.query(selection, selectionArgs);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}