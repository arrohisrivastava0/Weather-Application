# Weather Application
A Java based Android application which fetches real time weather forecast data from a weather api.

<p>
  <img src="https://github.com/arrohisrivastava0/Weather-Application/blob/master/images/weatherApp_ss1.jpg" width="300">
  <img src="https://github.com/arrohisrivastava0/Weather-Application/blob/master/images/weatherApp_ss2.jpg" width="300">
  <img src="https://github.com/arrohisrivastava0/Weather-Application/blob/master/images/weatherApp_ss3.jpg" width="300">
</p>

## Installation
+ Clone this repository to your local machine using 'git clone'.
+ Open the project in Android Studio.
+ Build and run the application on an Android device or emulator.

## Usage
+ The app will ask for location permission and will by default show the weather forcast for the user's current location.
+ Tap on the search bar and enter the city your choice to see the custom city weather details.
+ Go to the app's settings section to change the units of of temperature, wind, precipitation, visibility and pressure.

## Project setup 

### Android Manifest
Add the following permissions in your AndroidManifest.xml file.

```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
### Import drawables
You can add icons and fonts in the drawable file as per your choice.
Or you can work with the [icons](https://github.com/arrohisrivastava0/Weather-Application/tree/master/app/src/main/res/drawable) and [fonts](https://github.com/arrohisrivastava0/Weather-Application/tree/master/app/src/main/res/drawable) given in this repository.

#### Major used fonts
+ [Heading](https://github.com/arrohisrivastava0/Weather-Application/blob/master/app/src/main/res/font/droid_serif.xml)
+ [Subheading](https://github.com/arrohisrivastava0/Weather-Application/blob/master/app/src/main/res/font/spectral.xml)

### Main Activity UI
Refer to the XML file for the UI given [here](https://github.com/arrohisrivastava0/Weather-Application/blob/master/app/src/main/res/layout/activity_main.xml)

### Weather API
Refer to the base URL for the API used in this project.
  ```java
    String url1="https://api.weatherapi.com/v1/forecast.json?key=<YOUR_KEY>&q="+cityName+"&days=15&aqi=yes&alerts=yes";
  ```


### Fetching the weather data from the API

```java
private void getWeatherInfo(String cityName){

  String url1="<BASE_URL>
  cityNameTV.setText(cityName);

  RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);

  JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {

    public void onResponse(JSONObject response) {
      //handled on response operations
    }

  }

}
```
