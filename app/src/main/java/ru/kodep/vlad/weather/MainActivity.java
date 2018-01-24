package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ru.kodep.vlad.weather.entity.City;
import ru.kodep.vlad.weather.entity.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GeoLocation.OnLocationChangedCallback, LoaderManager.LoaderCallbacks {
    static final int LOADER_TIME_ID = 1;
    final Uri WEATHER_URI = Uri.parse("content://ru.kodep.vlad.weather.WeatherData/WeatherTable");
    SearchCityTask mt;
    MyTaskWeather mtw;
    SearchTheNameCityTask stnct;
    MyTaskWeatherTheCity mtwtc;
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
    String mCity, id;
    GeoLocation geoLocation;
    private List<ForeCast> foreCasts = new ArrayList<>();

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
        geoLocation = new GeoLocation(getSystemService(LocationManager.class), this);
        mCity = new CityPreference(MainActivity.this).getCity();
        monitorOutputDataBase();
        Cursor cursor = getContentResolver().query(WEATHER_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        getLoaderManager().initLoader(LOADER_TIME_ID, null, this);
        Log.i("Async", "onCreate is main");
        AsyncLoader loader = null;
        loader = (AsyncLoader) getLoaderManager().getLoader(LOADER_TIME_ID);
        loader.forceLoad();
    }


    @SuppressLint("NewApi")
    private void addInAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvListForeCast);
        adapter = new DataAdapter(foreCasts);
        recyclerView.setAdapter(adapter);
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
                String city = input.getText().toString();
                updateWeatherDataNameCity(city);
            }
        });
        chooseCity.show();
    }

    private void updateWeatherData() {
        mt = new SearchCityTask();
        mt.execute();
    }

    private void updateWeatherDataNameCity(final String city) {
        mCity = city;
        stnct = new SearchTheNameCityTask();
        stnct.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                updateWeatherData();
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
        updateWeatherData();
    }

    @SuppressLint({"WrongConstant", "DefaultLocale", "SetTextI18n"})
    private void monitorOutputDataBaseToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        String todayData = String.valueOf((cal.getTimeInMillis() / 1000 - 43199));
        String todayDatas = String.valueOf((cal.getTimeInMillis() / 1000 + 43199));
        Log.i("dasd", todayData + " - " + todayDatas);
        String selection = "cityname = ? AND data > ? AND data < ?";
        String citys = mCity;
        String[] selectionArgs = new String[]{citys, todayData, todayDatas};

        @SuppressLint("Recycle") Cursor d = getContentResolver().query(WEATHER_URI, null, selection, selectionArgs, null);
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
        viewLoading.setVisibility(View.VISIBLE);
        viewLoadingError.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    private void monitorOutputDataBase() {
        @SuppressLint("Recycle") Cursor h = getContentResolver().query(WEATHER_URI, null, null, null, null);
        if (h != null) {
            if (h.getCount() == 0) {
                viewError.setVisibility(View.VISIBLE);
                viewWeather.setVisibility(View.GONE);
                viewBar.setVisibility(View.GONE);
            } else {
                foreCasts.clear();
                String selection = "cityname = ?";
                String citys = mCity;
                String[] selectionArgs = new String[]{citys};
                @SuppressLint("Recycle") Cursor d = getContentResolver().query(WEATHER_URI, null, selection, selectionArgs, null);
                if (d.moveToFirst()) {
                    int tempColIndex = d.getColumnIndex("temps");
                    int humidityColIndex = d.getColumnIndex("humidity");
                    int pressureColIndex = d.getColumnIndex("pressure");
                    int dataColIndex = d.getColumnIndex("data");
                    int speedColIndex = d.getColumnIndex("speed");
                    do {
                        foreCasts.add(new ForeCast(d.getLong(dataColIndex), d.getDouble(tempColIndex), d.getDouble(speedColIndex), d.getDouble(humidityColIndex), d.getDouble(pressureColIndex)));
                    } while (d.moveToNext());
                }

                monitorOutputDataBaseToday();
                addInAdapter();
            }
        }
    }

    private void renderGeo() {
        mtw = new MyTaskWeather();
        mtw.execute();
        foreCasts.clear();
    }

    private void renderGeoWeek(City result) {
        id = result.getId();
        mtwtc = new MyTaskWeatherTheCity();
        mtwtc.execute();
        foreCasts.clear();
    }

    private void errorVisible() {
        viewLoading.setVisibility(View.GONE);
        viewLoadingError.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi")
    private void addAndUpdate(Response response) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 7; i++) {
            String name = null, datas = null;
            String citys = response.getCity().getName();
            String data = String.valueOf(response.getList().get(i).getDt());
            String selection = "cityname = ? AND data = ?";
            String[] selectionArgs = new String[]{citys, data};
            @SuppressLint("Recycle")
            Cursor h = getContentResolver().query(WEATHER_URI, null, selection, selectionArgs, null);
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
                Uri newUri = getContentResolver().insert(WEATHER_URI, cv);
                assert newUri != null;
                Log.i("MAIN", "insert, result Uri : " + newUri.toString());
            } else if (Objects.equals(name, citys) & Objects.equals(datas, data)) {

                cv.put("cityname", response.getCity().getName());
                cv.put("temps", response.getList().get(i).getTemp());
                cv.put("humidity", response.getList().get(i).getHumidity());
                cv.put("pressure", response.getList().get(i).getPressure());
                cv.put("speed", response.getList().get(i).getSpeed());
                cv.put("data", response.getList().get(i).getDt());
                int cnt = getContentResolver().update(WEATHER_URI, cv, selection, selectionArgs);
                Log.i("MAIN", "update, count = " + cnt);

            }

        }
        //добавление в шаблон вывода
        String selection = "cityname = ?";
        String city = response.getCity().getName();
        String[] selectionArgs = new String[]{city};
        @SuppressLint("Recycle") Cursor d = getContentResolver().query(WEATHER_URI, null, selection, selectionArgs, null);
        if (d.moveToFirst()) {
            int tempColIndex = d.getColumnIndex("temps");
            int humidityColIndex = d.getColumnIndex("humidity");
            int pressureColIndex = d.getColumnIndex("pressure");
            int dataColIndex = d.getColumnIndex("data");
            int speedColIndex = d.getColumnIndex("speed");
            do {
                foreCasts.add(new ForeCast(d.getLong(dataColIndex), d.getDouble(tempColIndex), d.getDouble(speedColIndex), d.getDouble(humidityColIndex), d.getDouble(pressureColIndex)));
            } while (d.moveToNext());
        }
    }


    //Обработка загруженных данных
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void renderWeather(Response json) {
        try {
            city.setText(json.getCity().getName());
            humidity.setText("Влажность: " + String.valueOf(json.getList().get(0).getHumidity()) + "%");
            temp.setText(String.format("%.1f", json.getList().get(0).getTemp()) + "\u00b0C");
            pressure.setText("Давление: " + String.valueOf(json.getList().get(0).getPressure()) + "hPa");
            Log.i("DHB", "sdfsd");
        } catch (Exception e) {
            Log.e("Main", "One or more fields not found in the JSON data");
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        AsyncLoader loader = null;
        if (id == LOADER_TIME_ID) {
        loader = new AsyncLoader(this, null);
        Log.i("Async", "запустилась из активити " + loader.hashCode());
    }
    return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Log.i("Async", "finish из активити " + loader.hashCode());
        Log.i("Async", "finish вернул " + data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.i("Async", "restart из активити " + loader.hashCode());

    }

    @SuppressLint("StaticFieldLeak")
    class SearchCityTask extends AsyncTask<Void, Void, City> {

        @Override
        protected City doInBackground(Void... params) {
            final City city = WeatherData.getJSONDataDayGeo(MainActivity.this, lat, lon);
            if (city == null) {
                handler.post(new Runnable() {
                    public void run() {
                        errorVisible();
                    }
                });
            } else {
                new CityPreference(MainActivity.this).setCity(city.getName());
            }
            return city;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(City result) {
            super.onPostExecute(result);
            if (result != null) {
                renderGeo();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SearchTheNameCityTask extends AsyncTask<Void, Void, City> {

        @Override
        protected City doInBackground(Void... params) {
            final City city = WeatherData.getJSONDataDayNameCity(MainActivity.this, mCity);
            if (city == null) {
                handler.post(new Runnable() {
                    public void run() {
                        errorVisible();
                    }
                });
            } else {
                new CityPreference(MainActivity.this).setCity(city.getName());
            }
            return city;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(City result) {
            super.onPostExecute(result);
            if (result != null) {
                renderGeoWeek(result);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class MyTaskWeather extends AsyncTask<Void, Void, Response> {

        @SuppressLint({"SetTextI18n", "WrongConstant"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Response doInBackground(Void... params) {
            final Response response = WeatherData.getJSONDataWeekGeo(MainActivity.this, lat, lon);

            if (response == null) {
                handler.post(new Runnable() {
                    public void run() {
                        errorVisible();
                    }
                });
            } else {
                addAndUpdate(response);
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            if (result != null) {
                renderWeather(result);
                addInAdapter();
                viewError.setVisibility(View.GONE);
                viewWeather.setVisibility(View.VISIBLE);
                viewBar.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewLoadingError.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class MyTaskWeatherTheCity extends AsyncTask<Void, Void, Response> {

        @SuppressLint({"SetTextI18n", "WrongConstant"})
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Response doInBackground(Void... params) {
            final Response response = WeatherData.getJSONDataWeekId(MainActivity.this, id);

            if (response == null) {
                handler.post(new Runnable() {
                    public void run() {
                        errorVisible();
                    }
                });
            } else {
                addAndUpdate(response);
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            if (result != null) {
                renderWeather(result);
                addInAdapter();
                viewError.setVisibility(View.GONE);
                viewWeather.setVisibility(View.VISIBLE);
                viewBar.setVisibility(View.GONE);
                viewLoading.setVisibility(View.GONE);
                viewLoadingError.setVisibility(View.GONE);
            }
        }
    }
}
