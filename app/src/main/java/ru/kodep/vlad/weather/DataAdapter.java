package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.kodep.vlad.weather.entity.Response;

/**
 * Created by vlad on 21.12.17
 */

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Response> records;

    DataAdapter(Context context, List<Response> records) {
        this.records = records;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout., parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        AlphabeticIndex.Record record = records.get(position);
        String score = String.valueOf(record.getScore());
        holder.scoreView.setText(score);
        holder.nameView.setText(record.getName());
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("mm:ss").format(new Date(record.getTime()));
        holder.timeView.setText(time);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, scoreView, timeView;

        ViewHolder(View view) {
            super(view);
            scoreView =  view.findViewById(R.id.tvScores);
            nameView =  view.findViewById(R.id.tvName);
            timeView =  view.findViewById(R.id.tvTime);
        }
    }
}