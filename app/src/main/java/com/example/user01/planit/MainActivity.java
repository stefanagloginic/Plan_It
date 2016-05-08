package com.example.user01.planit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GPSListener {
    private YelpAPIWrapper yelpAPIWrapper;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GPS gps;
    private String currLocation;
    private AlertDialog dialog;
    private User user;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Bundle data = getIntent().getExtras();
        user = (User) data.getParcelable("user");

        final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome " + user.getFirstName() + " " + user.getLastName());



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Accessing Location");
            dialog = builder.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            //create instance of GPS, immediately tries to obtain user location
            gps = new GPS(this);
            //add MainActivity to list of Listeners for gps
            gps.addGPSListener(this);
            //start gps services
            gps.connect();
        }


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
                yelpAPIWrapper = new YelpAPIWrapper(getBaseContext(), MainActivity.this, settings);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Accessing Location");
                    dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);

                    //create instance of GPS, immediately tries to obtain user location
                    gps = new GPS(this);
                    //add MainActivity to list of Listeners for gps
                    gps.addGPSListener(this);
                    //start gps services
                    gps.connect();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        gps.disconnect();
        super.onStop();
    }

    @Override
    public void onGPSSuccess(GPSEvent e){
        currLocation = gps.getLocation();
        String timeOfDay = "All Day";
        String priceRange = "Any Price Range";
        String activityPreference = "Any Activity";
        ArrayList<String> settings = new ArrayList<>();
        settings.add(currLocation);
        settings.add(timeOfDay);
        settings.add(priceRange);
        settings.add(activityPreference);

        dialog.dismiss();
        Toast.makeText(MainActivity.this, "Success, now loading your day", Toast.LENGTH_SHORT).show();

        yelpAPIWrapper = new YelpAPIWrapper(getBaseContext(), this,  settings);
        yelpAPIWrapper.execute();
    }

    @Override
    public void onGPSFailure(GPSEvent e){
        dialog.dismiss();
        Button generateLocationButton = (Button) findViewById(R.id.location_Button);
        generateLocationButton.setClickable(false);
        generateLocationButton.setAlpha(.5f);
        dialog.dismiss();
        Toast.makeText(MainActivity.this, "Failed to find location", Toast.LENGTH_SHORT).show();
    }



}


