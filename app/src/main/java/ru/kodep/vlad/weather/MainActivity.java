package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GeoLocation.OnLocationChangedCallback {
    SearchCityTask mt;
    MyTaskWeather mtw;
    SearchTheNameCityTask stnct;
    MyTaskWeatherTheCity mtwtc;

    private List<ForeCast> foreCasts = new ArrayList<>();
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
    DBHelper dbHelper;
    GeoLocation geoLocation;

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
        dbHelper = new DBHelper(this);
        geoLocation = new GeoLocation(getSystemService(LocationManager.class), this);
        mCity = new CityPreference(MainActivity.this).getCity();
        monitorOutputDataBase();
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "cityname = ? AND data > ? AND data < ?";
        String citys = mCity;
        String[] selectionArgs = new String[]{citys, todayData, todayDatas};
        Cursor d = db.query("WeatherTable", null, selection, selectionArgs, null, null, null);
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
        d.close();
        dbHelper.close();
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.VISIBLE);
        viewBar.setVisibility(View.GONE);
        viewLoading.setVisibility(View.VISIBLE);
        viewLoadingError.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    private void monitorOutputDataBase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor h = db.query("WeatherTable", null, null, null, null, null, null);
        if (h.getCount() == 0) {
            viewError.setVisibility(View.VISIBLE);
            viewWeather.setVisibility(View.GONE);
            viewBar.setVisibility(View.GONE);
        } else {
            foreCasts.clear();
            String selection = "cityname = ?";
            String citys = mCity;
            String[] selectionArgs = new String[]{citys};
            Cursor d = db.query("WeatherTable", null, selection, selectionArgs, null, null, null);
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
            d.close();
            dbHelper.close();
            monitorOutputDataBaseToday();
            addInAdapter();
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

    private void errorVisible() {
        viewLoading.setVisibility(View.GONE);
        viewLoadingError.setVisibility(View.VISIBLE);
    }


    @SuppressLint("NewApi")
    private void addAndUpdate(Response response) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (int i = 0; i < 7; i++) {
            String name = null, datas = null;
            String citys = response.getCity().getName();
            String data = String.valueOf(response.getList().get(i).getDt());
            String selection = "cityname = ? AND data = ?";
            String[] selectionArgs = new String[]{citys, data};
            @SuppressLint("Recycle")
            Cursor h = db.query("WeatherTable", null, selection, selectionArgs, null, null, null);
            if (h.moveToFirst()) {
                int citynameColIndex = h.getColumnIndex("cityname");
                int dataColIndex = h.getColumnIndex("data");
                do {
                    name = h.getString(citynameColIndex);
                    datas = h.getString(dataColIndex);
                } while (h.moveToNext());
            }
            if (name == null | datas == null) {
                addDataBase(response, cv, i);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("WeatherTable", null, cv);

            } else if (Objects.equals(name, citys) & Objects.equals(datas, data)) {
                addDataBase(response, cv, i);
                // вставляем запись и получаем ее ID
                db.update("WeatherTable", cv, selection, selectionArgs);
            }
            h.close();
        }

        //добавление в шаблон вывода
        String selection = "cityname = ?";
        String city = response.getCity().getName();
        String[] selectionArgs = new String[]{city};
        Cursor d = db.query("WeatherTable", null, selection, selectionArgs, null, null, null);
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
        d.close();
        dbHelper.close();
    }

    private void addDataBase(Response response, ContentValues cv, int i) {
        cv.put("cityname", response.getCity().getName());
        cv.put("temps", response.getList().get(i).getTemp());
        cv.put("humidity", response.getList().get(i).getHumidity());
        cv.put("pressure", response.getList().get(i).getPressure());
        cv.put("speed", response.getList().get(i).getSpeed());
        cv.put("data", response.getList().get(i).getDt());
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
}
