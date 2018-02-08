package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.util.Calendar;

import ru.kodep.vlad.weather.entity.GuestProvaider;

/**
 * Created by vlad on 24.01.18
 */

class AsyncLoaderDataForToday extends CursorLoader {
    private static final long SECOND = 1000;

    private  GuestProvaider guestProvaider;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    AsyncLoaderDataForToday(Context context, GuestProvaider guestProvaider) {
        super(context);
        this.guestProvaider = guestProvaider;
        this.context = context;
    }

    @SuppressLint("WrongConstant")
    @Override
    public Cursor loadInBackground() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        String todayData = String.valueOf((cal.getTimeInMillis() / SECOND - 43199));
        String todayDatas = String.valueOf((cal.getTimeInMillis() / SECOND + 43199));
        String selection = "cityname = ? AND data > ? AND data < ?";
        String citys = new CityPreference((Activity) context).getCity();
        String[] selectionArgs = new String[]{citys, todayData, todayDatas};
        return guestProvaider.query(selection, selectionArgs);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}