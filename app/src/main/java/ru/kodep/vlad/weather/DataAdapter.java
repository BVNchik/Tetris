package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by vlad on 21.12.17
 */

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {


    private List<ForeCast> forecasts;

    DataAdapter(List<ForeCast> forecasts) {
        this.forecasts = forecasts;
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        ForeCast foreCast = forecasts.get(position);
        @SuppressLint("SimpleDateFormat")
        String data = new SimpleDateFormat("dd MMMM yyyy").format(new Date(foreCast.getDt() * 1000));
        holder.data.setText(data);
       Double temp = foreCast.getTemp();
        holder.temp.setText(String.format("%.1f", temp) + "\u00b0C");
        String speed = String.valueOf(foreCast.getSpeed());
        holder.speed.setText("Ветер: \n" + speed + "м/с");
        String humidity = String.valueOf(foreCast.getHumidity());
        holder.humidity.setText("Влажность: \n" + humidity + "%");
        String pressure = String.valueOf(foreCast.getPressure());
        holder.pressure.setText("Давление: \n" + pressure + "hPa");

    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView data, temp, speed, humidity, pressure;

        ViewHolder(View view) {
            super(view);
            data = view.findViewById(R.id.tvData);
            temp = view.findViewById(R.id.tvTemp);
            speed = view.findViewById(R.id.tvSpeed);
            humidity = view.findViewById(R.id.tvHumiditys);
            pressure = view.findViewById(R.id.tvPressures);
        }
    }
}