package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
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

class DataAdapter extends CursorRecyclerViewAdapter<DataAdapter.ViewHolder>{


    private final OnForeCastClickListener mOnForeCastClickListener;
    private Cursor cursor;

    DataAdapter(Context context, Cursor cursor, @NonNull OnForeCastClickListener onForeCastClickListener) {
        super(context, cursor);
        this.cursor = cursor;
        mOnForeCastClickListener = onForeCastClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView, mOnForeCastClickListener);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        ForeCast foreCast = ForeCast.fromCursor(cursor);
        viewHolder.bind(foreCast);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView data, temp, speed, humidity, pressure;
        ForeCast mForeCast;


        ViewHolder(View view, final OnForeCastClickListener onForeCastClickListener) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onForeCastClickListener.onForeCastClick(mForeCast);
                }
            });

            data = view.findViewById(R.id.tvData);
            temp = view.findViewById(R.id.tvTemp);
            speed = view.findViewById(R.id.tvSpeed);
            humidity = view.findViewById(R.id.tvHumiditys);
            pressure = view.findViewById(R.id.tvPressures);
        }

        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        void bind(ForeCast foreCast) {

            mForeCast = foreCast;
            @SuppressLint("SimpleDateFormat") String datas = new SimpleDateFormat("dd MMMM yyyy").format(new Date(foreCast.getDt() * 1000));
            data.setText(datas);
            Double temps = foreCast.getTemp();
            temp.setText(String.format("%.1f", temps) + "\u00b0C");
            String speeds = String.valueOf(foreCast.getSpeed());
            speed.setText("Ветер: \n" + speeds + "м/с");
            String humiditys = String.valueOf(foreCast.getHumidity());
            humidity.setText("Влажность: \n" + humiditys + "%");
            String pressures = String.valueOf(foreCast.getPressure());
            pressure.setText("Давление: \n" + pressures + "hPa");
        }

    }
    interface OnForeCastClickListener {
        void onForeCastClick(ForeCast foreCast);
    }
}


