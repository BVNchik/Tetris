package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Objects;

import ru.kodep.vlad.weather.entity.GuestProvaider;
import ru.kodep.vlad.weather.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements GeoLocation.OnLocationChangedCallback, LoaderCallbacks<Cursor> {
    static final int ASYNC_LOADER_DATA_ACQUISITION = 0;
    static final int ASYNC_LOADER_DATA_FOR_TODAY = 1;
    static final int ASYNC_LOADER_UPDATING_AND_ADDING_DATA = 3;
    static final int ASYNC_LOADER_DATA_ON_GEO = 10;
    Handler handler;
    String lat, lon;
    String mCity;
    GuestProvaider guestProvaider;
    GeoLocation geo;
    RelativeLayout viewBar;
    int onGeo;
    String[] weatherData;
    MainFragment mainFragment;
    FragmentTransaction fTrans;
    ProgressBar progressBar;


    @SuppressLint({"CommitTransaction", "WrongViewCast", "NewApi", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        guestProvaider = new GuestProvaider(this);
        guestProvaider.open();
        getSupportLoaderManager().initLoader(ASYNC_LOADER_DATA_ACQUISITION, null, this);
        getSupportLoaderManager().initLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
        viewBar = findViewById(R.id.viewBar);
        progressBar = findViewById(R.id.pbLoading);
        viewBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }


    @SuppressLint("NewApi")
    private void addInAdapter(@NonNull Cursor cursor) {
        mainFragment = new MainFragment(cursor, weatherData);
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainFragment, mainFragment);
        fTrans.commit();
        viewBar.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) showInputDialog();
        if (item.getItemId() == R.id.action_updater) {
            geo = new GeoLocation(getSystemService(LocationManager.class), this);
            onGeo = 1;
        }
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onStop() {
        super.onStop();
        if (geo != null)
            geo.unsubscribe();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("onGeo", onGeo);


    }

    @SuppressLint("NewApi")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onGeo = savedInstanceState.getInt("onGeo");
        if (onGeo == 1) {
            geo = new GeoLocation(getSystemService(LocationManager.class), this);
        }
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
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!Objects.equals(input.getText().toString(), "")) {
                    new CityPreference(MainActivity.this).setCity(input.getText().toString());
                    getSupportLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, MainActivity.this);
//                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        chooseCity.show();
    }

    private void showGeoDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
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
                finish();
            }
        });
        chooseCity.show();
    }


    @Override
    public void onLocationChanged(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        onGeo = 0;
        getSupportLoaderManager().restartLoader(ASYNC_LOADER_DATA_ON_GEO, null, this);
    }

    @Override
    public void geoLocationSetting() {
        showGeoDialog();
    }


    @SuppressLint({"WrongConstant", "SetTextI18n", "DefaultLocale"})
    private void translateWeatherDataIntoAString(@NonNull Cursor cursor) {
        if (cursor.moveToFirst()) {
            int citynameColIndex = cursor.getColumnIndex("cityname");
            int tempColIndex = cursor.getColumnIndex("temps");
            int humidityColIndex = cursor.getColumnIndex("humidity");
            int pressureColIndex = cursor.getColumnIndex("pressure");

            weatherData = new String[4];
            weatherData[0] = cursor.getString(citynameColIndex);
            weatherData[1] = cursor.getString(humidityColIndex);
            weatherData[2] = cursor.getString(tempColIndex);
            weatherData[3] = cursor.getString(pressureColIndex);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        switch (id) {
            case ASYNC_LOADER_DATA_ACQUISITION:
                loader = new AsyncLoaderDataAcquisition(this, guestProvaider);
                break;
            case ASYNC_LOADER_DATA_ON_GEO:
                loader = new AsyncLoaderDataOnGeo(this, guestProvaider, lat, lon);
                break;
            case ASYNC_LOADER_DATA_FOR_TODAY:
                loader = new AsyncLoaderDataForToday(this, guestProvaider);
                break;
            case ASYNC_LOADER_UPDATING_AND_ADDING_DATA:
                loader = new AsyncLoaderUpdatingAndAddingData(this, guestProvaider);
                break;
        }
        return loader;
    }

    @SuppressLint("NewApi")
    @Override
    public void onLoadFinished(final android.support.v4.content.Loader<Cursor> loader, final Cursor data) {
        if (data != null) {
            switch (loader.getId()) {
                case ASYNC_LOADER_DATA_ACQUISITION:
                    mCity = new CityPreference(MainActivity.this).getCity();
                    getSupportLoaderManager().initLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
                    break;
                case ASYNC_LOADER_DATA_FOR_TODAY:
                    translateWeatherDataIntoAString(data);
                    break;
                case ASYNC_LOADER_DATA_ON_GEO:
                    getSupportLoaderManager().restartLoader(ASYNC_LOADER_UPDATING_AND_ADDING_DATA, null, this);
                    break;
                case ASYNC_LOADER_UPDATING_AND_ADDING_DATA:
                    addInAdapter(data);
                    getSupportLoaderManager().restartLoader(ASYNC_LOADER_DATA_FOR_TODAY, null, this);
                    break;
            }
        }
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}

