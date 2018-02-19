package ru.kodep.vlad.weather;

        import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import ru.kodep.vlad.weather.entity.GuestProvaider;

/**
 * Created by vlad on 24.01.18
 */

public class AsyncLoaderOneScreen  extends  CursorLoader{

    private GuestProvaider guestProvaider;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    public AsyncLoaderOneScreen(Context context, GuestProvaider guestProvaider) {
        super(context);
        this.context = context;
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
        String mCity = new CityPreference(context).getCity();
        String selection = "cityname = ?";
        String[] selectionArgs = new String[]{mCity};
        return guestProvaider.query(selection, selectionArgs);
    }
}
