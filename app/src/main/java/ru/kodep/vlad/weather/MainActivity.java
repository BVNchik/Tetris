package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.kodep.vlad.weather.entity.GuestProvaider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GeoLocation.OnLocationChangedCallback, LoaderCallbacks<Cursor> {
    static final int ASYNC_LOADER_DATA_ACQUISITION = 0;
    static final int ASYNC_LOADER_DATA_FOR_TODAY = 1;
    static final int ASYNC_LOADER_DATA_ON_THE_CITY = 2;
    static final int ASYNC_LOADER_UPDATING_AND_ADDING_DATA = 3;
    static final int ASYNC_LOADER_DATA_ON_GEO = 10;
    int loaderDataForToday, loaderDataOnTheCity, loaderUpdatingAndAddingData, loaderDataAcquisition, loaderFirstStart;
    Handler handler;
    DataAdapter adapter;
    TextView city;
    TextView temp;
    TextView humidity;
    TextView pressure;
    RelativeLayout viewBar;
    ConstraintLayout viewError;
    LinearLayout viewWeather;
    ProgressBar viewLoading;
    TextView viewLoadingError;
    String lat, lon;
    String mCity;
    GeoLocation geoLocation;
    GuestProvaider guestProvaider;
    Cursor cursor;

    @SuppressLint({"CommitTransaction", "WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.btnRefresh).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
        viewBar = findViewById(R.id.viewBar);
        viewError = findViewById(R.id.viewError);
        viewWeather = findViewById(R.id.viewWeather);
        viewLoading = findViewById(R.id.pbLoading);
        viewLoadingError = findViewById(R.id.tvLoadingError);
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.GONE);
        viewBar.setVisibility(View.VISIBLE);
        viewLoading.setVisibility(View.GONE);
        viewLoadingError.setVisibility(View.GONE);
        city = findViewById(R.id.tvCity);
        temp = findViewById(R.id.tvTemp);
        humidity = findViewById(R.id.tvHumidity);
        pressure = findViewById(R.id.tvPressure);
        handler = new Handler();
        guestProvaider = new GuestProvaider(this);
        guestProvaider.open();
        getSupportLoaderManager().initLoader(ASYNC_LOADER_DATA_ACQUISITION, null, this);
    }


    @SuppressLint("NewApi")
    private void addInAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvListForeCast);
        adapter = new DataAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.VISIBLE);
        viewBar.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);
        viewLoadingError.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //обработка нажатия пункта меню
    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) showInputDialog();
        if (item.getItemId() == R.id.action_updater) {
            viewLoading.setVisibility(View.VISIBLE);
            viewLoadingError.setVisibility(View.GONE);
            new GeoLocation(getSystemService(LocationManager.class), this);
        }
        return true;
    }

    private void showInputDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
        chooseCity.setIcon(R.mipmap.ic_launcher);
        chooseCity.setTitle(R.string.choose_city);
        final EditText input = new EditText(this);
        input.setHint("Moscow");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        chooseCity.setView(input);
        chooseCity.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new CityPreference(MainActivity.this).setCity(input.getText().toString());
                getSupportLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, MainActivity.this);
                viewLoading.setVisibility(View.VISIBLE);
            }
        });
        chooseCity.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                viewError.setVisibility(View.GONE);
                viewWeather.setVisibility(View.GONE);
                viewBar.setVisibility(View.VISIBLE);
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }

    @Override
    public void onLocationChanged(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        if (new CityPreference(MainActivity.this).getStart() == 0) {
            getSupportLoaderManager().initLoader(ASYNC_LOADER_DATA_ON_GEO, null, this);
        } else {
            getSupportLoaderManager().restartLoader(ASYNC_LOADER_DATA_ON_GEO, null, this);
        }
    }


    @SuppressLint({"WrongConstant", "SetTextI18n", "DefaultLocale"})
    private void monitorOutputDataBase() {

        Cursor d = cursor;
        if (d.moveToFirst()) {
            int citynameColIndex = d.getColumnIndex("cityname");
            int tempColIndex = d.getColumnIndex("temps");
            int humidityColIndex = d.getColumnIndex("humidity");
            int pressureColIndex = d.getColumnIndex("pressure");

            city.setText(d.getString(citynameColIndex));
            humidity.setText("Влажность: " + String.valueOf(d.getString(humidityColIndex)) + "%");
            temp.setText(String.format("%.1f", d.getDouble(tempColIndex)) + "\u00b0C");
            pressure.setText("Давление: " + String.valueOf(d.getString(pressureColIndex)) + "hPa");
        }
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.VISIBLE);
        viewBar.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);
        viewLoadingError.setVisibility(View.GONE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        Log.i("Async", "пришел id = " + id);
        mCity = new CityPreference(MainActivity.this).getCity();
        switch (id) {
            case ASYNC_LOADER_DATA_ACQUISITION:
                loader = new AsyncLoaderDataAcquisition(this, guestProvaider);
                loaderDataAcquisition = loader.hashCode();
                Log.i("Async", "запущени лоадер 0  " + loaderDataAcquisition);
                break;
            case ASYNC_LOADER_DATA_ON_GEO:
                loader = new AsyncLoaderDataOnGeo(this, guestProvaider, lat, lon);
                loaderFirstStart = loader.hashCode();
                Log.i("Async", "запущени лоадер 10  " + loaderFirstStart);
                break;
            case ASYNC_LOADER_DATA_FOR_TODAY:
                loader = new AsyncLoaderDataForToday(this, guestProvaider, mCity);
                loaderDataForToday = loader.hashCode();
                Log.i("Async", "запущени лоадер 1  " + loaderDataForToday);
                break;
            case ASYNC_LOADER_DATA_ON_THE_CITY:
                loader = new AsyncLoaderDataOnTheCity(this, guestProvaider, mCity);
                loaderDataOnTheCity = loader.hashCode();
                Log.i("Async", "запущени лоадер 2  " + loaderDataOnTheCity);
                break;
            case ASYNC_LOADER_UPDATING_AND_ADDING_DATA:
                loader = new AsyncLoaderUpdatingAndAddingData(this, guestProvaider, mCity);
                loaderUpdatingAndAddingData = loader.hashCode();
                Log.i("Async", "запущени лоадер 3  " + loaderUpdatingAndAddingData);
                break;
        }
        return loader;
    }

    @SuppressLint("NewApi")
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.i("Async", "вернула в активити  " + loader.hashCode());
        cursor = data;
        int hashcode = loader.hashCode();
        if (hashcode == loaderDataAcquisition) {
            if (cursor.getCount() == 0) {
                new CityPreference(MainActivity.this).setStart((1));
                geoLocation = new GeoLocation(getSystemService(LocationManager.class), this);
            } else {
                mCity = new CityPreference(MainActivity.this).getCity();
                getSupportLoaderManager().initLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
            }

        } else if (hashcode == loaderDataForToday) {
            monitorOutputDataBase();

        } else if (hashcode == loaderFirstStart) {
            if (new CityPreference(MainActivity.this).getStart() == 0) {
                getSupportLoaderManager().initLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
            } else {
                getSupportLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
            }

        } else if (hashcode == loaderDataOnTheCity) {
            addInAdapter();
        } else if (hashcode == loaderUpdatingAndAddingData) {
            addInAdapter();
            getSupportLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
        }
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

}

