package ru.kodep.vlad.weather.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.kodep.vlad.weather.AsyncLoaderDataAcquisition;
import ru.kodep.vlad.weather.AsyncLoaderDataForToday;
import ru.kodep.vlad.weather.AsyncLoaderDataOnGeo;
import ru.kodep.vlad.weather.AsyncLoaderOneScreen;
import ru.kodep.vlad.weather.AsyncLoaderUpdatingAndAddingData;
import ru.kodep.vlad.weather.CityPreference;
import ru.kodep.vlad.weather.DataAdapter;
import ru.kodep.vlad.weather.ForeCast;
import ru.kodep.vlad.weather.GeoLocation;
import ru.kodep.vlad.weather.MainActivity;
import ru.kodep.vlad.weather.R;
import ru.kodep.vlad.weather.entity.GuestProvaider;

/**
 * Created by vlad on 09.02.18
 */

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements DataAdapter.OnForeCastClickListener, GeoLocation.OnLocationChangedCallback, LoaderManager.LoaderCallbacks<Cursor>, FragmentManager.OnBackStackChangedListener {
    static final int ASYNC_LOADER_DATA_FOR_TODAY = 1;
    static final int ASYNC_LOADER_OPENING_THE_APPLICATION = 4;
    static final int ASYNC_LOADER_UPDATING_AND_ADDING_DATA = 3;
    static final int ASYNC_LOADER_DATA_ON_GEO = 10;
    static final int ASYNC_LOADER_UPDATING_AND_ADDING_DATA_CITY_NAME = 2;

    TextView city;
    TextView temp;
    TextView humidity;
    TextView pressure;
    View view;
    ProgressBar progressBar;
    DataAdapter adapter;
    FragmentWeather fragmentWeather;
    RecyclerView recyclerView;
    MainActivity act;
    String lat, lon;
    GuestProvaider guestProvaider;
    GeoLocation geo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        guestProvaider = new GuestProvaider(getActivity().getApplicationContext());
        guestProvaider.open();
        getLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, null);
        progressBar = view.findViewById(R.id.pbLoading);
        progressBar.setVisibility(View.VISIBLE);
        Log.i("Fragment", "otobrazilas");
        return view;

    }


    @SuppressLint("NewApi")
    public void addInAdapter(@NonNull Cursor cursor) {
        recyclerView = view.findViewById(R.id.rvListForeCast);
        adapter = new DataAdapter(this, cursor, this);
        recyclerView.setAdapter(adapter);
        Log.i("MainFragment", "Отобразился текст во фрагменте");
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        act = (MainActivity) getActivity();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void displayWeather(Cursor cursor) {
        city = view.findViewById(R.id.tvCity);
        temp = view.findViewById(R.id.tvTemp);
        humidity = view.findViewById(R.id.tvHumidity);
        pressure = view.findViewById(R.id.tvPressure);

        if (cursor.moveToFirst()) {
            int citynameColIndex = cursor.getColumnIndex("cityname");
            int tempColIndex = cursor.getColumnIndex("temps");
            int humidityColIndex = cursor.getColumnIndex("humidity");
            int pressureColIndex = cursor.getColumnIndex("pressure");

            city.setText(cursor.getString(citynameColIndex));
            humidity.setText("Влажность: " + String.valueOf(cursor.getString(humidityColIndex)) + "%");
            temp.setText(String.format("%.1f", cursor.getDouble(tempColIndex)) + "\u00b0C");
            pressure.setText("Давление: " + String.valueOf(cursor.getString(pressureColIndex)) + "hPa");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onForeCastClick(ForeCast foreCast) {
        Log.i("FORECASTCLICK", "FORECASRCLICK");
        String cityName;
        Long data;
        Double temp;
        Double speed;
        Double humidity;
        Double pressure;
        cityName = foreCast.getCityName();
        data = foreCast.getDt();
        temp = foreCast.getTemp();
        humidity = foreCast.getHumidity();
        pressure = foreCast.getPressure();
        speed = foreCast.getSpeed();

        foreCast = new ForeCast(cityName, data, temp, humidity, pressure, speed);
        fragmentWeather = new FragmentWeather(foreCast);
        getFragmentManager()
                .addOnBackStackChangedListener(this);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, fragmentWeather, "fragmentWeather")
                .addToBackStack("fragmentWeather")
                .commit();
    }


    public void weatherAroundTheCity(String city) {
        new CityPreference(act).setCity(city);
        getLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA_CITY_NAME, null, this);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void visibleProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void showGeoDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(act);
        chooseCity.setTitle(R.string.geoSetting);
        chooseCity.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        chooseCity.setNeutralButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                act.finish();
            }
        });
        chooseCity.show();
    }


    @Override
    public void onLocationChanged(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        act.onGeo = 0;
        getLoaderManager().restartLoader(ASYNC_LOADER_DATA_ON_GEO, null, this);
    }

    @Override
    public void geoLocationSetting() {
        showGeoDialog();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        switch (id) {
            case ASYNC_LOADER_OPENING_THE_APPLICATION:
                loader = new AsyncLoaderOneScreen(act, guestProvaider);
                break;
            case ASYNC_LOADER_DATA_ON_GEO:
                loader = new AsyncLoaderDataOnGeo(act, guestProvaider, lat, lon);
                break;
            case ASYNC_LOADER_DATA_FOR_TODAY:
                String mCity = new CityPreference(act).getCity();
                loader = new AsyncLoaderDataForToday(act, guestProvaider, mCity);
                break;
            case ASYNC_LOADER_UPDATING_AND_ADDING_DATA:
                loader = new AsyncLoaderUpdatingAndAddingData(getActivity().getApplicationContext(), guestProvaider);
                break;
            case ASYNC_LOADER_UPDATING_AND_ADDING_DATA_CITY_NAME:
                loader = new AsyncLoaderDataAcquisition(act, guestProvaider);
        }
        return loader;
    }

    @SuppressLint("NewApi")
    @Override
    public void onLoadFinished(final android.support.v4.content.Loader<Cursor> loader, final Cursor data) {
        Log.i("Loader", "вернул лоадер " + loader.getId());
        if (data != null) {
            switch (loader.getId()) {
                case ASYNC_LOADER_OPENING_THE_APPLICATION:
                    addInAdapter(data);
                    getLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
                    break;
                case ASYNC_LOADER_DATA_FOR_TODAY:
                    Log.i("FOR_TODAY", "вышел из FOR_TODAY");
                    displayWeather(data);
                    break;
                case ASYNC_LOADER_DATA_ON_GEO:
                    addInAdapter(data);
                    getLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
                    break;
                case ASYNC_LOADER_UPDATING_AND_ADDING_DATA:
                    Log.i("Updating", "вышел из updating");
                    addInAdapter(data);
                    getLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
                    break;
                case ASYNC_LOADER_UPDATING_AND_ADDING_DATA_CITY_NAME:
                    addInAdapter(data);
                    getLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


    @Override
    public void onBackStackChanged() {
      int i =  getFragmentManager().getBackStackEntryCount();
        Log.i("BACKSTACK", " weatherFragment сработало" + i);
    }
}
