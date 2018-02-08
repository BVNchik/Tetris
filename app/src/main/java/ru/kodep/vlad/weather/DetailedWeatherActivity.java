package ru.kodep.vlad.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vlad on 08.02.18
 */

public class DetailedWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_weather);

        TextView tvcityName, tvdata, tvtemp, tvspeed, tvhumidity, tvpressure;
        tvcityName = findViewById(R.id.tvCityNameDW);
        tvdata = findViewById(R.id.tvDataDW);
        tvtemp = findViewById(R.id.tvTempDW);
        tvspeed = findViewById(R.id.tvSpeedDW);
        tvhumidity = findViewById(R.id.tvHummidityDW);
        tvpressure = findViewById(R.id.tvPressureDW);


        String name = "";
        Long datas;
        Double temp ;
        Double speed;
        Double humidity;
        Double pressure;

        ForeCast foreCast = (ForeCast) getIntent().getParcelableExtra("DetailedWeather");

        name = foreCast.getCityName();
        datas = foreCast.getDt();
        temp = foreCast.getTemp();
        humidity = foreCast.getHumidity();
        pressure = foreCast.getPressure();
        speed = foreCast.getSpeed();

        String data = new SimpleDateFormat("dd MMMM yyyy").format(new Date(datas * 1000));
        tvcityName.setText(name);
        tvdata.setText(data);
        tvtemp.setText("Температура: \n" + temp + "\u00b0C");
       tvspeed.setText("Ветер: \n" + speed + "м/с");
        tvhumidity.setText("Влажность: \n" + humidity + "%");
        tvpressure.setText("Давление: \n" + pressure + "hPa");
    }
}
