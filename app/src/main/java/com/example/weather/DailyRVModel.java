package com.example.weather;

public class DailyRVModel {

    private String date, day, condition, unit;
    private int isDay;

    public DailyRVModel(String date, String condition, String unit, int isDay) {
        this.date = date;
        this.condition = condition;
        this.unit = unit;
        this.isDay=isDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getIsDay() {
        return isDay;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }
}