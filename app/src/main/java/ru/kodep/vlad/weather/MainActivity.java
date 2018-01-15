package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.kodep.vlad.weather.entity.City;
import ru.kodep.vlad.weather.entity.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    SearchCityTask mt;
    MyTaskWeather mtw;
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
    String mCity;
    String id;


    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnRefresh).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
        viewBar = findViewById(R.id.viewBar);
        viewError = findViewById(R.id.viewError);
        viewWeather = findViewById(R.id.viewWeather);
        city = findViewById(R.id.tvCity);
        temp = findViewById(R.id.tvTemp);
        humidity = findViewById(R.id.tvHumidity);
        pressure = findViewById(R.id.tvPressure);
        handler = new Handler();
        updateWeatherData(new CityPreference(MainActivity.this).getCity());
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.GONE);
        viewBar.setVisibility(View.VISIBLE);
    }

    private void addInAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rvListForeCast);
        adapter = new DataAdapter(foreCasts);
        recyclerView.setAdapter(adapter);
        viewError.setVisibility(View.GONE);
        viewWeather.setVisibility(View.VISIBLE);
        viewBar.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //обработка нажатия пункта меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) showInputDialog();
        return true;
    }


    private void showInputDialog() {
        AlertDialog.Builder chooseCity = new AlertDialog.Builder(this);
        chooseCity.setIcon(R.mipmap.ic_launcher);
        chooseCity.setTitle(R.string.choose_city);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        chooseCity.setView(input);
        chooseCity.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();
                updateWeatherData(city);
                new CityPreference(MainActivity.this).setCity(city);
            }
        });
        chooseCity.show();
    }

    //Обновление/загрузка погодных данных
    private void updateWeatherData(final String city) {
        mCity = city;
        mt = new SearchCityTask();
        mt.execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                updateWeatherData(new CityPreference(MainActivity.this).getCity());
                viewError.setVisibility(View.GONE);
                viewWeather.setVisibility(View.GONE);
                viewBar.setVisibility(View.VISIBLE);
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SearchCityTask extends AsyncTask<Void, Void, City> {

        @Override
        protected City doInBackground(Void... params) {
            final City city = WeatherData.getJSONData(MainActivity.this, mCity);

            if (city == null) {
                handler.post(new Runnable() {
                    public void run() {
                        viewError.setVisibility(View.VISIBLE);
                        viewWeather.setVisibility(View.GONE);
                        viewBar.setVisibility(View.GONE);
                    }
                });
            }
            return city;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(City result) {
            super.onPostExecute(result);
            if (result == null) {

            } else {
                renderWeather(result);
                renderId(result);

            }
        }
    }

    private void renderId(City result) {
        if (result.getId() == null) {
            id = new CityPreference(MainActivity.this).getId();
        } else {
            id = result.getId();
        }
        mtw = new MyTaskWeather();
        mtw.execute();
        foreCasts.clear();
    }


    @SuppressLint("StaticFieldLeak")
    class MyTaskWeather extends AsyncTask<Void, Void, Response> {

        @Override
        protected Response doInBackground(Void... params) {
            final Response response = WeatherDataForAWeek.getJSONDataForAWeek(MainActivity.this, String.valueOf(id));


            if (response == null) {
                handler.post(new Runnable() {
                    public void run() {
                        viewError.setVisibility(View.VISIBLE);
                        viewWeather.setVisibility(View.GONE);
                        viewBar.setVisibility(View.GONE);
                    }
                });
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);
            renderWeatherWeek(result);

        }
    }

    @SuppressLint("SetTextI18n")
    private void renderWeatherWeek(Response response) {
        try {
            for (int i = 1; i < 7; i++) {
                foreCasts.add(new ForeCast(response.getList().get(i).getDt(), response.getList().get(i).getTemp(), response.getList().get(i).getSpeed(), response.getList().get(i).getHumidity(), response.getList().get(i).getPressure()));
            }
            addInAdapter();

        } catch (Exception e) {
            Log.e("Main", "One or more fields not found in the JSON data");
        }

    }

    //Обработка загруженных данных
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void renderWeather(City json) {
        try {

            city.setText(json.getName());
            humidity.setText("Влажность: "+String.valueOf(json.getHumidity())+"%");
            temp.setText( String.format("%.1f",json.getTemp())+"\u00b0C");
            pressure.setText("Давление: "+String.valueOf(json.getPressure())+"hPa");

        } catch (Exception e) {
            Log.e("Main", "One or more fields not found in the JSON data");
        }
    }
}
