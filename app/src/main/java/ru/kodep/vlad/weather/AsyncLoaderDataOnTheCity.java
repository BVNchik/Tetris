package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import ru.kodep.vlad.weather.entity.GuestProvaider;


/**
 * Created by vlad on 24.01.18
 */

class AsyncLoaderDataOnTheCity extends CursorLoader {

    private GuestProvaider guestProvaider;
    private String mCity;
    @SuppressLint("StaticFieldLeak")
    Context context;

    AsyncLoaderDataOnTheCity(Context context, GuestProvaider guestProvaider, String mCity) {
        super(context);
        this.guestProvaider = guestProvaider;
        this.mCity = mCity;
    this.context = context;
    }


    @Override
    public Cursor loadInBackground() {
        String selection = "cityname = ?";
        String citys =  new CityPreference((Activity) context).getCity();
        String[] selectionArgs = new String[]{citys};
        return guestProvaider.query(selection, selectionArgs);
    }


}
