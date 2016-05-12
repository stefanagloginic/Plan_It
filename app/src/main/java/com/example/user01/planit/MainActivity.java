package com.example.user01.planit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements GPSListener {
    private YelpAPIWrapper yelpAPIWrapper;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GPS gps;
    private String currLocation;
    private User user;
    private boolean requestingLocationUpdates = true;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Condensed.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        Bundle data = getIntent().getExtras();
        user = data.getParcelable("user");

        final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome " + user.getFirstName() + " " + user.getLastName());



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);

        progressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCircularProgressUnclickable();
                CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
                progressCircle.startAnimation();
                String location = null;
                if (currLocation != null && requestingLocationUpdates) {
                    location = currLocation;

                    String timeOfDay = "All Day";
                    String priceRange = "Any Price Range";
                    String activityPreference = "Any Activity";

                    ArrayList<String> settings = new ArrayList<>();
                    settings.add(currLocation);
                    settings.add(timeOfDay);
                    settings.add(priceRange);
                    settings.add(activityPreference);


                    yelpAPIWrapper = new YelpAPIWrapper(MainActivity.this, settings);
                    yelpAPIWrapper.execute();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //create instance of GPS, immediately tries to obtain user location
                    gps = new GPS(this);
                    //add MainActivity to list of Listeners for gps
                    gps.addGPSListener(this);
                    //start gps services
                    gps.connect();
                }
                else {
                    if(requestingLocationUpdates)
                        Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        currLocation = e.getLocation();
        Toast.makeText(MainActivity.this, "Location Update Recieved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSFailure(GPSEvent e){
        Toast.makeText(MainActivity.this, "Failed to find location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSConnect(GPSEvent e) {
        if(requestingLocationUpdates) {
            Toast.makeText(MainActivity.this, "Location updates will begin", Toast.LENGTH_SHORT).show();
            gps.startLocationUpdates();
            currLocation = e.getLocation();
        }
    }

    public void makeCircularProgressUnclickable(){
        CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
        progressCircle.setClickable(false);
    }



    @Override
    public void onPause(){
        super.onPause();
        gps.stopLocationUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(requestingLocationUpdates){
            gps = new GPS(this);
            //add MainActivity to list of Listeners for gps
            gps.addGPSListener(this);
            //start gps services
            gps.connect();
        }
        CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
        progressCircle.resetAnimation();
        progressCircle.stopAnimation();
        progressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCircularProgressUnclickable();
                CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
                progressCircle.startAnimation();
                String location = null;
                if (currLocation != null && requestingLocationUpdates) {
                    location = currLocation;
                }
                String timeOfDay = "All Day";
                String priceRange = "Any Price Range";
                String activityPreference = "Any Activity";

                ArrayList<String> settings = new ArrayList<>();
                settings.add(currLocation);
                settings.add(timeOfDay);
                settings.add(priceRange);
                settings.add(activityPreference);


                yelpAPIWrapper = new YelpAPIWrapper(MainActivity.this, settings);
                yelpAPIWrapper.execute();
            }
        });
    }


}


