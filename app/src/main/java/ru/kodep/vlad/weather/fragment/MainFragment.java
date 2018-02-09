package ru.kodep.vlad.weather.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.kodep.vlad.weather.DataAdapter;
import ru.kodep.vlad.weather.ForeCast;
import ru.kodep.vlad.weather.R;

/**
 * Created by vlad on 09.02.18
 */

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements DataAdapter.OnForeCastClickListener {
    TextView city;
    TextView temp;
    TextView humidity;
    TextView pressure;
    View view;

    Cursor cursor;
    DataAdapter adapter;
    String[] weatherData;
    FragmentWeather fragmentWeather;
    FragmentTransaction fTrans;

    @SuppressLint("ValidFragment")
    public MainFragment(Cursor cursor, String[] weatherData) {
        this.cursor = cursor;
        this.weatherData = weatherData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, null);
        displayWeather();
        addInAdapter(cursor);
        Log.i("Fragment", "otobrazilas");
        return view;

    }

    @SuppressLint("NewApi")
    private void addInAdapter(@NonNull Cursor cursor) {
        RecyclerView recyclerView = view.findViewById(R.id.rvListForeCast);
        adapter = new DataAdapter(this, cursor, this);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void displayWeather() {
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
    public void onForeCastClick(ForeCast foreCast) {
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
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainFragment, fragmentWeather);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }
}
