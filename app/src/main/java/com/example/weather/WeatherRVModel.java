package com.example.weather;

public class WeatherRVModel {

    private String time, temperature, icon, unit;
    private int isDay;
//    private String windSpeed;

    public WeatherRVModel(String time, String temperature, String icon, int isDay, String unit) {
        this.isDay=isDay;
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.unit=unit;
//        this.windSpeed = windSpeed;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

//    public String getWindSpeed() {
//        return windSpeed;
//    }
//
//    public void setWindSpeed(String windSpeed) {
//        this.windSpeed = windSpeed;
//    }


}
