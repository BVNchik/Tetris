package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import ru.kodep.vlad.weather.entity.GuestProvaider;

/**
 * Created by vlad on 24.01.18
 */

class AsyncLoaderDataAcquisition extends CursorLoader {

    private GuestProvaider guestProvaider;

    @SuppressLint("StaticFieldLeak")
    AsyncLoaderDataAcquisition(Context context, GuestProvaider guestProvaider) {
        super(context);
        this.guestProvaider = guestProvaider;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @SuppressLint("NewApi")
    @Override
    public Cursor loadInBackground() {
        return guestProvaider.query(null, null);
    }
}
