package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vlad on 21.12.17
 */

class DataAdapter extends CursorRecyclerViewAdapter<DataAdapter.ViewHolder> {
    private Cursor cursor;

    DataAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        ForeCast foreCast = ForeCast.fromCursor(cursor);
        @SuppressLint("SimpleDateFormat") String data = new SimpleDateFormat("dd MMMM yyyy").format(new Date(foreCast.getDt() * 1000));
        viewHolder.data.setText(data);
        Double temp = foreCast.getTemp();
        viewHolder.temp.setText(String.format("%.1f", temp) + "\u00b0C");
        String speed = String.valueOf(foreCast.getSpeed());
        viewHolder.speed.setText("Ветер: \n" + speed + "м/с");
        String humidity = String.valueOf(foreCast.getHumidity());
        viewHolder.humidity.setText("Влажность: \n" + humidity + "%");
        String pressure = String.valueOf(foreCast.getPressure());
        viewHolder.pressure.setText("Давление: \n" + pressure + "hPa");
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView data, temp, speed, humidity, pressure;

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


