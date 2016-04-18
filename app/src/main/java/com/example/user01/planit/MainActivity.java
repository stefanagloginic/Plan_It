package com.example.user01.planit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private YelpAPIWrapper yelpAPIWrapper;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GPS gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        //create instance of GPS, immediately tries to obtain user location
        gps = new GPS(this);

        final Spinner timeOfDaySpinner = (Spinner) findViewById(R.id.time_of_day_spinner);
        ArrayAdapter<CharSequence> timeOfDayAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.time_of_day_choices,
                        R.layout.spinner_center_dropdown);
        timeOfDayAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        timeOfDaySpinner.setAdapter(timeOfDayAdapter);

        final Spinner priceRangeSpinner = (Spinner) findViewById(R.id.price_range_spinner);
        ArrayAdapter<CharSequence> priceRangeAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.price_range_choices,
                        R.layout.spinner_center_dropdown);
        priceRangeAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        priceRangeSpinner.setAdapter(priceRangeAdapter);

        final Spinner activityPreferencesSpinner =
                (Spinner) findViewById(R.id.activity_preferences_spinner);
        ArrayAdapter<CharSequence> activityPreferencesAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_preferences_choices,
                        R.layout.spinner_center_dropdown);
        activityPreferencesAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        activityPreferencesSpinner.setAdapter(activityPreferencesAdapter);

        Button generateButton = (Button) findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationInput = (EditText) findViewById(R.id.location_input);
                String location = locationInput.getText().toString();
                String timeOfDay = timeOfDaySpinner.getSelectedItem().toString();
                String priceRange = priceRangeSpinner.getSelectedItem().toString();
                String activityPreference = activityPreferencesSpinner.getSelectedItem().toString();
                ArrayList<String> settings = new ArrayList<>();
                settings.add(location);
                settings.add(timeOfDay);
                settings.add(priceRange);
                settings.add(activityPreference);
                yelpAPIWrapper = new YelpAPIWrapper(getBaseContext(), settings);
                yelpAPIWrapper.execute();
            }
        });

        //Generate schedule with location acquired by GPS
        Button generateLocationButton = (Button) findViewById(R.id.location_Button);
        generateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationInput = (EditText) findViewById(R.id.location_input);
                if(gps.getAddressFailed()) {
                    locationInput.setHint("Location not found");
                }
                else{
                    locationInput.setText(gps.getLocation());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gps.connect();
    }

    @Override
    protected void onStop() {
        gps.disconnect();
        super.onStop();
    }

}


