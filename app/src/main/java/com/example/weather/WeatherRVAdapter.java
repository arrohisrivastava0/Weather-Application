package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private int isDey;
    private String unit;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModel> weatherRVModelArrayList) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.weather_forecast_items, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
        WeatherRVModel model=weatherRVModelArrayList.get(position);
        isDey=model.getIsDay();
        unit=model.getUnit();
        if (unit.equals("Fahrenheit - °F")){
            holder.temperatureTV.setText(model.getTemperature()+"°F");
        }
        else {
            holder.temperatureTV.setText(model.getTemperature()+"°c");
        }
        Picasso.get().load("https:".concat(model.getIcon())).into(holder.conditionIconIV);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try{
            Date t=input.parse(model.getTime());
            holder.timeTV.setText(output.format(t));

        }
        catch (ParseException e){
            e.printStackTrace();
        }
        if (isDey==0){
            holder.temperatureTV.setTextColor(Color.WHITE);
            holder.timeTV.setTextColor(Color.WHITE);
        }
        else {
            holder.temperatureTV.setTextColor(Color.BLACK);
            holder.timeTV.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {

        return weatherRVModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView temperatureTV, timeTV;
        private ImageView conditionIconIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTV=itemView.findViewById(R.id.HourlyTemperatureTV);
            timeTV=itemView.findViewById(R.id.HourlyTimeTV);
            conditionIconIV=itemView.findViewById(R.id.HourlyConditionIconIV);
        }
    }
}
