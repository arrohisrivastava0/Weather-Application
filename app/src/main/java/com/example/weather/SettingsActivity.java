package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout temperatureOption;
    private LinearLayout windOption;
    private LinearLayout precipitationOption;
    private LinearLayout visibilityOption;
    private LinearLayout pressureOption;
    private TextView temperatureUnit, windUnit, preciUnit, visiUnit, presUnit;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        temperatureOption = findViewById(R.id.TempOpt);
        windOption = findViewById(R.id.WindOpt);
        precipitationOption = findViewById(R.id.PrecipitationOpt);
        visibilityOption = findViewById(R.id.VisibilityOpt);
        pressureOption = findViewById(R.id.PressureOpt);
        temperatureUnit = findViewById(R.id.temperatureUnitTV);
        windUnit = findViewById(R.id.windUnitTV);
        preciUnit = findViewById(R.id.precipitationUnitTV);
        visiUnit = findViewById(R.id.visibilityUnitTV);
        presUnit = findViewById(R.id.pressureUnitTV);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        temperatureOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitSelectionMenu(v, "Temperature");
            }
        });

        windOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitSelectionMenu(v, "Wind");
            }
        });

        precipitationOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitSelectionMenu(v, "Precipitation");
            }
        });

        visibilityOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitSelectionMenu(v, "Visibility");
            }
        });

        pressureOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitSelectionMenu(v, "Pressure");
            }
        });

        // Additional code for your activity...
    }

    private void showUnitSelectionMenu(View anchorView, final String option) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        int menuResId = getMenuResourceForOption(option);
        popupMenu.getMenuInflater().inflate(menuResId, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the selected unit choice
                String unit = getUnitFromMenuItem(item, option);
                handleUnitChoice(option, unit);
                return true;
            }
        });

        popupMenu.show();
    }


    private int getMenuResourceForOption(String option) {
        switch (option) {
            case "Temperature":
                return R.menu.temperature_menu;
            case "Wind":
                return R.menu.wind_menu;
            case "Precipitation":
                return R.menu.precipitation_menu;
            case "Visibility":
                return R.menu.visibility_menu;
            case "Pressure":
                return R.menu.pressure_menu;
            default:
                return 0;
        }
    }

    private void handleUnitChoice(String option, String unit) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(option, unit);
        editor.apply();

        switch (option) {
            case "Temperature":
                temperatureUnit.setText(unit);
                break;
            case "Wind":
                windUnit.setText(unit);
                break;
            case "Precipitation":
                preciUnit.setText(unit);
                break;
            case "Visibility":
                visiUnit.setText(unit);
                break;
            case "Pressure":
                presUnit.setText(unit);
                break;
        }
    }


    private String getUnitFromMenuItem(MenuItem item, String option) {
        if (option.equals("Temperature")) {
            if (item.getItemId() == R.id.titem1) {
                return "Celsius - °c";
            } else if (item.getItemId() == R.id.titem2) {
                return "Fahrenheit - °F";
            }
        } else if (option.equals("Wind")) {
            if (item.getItemId() == R.id.witem1) {
                return "Kilometres per hour - km/h";
            } else if (item.getItemId() == R.id.witem2) {
                return "Miles per hour - m/h";
            }
        } else if (option.equals("Precipitation")) {
            if (item.getItemId() == R.id.pcitem1) {
                return "Millimetres - mm";
            } else if (item.getItemId() == R.id.pcitem2) {
                return "Inches - in";
            }
        } else if (option.equals("Visibility")) {
            if (item.getItemId() == R.id.vitem1) {
                return "Kilometres - km";
            } else if (item.getItemId() == R.id.vitem2) {
                return "Miles - mi";
            }
        } else if (option.equals("Pressure")) {
            if (item.getItemId() == R.id.psitem1) {
                return "Millibar - mb";
            } else if (item.getItemId() == R.id.psitem2) {
                return "Inches - in";
            }
        }
        return "";
    }
    @Override
    public void onBackPressed() {
        goBackToMainActivity();
    }

    public void goBack(View view) {
        goBackToMainActivity();
    }
    public void goBackToMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}