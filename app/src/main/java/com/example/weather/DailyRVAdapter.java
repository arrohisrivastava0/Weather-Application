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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyRVAdapter extends RecyclerView.Adapter<DailyRVAdapter.ViewHolder> {

    private Context context;
    private int isDey;
    private String unit, condition;
    private ArrayList<DailyRVModel> dailyRVModelArrayList;

    public DailyRVAdapter(Context context, ArrayList<DailyRVModel> dailyRVModelArrayList) {
        this.context = context;
        this.dailyRVModelArrayList = dailyRVModelArrayList;
    }

    @NonNull
    @Override
    public DailyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.daily_forecast_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyRVAdapter.ViewHolder holder, int position) {
        DailyRVModel model=dailyRVModelArrayList.get(position);
        unit=model.getUnit();
        isDey=model.getIsDay();
        condition=model.getCondition();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output=new SimpleDateFormat("MMMM d");
        SimpleDateFormat outDay=new SimpleDateFormat("E");
        try{
            Date t=input.parse(model.getDate());
            holder.dateTV.setText(output.format(t));
            Date d=input.parse(model.getDate());
            holder.dayTV.setText(outDay.format(d));
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        holder.conditionTV.setText(condition);

        if (isDey==0){
            holder.dayTV.setTextColor(Color.WHITE);
            holder.dateTV.setTextColor(Color.parseColor("#A8FFFFFF"));
            holder.conditionTV.setTextColor(Color.parseColor("#A8FFFFFF"));
        }
        else {
            holder.dayTV.setTextColor(Color.BLACK); //#34000000
            holder.dateTV.setTextColor(Color.parseColor("#A8000000"));
            holder.conditionTV.setTextColor(Color.parseColor("#A8000000"));
        }

    }

    private void setTextColor(int isDey){

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dayTV, dateTV, conditionTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTV=itemView.findViewById(R.id.DailyDayTV);
            dateTV=itemView.findViewById(R.id.DailyDateTV);
            conditionTV=itemView.findViewById(R.id.DailyConditionTV);
        }
    }

    @Override
    public int getItemCount() {
        return dailyRVModelArrayList.size();
    }
}
