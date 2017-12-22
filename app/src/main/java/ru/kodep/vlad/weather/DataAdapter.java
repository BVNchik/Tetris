package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private List<Response> responses;

    DataAdapter(Context context, List<Response> responses) {
        this.responses = responses;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Response response = responses.get(position);
        double degre = responses.get(0).getList().get(0).getDeg();
        holder.degree.setText((int) degre);
        @SuppressLint("SimpleDateFormat")
        String dayOfTheWeek = new SimpleDateFormat("dd MMMM yyyy").format(new Date(responses.get(0).getList().get(0).getDt()));
        holder.dayOfTheWeek.setText(dayOfTheWeek);
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dayOfTheWeek, degree, description;

        ViewHolder(View view) {
            super(view);
             dayOfTheWeek=  view.findViewById(R.id.tvDayOfTheWeek);
            degree =  view.findViewById(R.id.tvDegree);
            description =  view.findViewById(R.id.tvDescription);
        }
    }
}