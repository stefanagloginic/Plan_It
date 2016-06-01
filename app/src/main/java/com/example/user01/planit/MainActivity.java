package com.example.user01.planit;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private String currLocation = null;
    private User user;
    private boolean requestingLocationUpdates = true;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Condensed.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        Bundle data = getIntent().getExtras();
        user = data.getParcelable("user");

       /* final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome " + user.getFirstName() + " " + user.getLastName());
*/


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        setFABListener();

        setFABListener();

        setCircularProgress();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setNewGPS();
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
        Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSFailure(GPSEvent e){
        currLocation = null;
        Toast.makeText(MainActivity.this, "Failed to find location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSConnect(GPSEvent e) {
        if(requestingLocationUpdates) {
            gps.startLocationUpdates();
            currLocation = e.getLocation();
        }
    }

    public void makeCircularProgressUnclickable(){
        CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
        progressCircle.setThickness(30);
        progressCircle.setColor(ContextCompat.getColor(getApplicationContext(),R.color
                .colorPrimary));
        progressCircle.setClickable(false);
    }



    @Override
    public void onPause(){
        super.onPause();
        if(gps.getIsGPSConnected())
            gps.stopLocationUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();

        final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);
        fabSettings.setVisibility(View.VISIBLE);

        setNewGPS();

        setFABListener();

        setCircularProgress();
    }

    private void setFABListener(){
        final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout relativeLayoutContent = (RelativeLayout) findViewById(R.id.Content);
                relativeLayoutContent.setAlpha(0.1f);
                fabSettings.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutSettings = (RelativeLayout) findViewById(R.id.Settings);
                relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relativeLayoutContent.setAlpha(1.0f);
                        relativeLayoutSettings.setVisibility(View.GONE);
                        fabSettings.setVisibility(View.VISIBLE);
                    }
                });

                setFABGPS();
                setFABLogout();
                setFABAccount();

                relativeLayoutSettings.setVisibility(View.VISIBLE);


            }
        });
    }

    private void setCircularProgress(){
        CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
        progressCircle.resetAnimation();
        progressCircle.stopAnimation();
        progressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requestingLocationUpdates || currLocation == null){
                    createDialogBoxLocation();
                }
                else {

                    final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);
                    fabSettings.setVisibility(View.GONE);

                    makeCircularProgressUnclickable();
                    CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
                    progressCircle.startAnimation();


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

    private void setNewGPS(){
        if(requestingLocationUpdates){
            gps = new GPS(this);
            //add MainActivity to list of Listeners for gps
            gps.addGPSListener(this);
            //start gps services
            gps.connect();
        }
    }

    private void setFABGPS(){
        FloatingActionButton fabGPS = (FloatingActionButton) findViewById(R.id.fabGPS);
        fabGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView gpsStatus = (TextView) findViewById(R.id.tvGPSStatus);

                if(requestingLocationUpdates){
                    gpsStatus.setText("Start GPS");
                    currLocation = null;
                    requestingLocationUpdates = false;
                    gps.stopLocationUpdates();
                }else{
                    gpsStatus.setText("Stop GPS");
                    requestingLocationUpdates = true;
                    setNewGPS();
                }
            }
        });
    }

    private void setFABLogout(){
        FloatingActionButton fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginLogoutHelpers.logUserOut(MainActivity.this);
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(logoutIntent);
                MainActivity.this.finish();
            }
        });
    }

    private void setFABAccount(){
        FloatingActionButton fabAccount = (FloatingActionButton) findViewById(R.id.fabAccount);
        fabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AccountIntent = new Intent(MainActivity.this, AccountActivity.class);
                AccountIntent.putExtra("user", user);
                MainActivity.this.startActivity(AccountIntent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        final RelativeLayout relativeLayoutSettings = (RelativeLayout) findViewById(R.id.Settings);
        int visibility = relativeLayoutSettings.getVisibility();
        final RelativeLayout relativeLayoutContent = (RelativeLayout) findViewById(R.id.Content);
        final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);

        if(visibility == View.VISIBLE){
            relativeLayoutContent.setAlpha(1.0f);
            relativeLayoutSettings.setVisibility(View.GONE);
            fabSettings.setVisibility(View.VISIBLE);
        }
        else if(yelpAPIWrapper != null && yelpAPIWrapper.getStatus() == AsyncTask.Status.RUNNING){

        }
        else{
            this.finish();
        }
    }

    public void createDialogBoxLocation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Location");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currLocation = input.getText().toString();

                final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);
                fabSettings.setVisibility(View.GONE);

                makeCircularProgressUnclickable();
                CircularProgressView progressCircle = (CircularProgressView) findViewById(R.id.progress_circle);
                progressCircle.startAnimation();


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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currLocation = null;
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Need a location!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

}


