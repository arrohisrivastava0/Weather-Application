package com.example.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView  cityNameTV, temperatureTV, conditionTV, hourlyForecastHeadingTV, minTempTV, maxTempTV,
            precipitationTV, windTV, humidityTV, visibilityTV, UVTV, pressureTV, feelsLikeTV, sunriseTV, sunsetTV, sunTV, detTV,
            pnTV,preTV,wTV,hTV,vTV,uTV, rainChanceTV, detailCondition, dailyHeadingTV;
    private ScrollView scrollView;
    private RecyclerView weatherForecastRV;
    private RecyclerView dailyForecastRV;
    private TextInputEditText editCityTIET;
    private ImageView  backIV, searchIV ,weatherIconIV, maxTempIV, minTempIV, feelsLikeIV, rainChanceIV;
    private WeatherRVAdapter weatherRVAdapter;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private DailyRVAdapter dailyRVAdapter;
    private ArrayList<DailyRVModel> dailyRVModelArrayList;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1, isDay;
    private String cityName,temperatureUnit,windUnit,precipitationUnit,visibilityUnit,pressureUnit;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GraphView gref;


//    private static int isDay;
//
//    public static void setIsDay(int isDay) {
//        MainActivity.isDay = isDay;
//    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeRL=findViewById(R.id.WHome);
        loadingPB=findViewById(R.id.loading);
        cityNameTV=findViewById(R.id.CityName);
        temperatureTV=findViewById(R.id.Temperature);
        conditionTV=findViewById(R.id.Condition);
        hourlyForecastHeadingTV=findViewById(R.id.HourForecastHeadingTV);
        weatherForecastRV=findViewById(R.id.WeatherForecastRV);
        dailyForecastRV=findViewById(R.id.DailyForecastRV);
        editCityTIET=findViewById(R.id.EditedCityTIET);
        backIV=findViewById(R.id.background);
        searchIV=findViewById(R.id.Search);
        weatherIconIV=findViewById(R.id.WeatherIcon);
        dailyRVModelArrayList=new ArrayList<>();
        dailyRVAdapter=new DailyRVAdapter(this,dailyRVModelArrayList);
        dailyForecastRV.setAdapter(dailyRVAdapter);
        weatherRVModelArrayList=new ArrayList<>();
        weatherRVAdapter=new WeatherRVAdapter(this,weatherRVModelArrayList);
        weatherForecastRV.setAdapter(weatherRVAdapter);
        minTempTV=findViewById(R.id.minTempTV);
        maxTempTV=findViewById(R.id.maxTempTV);
        precipitationTV=findViewById(R.id.preciContentTV);
        humidityTV=findViewById(R.id.humidityContentTV);
        UVTV=findViewById(R.id.uvContentTV);
        pressureTV=findViewById(R.id.pressureContentTV);
        visibilityTV=findViewById(R.id.visibilityContentTV);
        windTV=findViewById(R.id.windSpeedContentTV);
        maxTempIV=findViewById(R.id.maxTempIV);
        minTempIV=findViewById(R.id.minTempIV);
        feelsLikeTV=findViewById(R.id.feelsTempTV);
        feelsLikeIV=findViewById(R.id.feelsLike);
        sunriseTV=findViewById(R.id.sunriseTimeTV);
        sunsetTV=findViewById(R.id.sunsetTimeTV);
        sunTV=findViewById(R.id.SunHeadingTV);
        detTV=findViewById(R.id.DetailsHeadingTV);
        pnTV=findViewById(R.id.preciHeadingTV);
        preTV=findViewById(R.id.pressureHeadingTV);
        hTV=findViewById(R.id.humidityHeadingTV);
        vTV=findViewById(R.id.visibilityHeadingTV);
        uTV=findViewById(R.id.uvHeadingTV);
        wTV=findViewById(R.id.windSpeedHeadingTV);
        rainChanceIV=findViewById(R.id.rainChanceIV);
        rainChanceTV=findViewById(R.id.rainChanceTV);
        scrollView=findViewById(R.id.scrollView);
        detailCondition=findViewById(R.id.DetailedConditionTV);
        dailyHeadingTV=findViewById(R.id.DailyHeadingTV);
        gref=findViewById(R.id.GraphGV);
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        temperatureUnit = sharedPreferences.getString("Temperature", "Celsius - °c");
         windUnit = sharedPreferences.getString("Wind", "Kilometres per hour - km/h");
         precipitationUnit = sharedPreferences.getString("Precipitation", "Millimetres - mm");
         visibilityUnit = sharedPreferences.getString("Visibility", "Kilometres - km");
         pressureUnit = sharedPreferences.getString("Pressure", "Millibar - mb");

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){cityName = getCityName(location.getLatitude(),location.getLongitude());
            {
                getWeatherInfo(cityName);
            }
        } else {
            getWeatherInfo("London");
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    // User is at the top of the ScrollView
                    swipeRefreshLayout.setEnabled(true); // Enable swipe to refresh
                } else {
                    // User is not at the top
                    swipeRefreshLayout.setEnabled(false); // Disable swipe to refresh
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingPB.setVisibility(View.VISIBLE);
                getWeatherInfo(cityName);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city=editCityTIET.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter a city",Toast.LENGTH_SHORT).show();

                }else {
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });


    }

    public int getIsDay() {
        return isDay;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_CODE){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permissions Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please modify permissions",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void openSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);

    }
    private String getCityName(double latitude, double longitude){
        String cityName="Not Found";
        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addresses=gcd.getFromLocation(latitude,longitude,10);
            for(Address adr : addresses){
                if (adr!=null){
                    String city=adr.getLocality();
//                    String area = adr.getSubLocality();
                    if (city!=null&&city!=""){
                        cityName=city;
                    }
//                    else {
//                        Log.d("TAG","CITY NOT FOUND");
//                        Toast.makeText(this,"Couldn't find city",Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }

    private void changeTextColorInDay(){
//        cityNameTV.setTextColor(Color.BLACK);
        rainChanceIV.setImageResource(R.drawable.sales_black);
        rainChanceTV.setTextColor(Color.BLACK);
        pnTV.setTextColor(Color.BLACK);
        wTV.setTextColor(Color.BLACK);
        hTV.setTextColor(Color.BLACK);
        vTV.setTextColor(Color.BLACK);
        uTV.setTextColor(Color.BLACK);
        preTV.setTextColor(Color.BLACK);
        precipitationTV.setTextColor(Color.BLACK);
        windTV.setTextColor(Color.BLACK);
        humidityTV.setTextColor(Color.BLACK);
        visibilityTV.setTextColor(Color.BLACK);
        UVTV.setTextColor(Color.BLACK);
        pressureTV.setTextColor(Color.BLACK);
        sunTV.setTextColor(Color.BLACK);
        detTV.setTextColor(Color.BLACK);
        sunriseTV.setTextColor(Color.BLACK);
        sunsetTV.setTextColor(Color.BLACK);
        feelsLikeIV.setImageResource(R.drawable.temperature_feels_like_black);
        feelsLikeTV.setTextColor(Color.BLACK);
        maxTempIV.setImageResource(R.drawable.up_arrow);
        minTempIV.setImageResource(R.drawable.downarrowfin);
        editCityTIET.setTextColor(Color.BLACK);
        minTempTV.setTextColor(Color.BLACK);
        maxTempTV.setTextColor(Color.BLACK);
        temperatureTV.setTextColor(Color.BLACK);
        conditionTV.setTextColor(Color.BLACK);
        hourlyForecastHeadingTV.setTextColor(Color.BLACK);
        detailCondition.setTextColor(Color.BLACK);
        dailyHeadingTV.setTextColor(Color.BLACK);
    }

    private void changeTextColorInNight(){
//        cityNameTV.setTextColor(Color.WHITE);
        rainChanceIV.setImageResource(R.drawable.sales);
        rainChanceTV.setTextColor(Color.WHITE);
        pnTV.setTextColor(Color.WHITE);
        wTV.setTextColor(Color.WHITE);
        hTV.setTextColor(Color.WHITE);
        vTV.setTextColor(Color.WHITE);
        uTV.setTextColor(Color.WHITE);
        preTV.setTextColor(Color.WHITE);
        precipitationTV.setTextColor(Color.WHITE);
        windTV.setTextColor(Color.WHITE);
        humidityTV.setTextColor(Color.WHITE);
        visibilityTV.setTextColor(Color.WHITE);
        UVTV.setTextColor(Color.WHITE);
        pressureTV.setTextColor(Color.WHITE);
        sunTV.setTextColor(Color.WHITE);
        detTV.setTextColor(Color.WHITE);
        sunriseTV.setTextColor(Color.WHITE);
        sunsetTV.setTextColor(Color.WHITE);
        feelsLikeIV.setImageResource(R.drawable.image);
        feelsLikeTV.setTextColor(Color.WHITE);
        maxTempIV.setImageResource(R.drawable.up_arrow_white);
        minTempIV.setImageResource(R.drawable.down_arrow_white);
        editCityTIET.setTextColor(Color.WHITE);
        minTempTV.setTextColor(Color.WHITE);
        maxTempTV.setTextColor(Color.WHITE);
        temperatureTV.setTextColor(Color.WHITE);
        conditionTV.setTextColor(Color.WHITE);
        hourlyForecastHeadingTV.setTextColor(Color.WHITE);
        detailCondition.setTextColor(Color.WHITE);
        dailyHeadingTV.setTextColor(Color.WHITE);
    }
    private void getWeatherInfo(String cityName){
        loadingPB.setVisibility(View.VISIBLE);
        String url1="https://api.weatherapi.com/v1/forecast.json?key=795282c53dc0478cbcf111844232406&q="+cityName+"&days=15&aqi=yes&alerts=yes";
        String url2="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+cityName+"?unitGroup=us&key=UAAJ4DK2WCM2Z9H3PS4382V75&contentType=json";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                weatherRVModelArrayList.clear();
                dailyRVModelArrayList.clear();
                gref.removeAllSeries();
                String temperatureC,feelTempC,windSpeedDetKmh,preciDetMm,visibilityDetKm,pressureDetMb;
                try {
                    if (temperatureUnit.equals("Fahrenheit - °F")){
                        temperatureC=response.getJSONObject("current").getString("temp_f");
                        feelTempC=response.getJSONObject("current").getString("feelslike_f");
                        String minimumTemp=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("mintemp_f");
                        String maximumTemp=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("maxtemp_f");
                        minTempTV.setText(minimumTemp.concat("°F"));
                        maxTempTV.setText(maximumTemp.concat("°F"));
                        temperatureTV.setText(temperatureC+"°F");
                        feelsLikeTV.setText(feelTempC+"°F");
                    }
                    else {
                        temperatureC=response.getJSONObject("current").getString("temp_c");
                        feelTempC=response.getJSONObject("current").getString("feelslike_c");
                        String minimumTemp=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("mintemp_c");
                        String maximumTemp=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("maxtemp_c");
                        minTempTV.setText(minimumTemp.concat("°c"));
                        maxTempTV.setText(maximumTemp.concat("°c"));
                        temperatureTV.setText(temperatureC+"°c");
                        feelsLikeTV.setText(feelTempC+"°c");
                    }
                    if (windUnit.equals("Miles per hour - m/h")){
                        windSpeedDetKmh=response.getJSONObject("current").getString("wind_mph");
                        windTV.setText(windSpeedDetKmh+" m/h");
                    }
                    else {
                        windSpeedDetKmh=response.getJSONObject("current").getString("wind_kph");
                        windTV.setText(windSpeedDetKmh+" km/h");
                    }
                    if (precipitationUnit.equals("Inches - in")){
                        preciDetMm=response.getJSONObject("current").getString("precip_in");
                        precipitationTV.setText(preciDetMm+" in");
                    }
                    else {
                        preciDetMm=response.getJSONObject("current").getString("precip_mm");
                        precipitationTV.setText(preciDetMm+" mm");
                    }
                    if (visibilityUnit.equals("Miles - mi")){
                        visibilityDetKm=response.getJSONObject("current").getString("vis_miles");
                        visibilityTV.setText(visibilityDetKm+" mi");
                    }
                    else {
                        visibilityDetKm=response.getJSONObject("current").getString("vis_km");
                        visibilityTV.setText(visibilityDetKm+" km");
                    }
                    if (pressureUnit.equals("Inches - in")){
                        pressureDetMb=response.getJSONObject("current").getString("pressure_in");
                        pressureTV.setText(pressureDetMb+" in");
                    }
                    else {
                        pressureDetMb=response.getJSONObject("current").getString("pressure_mb");
                        pressureTV.setText(pressureDetMb+" mb");
                    }

                    String humidityDet=response.getJSONObject("current").getString("humidity");
                    humidityTV.setText(humidityDet+"%");

                    String uvDet=response.getJSONObject("current").getString("uv");
                    setUV(uvDet);

                    int issDay=response.getJSONObject("current").getInt("is_day");
                    setIsDay(issDay);

                    String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    conditionTV.setText(condition);

                    String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(weatherIconIV);

                    String conditionalBgUrl=setWallpaper(isDay, condition);
                    Picasso.get().load(conditionalBgUrl).into(backIV);

                    JSONObject forecastObj=response.getJSONObject("forecast");
                    JSONObject forecastO=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray forecastDaily=forecastObj.getJSONArray("forecastday");
                    JSONArray hourArray=forecastO.getJSONArray("hour");

                    String sunrise=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunrise");
                    String sunset=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunset");

                    sunriseTV.setText(sunrise);
                    sunsetTV.setText(sunset);

                    String rainPercent=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getString("daily_chance_of_rain");

                    rainChanceTV.setText(rainPercent+"%");

                    for (int i=0;i<hourArray.length();i++){
                        JSONObject hourObj=hourArray.getJSONObject(i);
                        String time=hourObj.getString("time");
                        String temp;
                        if (temperatureUnit.equals("Fahrenheit - °F")) {
                            temp=hourObj.getString("temp_f");
                        } else {
                            temp=hourObj.getString("temp_c");
                        }
                        String img=hourObj.getJSONObject("condition").getString("icon");
                        weatherRVModelArrayList.add(new WeatherRVModel(time,temp,img,isDay, temperatureUnit));
                    }
                    weatherRVAdapter.notifyDataSetChanged();

                    getDailyInfo(cityName);

                    //JSONObject forecastO=forecastObj.getJSONArray("forecastday").getJSONObject(0);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter a valid location",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest1);
    }


    public void getDailyInfo(String cityName){
        String url2="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+cityName+"?unitGroup=us&key=UAAJ4DK2WCM2Z9H3PS4382V75&contentType=json";
        RequestQueue requestQueue2= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dates = response.getJSONArray("days");
                    LineGraphSeries<DataPoint> minseries=new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> maxseries=new LineGraphSeries<>();

                    for (int j = 0; j < dates.length(); j++) {
                        JSONObject dailyObj = dates.getJSONObject(j);
                        String date = dailyObj.getString("datetime");
                        double mex, min;
                        if (temperatureUnit.equals("Fahrenheit - °F")) {
                            mex=Double.parseDouble(dailyObj.getString("tempmax"));
                            min = Double.parseDouble(dailyObj.getString("tempmin"));
                        } else {
                            double cheng1 = Double.parseDouble(dailyObj.getString("tempmax"));
                            mex=((cheng1-32)*5)/9;
                            double cheng2 = Double.parseDouble(dailyObj.getString("tempmin"));
                            min=((cheng2-32)*5)/9;
                        }
                        if (j==0){
                            maxseries.appendData(new DataPoint(0, mex), true, dates.length());
                            minseries.appendData(new DataPoint(0, min), true, dates.length());
                            maxseries.appendData(new DataPoint(j+1, mex), true, dates.length());
                            minseries.appendData(new DataPoint(j+1, min), true, dates.length());
                        }else {
                            maxseries.appendData(new DataPoint(j+1, mex), true, dates.length());
                            minseries.appendData(new DataPoint(j+1, min), true, dates.length());
                        }
//                        minseries.setColor(Color.BLUE);
//                        maxseries.setColor(Color.RED);
//
//                        gref.addSeries(series);
//                        gref.addSeries(maxseries);



                        String condition = dailyObj.getString("conditions");
                        dailyRVModelArrayList.add(new DailyRVModel(date, condition, temperatureUnit,isDay));
                    }
                    dailyRVAdapter.notifyDataSetChanged();
                    minseries.setColor(Color.BLUE);
                    maxseries.setColor(Color.YELLOW);
                    minseries.setDrawDataPoints(true);
                    maxseries.setDrawDataPoints(true);
                    minseries.setDataPointsRadius(8);
                    maxseries.setDataPointsRadius(8);
                    minseries.setThickness(3);
                    maxseries.setThickness(3);
                    gref.addSeries(minseries);
                    gref.addSeries(maxseries);
                    gref.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    gref.getGridLabelRenderer().setVerticalLabelsVisible(false);
                    gref.invalidate();

                    String condi=response.getJSONArray("days").getJSONObject(0).getString("description");
                    detailCondition.setText(condi);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter a valid location",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue2.add(jsonObjectRequest2);
    }

    private String setWallpaper(int isDay,String condition){
        if (isDay==1){
            changeTextColorInDay();
            switch (condition){
                case "Partly cloudy":
                    return "https://images.unsplash.com/photo-1596827270732-174209709611?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=974&q=80";

                case "Patchy rain possible":
                case "Light rain shower":
                case "Moderate or heavy rain shower":
                    return "https://media.istockphoto.com/id/182176554/photo/drops-falling-on-water-surface.jpg?s=612x612&w=0&k=20&c=rwQwpwIknJdxq_hn9h4Rn2NUIX1_pbpTp-n7XzM_3T4=";

                case "Patchy light rain with thunder":
                    return "https://media.istockphoto.com/id/469850273/photo/lightning-with-dramatic-cloudscape.jpg?s=612x612&w=0&k=20&c=AwlD6sOgSOaX6MojEFnIiwzLMmX0WZ_bT-dTMgv-hXk=";

                case "Overcast":
                    return "https://media.istockphoto.com/id/132057426/photo/sun-beams-bursting-through-a-dramatic-sky.jpg?s=612x612&w=0&k=20&c=ESax6Kb7qt22BpT-kXMdjF2iPfOebVRmkengm20JAZA=";

                case "Sunny":
                    return "https://media.istockphoto.com/id/693853414/photo/sunlight-through-clouds.jpg?s=612x612&w=0&k=20&c=crNn-t_uYuUEWH5F_WcmUlQIdTh1NWpSJdvKeJutNnk=";

                case "Cloudy":
                    return "https://media.istockphoto.com/id/477057828/photo/blue-sky-white-cloud.jpg?s=612x612&w=0&k=20&c=GEjySNaROrUD7TJUqoXEiBDI9yMmr2hUviSOox4SDlU=";

                default:
                    return "https://img.freepik.com/premium-photo/vertical-image-white-clouds-blue-sky-morning_35782-208.jpg?w=360";

            }
        }
        else {
            changeTextColorInNight();
            switch (condition){
                case "Patchy rain possible":
                case "Light rain shower":
                case "Moderate or heavy rain shower":
                    return "https://i.ibb.co/8PLccSC/rain-Night.jpg";

                case "Patchy light rain with thunder":
                    return "https://media.istockphoto.com/id/469850273/photo/lightning-with-dramatic-cloudscape.jpg?s=612x612&w=0&k=20&c=AwlD6sOgSOaX6MojEFnIiwzLMmX0WZ_bT-dTMgv-hXk=";

                case "Partly cloudy":
                    return "https://images.unsplash.com/photo-1622072165281-7be98a19a47b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80";

                case "Cloudy":
                    return "https://cdn.pixabay.com/photo/2017/05/21/01/42/full-2330283_1280.jpg";

                case "Overcast":
                    return "https://media.istockphoto.com/id/1220435368/photo/storm-clouds-in-summer.jpg?s=612x612&w=0&k=20&c=BO_qc8UtFunBeCkITYwIFbZSwYKZB9hc6HVFlhRfINM=";

                default:
                    return "https://images.unsplash.com/photo-1599045150678-4990323df445?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=380&q=80";

            }
        }
    }

    private void setUV(String uvDet){
        double uvInt=Double.parseDouble(uvDet);
        String uvText;
        if (uvInt<=2){
            uvText="Lowest";
        }
        else if (uvInt<=5){
            uvText="Moderate";
        }
        else if (uvInt<=7){
            uvText="High";
        }
        else if (uvInt<=10){
            uvText="Very High";
        }
        else {
            uvText="Extremely High";
        }
        UVTV.setText(uvText);
    }
}
