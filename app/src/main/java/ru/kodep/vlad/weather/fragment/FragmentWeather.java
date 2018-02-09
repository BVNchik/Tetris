package ru.kodep.vlad.weather.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.kodep.vlad.weather.ForeCast;
import ru.kodep.vlad.weather.R;

/**
 * Created by vlad on 08.02.18
 */

@SuppressLint("ValidFragment")
public  class FragmentWeather extends Fragment {
    View view;
    ForeCast foreCast;

    @SuppressLint("ValidFragment")
    public  FragmentWeather(ForeCast foreCast) {
        this.foreCast = foreCast;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint({"ResourceType", "InflateParams"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Fragment", "Fragment2 onCreateView");
        view = inflater.inflate(R.layout.detailed_weather, null);
        weatherOutputInAFragment();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void weatherOutputInAFragment() {

        TextView tvcityName, tvdata, tvtemp, tvspeed, tvhumidity, tvpressure;
        tvcityName = view.findViewById(R.id.tvCityNameDW);
        tvdata = view.findViewById(R.id.tvDataDW);
        tvtemp = view.findViewById(R.id.tvTempDW);
        tvspeed = view.findViewById(R.id.tvSpeedDW);
        tvhumidity = view.findViewById(R.id.tvHummidityDW);
        tvpressure = view.findViewById(R.id.tvPressureDW);


        String name;
        Long datas;
        Double temp;
        Double speed;
        Double humidity;
        Double pressure;


        name = foreCast.getCityName();
        datas = foreCast.getDt();
        temp = foreCast.getTemp();
        humidity = foreCast.getHumidity();
        pressure = foreCast.getPressure();
        speed = foreCast.getSpeed();

        @SuppressLint("SimpleDateFormat") String data = new SimpleDateFormat("dd MMMM yyyy").format(new Date(datas * 1000));
        tvcityName.setText(name);
        tvdata.setText(data);
        tvtemp.setText("Температура: \n" + temp + "\u00b0C");
        tvspeed.setText("Ветер: \n" + speed + "м/с");
        tvhumidity.setText("Влажность: \n" + humidity + "%");
        tvpressure.setText("Давление: \n" + pressure + "hPa");
    }

}
