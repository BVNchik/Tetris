package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.kodep.vlad.weather.entity.City;
import ru.kodep.vlad.weather.entity.Response;

public class MainActivity extends AppCompatActivity {
    SearchCityTask mt;
    MyTaskWeather mtw;
    private List<ForeCast> foreCasts;
    Handler handler;
    TextView city;
    TextView gradus;
    TextView humidity;
    TextView pressure;
    String mCity;
    int id;

    public MainActivity() {
    }

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.tvCity);
        gradus = findViewById(R.id.tvGradus);
        humidity = findViewById(R.id.tvHumidity);
        pressure = findViewById(R.id.tvPressure);
        handler = new Handler();
        updateWeatherData(new CityPreference(MainActivity.this).getCity());
    }

    //    private void addInAdapter() {
//        RecyclerView recyclerView =  findViewById(R.id.rvListForeCast);
//        adapter = new DataAdapter(this, records);
//        recyclerView.setAdapter(adapter);
//    }
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

    @SuppressLint("StaticFieldLeak")
    class SearchCityTask extends AsyncTask<Void, Void, City> {


        @Override
        protected City doInBackground(Void... params) {
            final City city = WeatherData.getJSONData(MainActivity.this, mCity);

            if (city == null) {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.place_not_found),
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    public void run() {
                        renderWeather(city);
                    }
                });
            }
            return city;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(City result) {
            super.onPostExecute(result);
            renderWeather(result);
            renderId(result);

        }
    }

    private void renderId(City result) {
        id = result.getId();
        mtw = new MyTaskWeather();
        mtw.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class MyTaskWeather extends AsyncTask<Void, Void, Response> {


        @Override
        protected Response doInBackground(Void... params) {
            final Response response = WeatherDataForAWeek.getJSONDataForAWeek(MainActivity.this, id);


            if (response == null) {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.place_not_found),
                                Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    public void run() {
                        renderWeatherWeek(response);
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
//            List<DayForecast> list = response.getList();
//            for (DayForecast dayForecast : list) {
//                dayForecast.getClouds();
//            }
            gradus.setText(response.getList().get(1).getClouds() + "cnt:" + response.getCnt() + "cod:"+ response.getCod());


        } catch (Exception e) {
            Log.e("Weather", "One or more fields not found in the JSON data");
        }
    }

    //Обработка загруженных данных
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void renderWeather(City json) {
        try {

            city.setText(String.valueOf(json.getId()));

//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            humidity.setText(getResources().getString(R.string.humidity)
//                    + ": " + main.getString("humidity") + "%");
//            pressure.setText(getResources().getString(R.string.pressure) + ": " + main.getString("pressure") + " hPa");
//            gradus.setText(String.format("%.1f", main.getDouble("temp")));
//
//
//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
//            data.setText(getResources().getString(R.string.last_update) + " " + updatedOn);

//            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000,
//                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.e("Weather", "One or more fields not found in the JSON data");
        }
    }

    //Подстановка нужной иконки
//    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
//        int id = actualId / 100;
//        String icon = "";
//        if (actualId == 800) {
//            long currentTime = new Date().getTime();
//            if (currentTime >= sunrise && currentTime < sunset) {
//                icon = MainActivity.this.getString(R.string.weather_sunny);
//            } else {
//                icon = MainActivity.this.getString(R.string.weather_clear_night);
//            }
//        } else {
//            Log.d("SimpleWeather", "id " + id);
//            switch (id) {
//                case 2:
//                    icon = MainActivity.this.getString(R.string.weather_thunder);
//                    break;
//                case 3:
//                    icon = MainActivity.this.getString(R.string.weather_drizzle);
//                    break;
//                case 5:
//                    icon = MainActivity.this.getString(R.string.weather_rainy);
//                    break;
//                case 6:
//                    icon = MainActivity.this.getString(R.string.weather_snowy);
//                    break;
//                case 7:
//                    icon = MainActivity.this.getString(R.string.weather_foggy);
//                    break;
//                case 8:
//                    icon = MainActivity.this.getString(R.string.weather_cloudy);
//                    break;
//            }
//        }
//        sky.setText(icon);
//    }


}
