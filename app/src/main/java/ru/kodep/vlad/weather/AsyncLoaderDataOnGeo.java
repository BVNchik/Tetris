package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.util.Objects;

import ru.kodep.vlad.weather.entity.City;
import ru.kodep.vlad.weather.entity.GuestProvaider;
import ru.kodep.vlad.weather.entity.Response;

/**
 * Created by vlad on 24.01.18
 */

class AsyncLoaderDataOnGeo extends CursorLoader {

    private GuestProvaider guestProvaider;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String lat;
    private String lon;

    AsyncLoaderDataOnGeo(Context context, GuestProvaider guestProvaider, String lat, String lon) {
        super(context);
        this.context = context;
        this.guestProvaider = guestProvaider;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();

    }

    @SuppressLint("NewApi")
    @Override
    public Cursor loadInBackground() {
        final City city;
        city = WeatherData.getJSONDataDayGeo(context, lat, lon);
        assert city != null;
        new CityPreference((Activity) context).setCity(city.getName());
        String id = city.getId();
        final Response response = WeatherData.getJSONDataWeekId(context, id);
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 7; i++) {
            String name = null, datas = null;
            assert response != null;
            String citys = response.getCity().getName();
            String data = String.valueOf(response.getList().get(i).getDt());
            String selection = "cityname = ? AND data = ?";
            String[] selectionArgs = new String[]{citys, data};
            Cursor h = guestProvaider.query(selection, selectionArgs);
            if (h.moveToFirst()) {
                int citynameColIndex = h.getColumnIndex("cityname");
                int dataColIndex = h.getColumnIndex("data");
                do {
                    name = h.getString(citynameColIndex);
                    datas = h.getString(dataColIndex);
                } while (h.moveToNext());
            }
            if (name == null | datas == null) {
                cv.put("cityname", response.getCity().getName());
                cv.put("temps", response.getList().get(i).getTemp());
                cv.put("humidity", response.getList().get(i).getHumidity());
                cv.put("pressure", response.getList().get(i).getPressure());
                cv.put("speed", response.getList().get(i).getSpeed());
                cv.put("data", response.getList().get(i).getDt());
                guestProvaider.insert(cv);

            } else if (Objects.equals(name, citys) & Objects.equals(datas, data)) {

                cv.put("cityname", response.getCity().getName());
                cv.put("temps", response.getList().get(i).getTemp());
                cv.put("humidity", response.getList().get(i).getHumidity());
                cv.put("pressure", response.getList().get(i).getPressure());
                cv.put("speed", response.getList().get(i).getSpeed());
                cv.put("data", response.getList().get(i).getDt());
                guestProvaider.update(cv, selection, selectionArgs);
            }
        }
        String selection = "cityname = ?";
        String cityname = response.getCity().getName();
        String[] selectionArgs = new String[]{cityname};
        return guestProvaider.query(selection, selectionArgs);
    }
}