package com.example.user01.planit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GPS implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private Activity activity;
    private ArrayList<GPSListener> listeners;
    private boolean isGPSConnected;

    public GPS(Activity activity) {
        this.isGPSConnected = false;
        this.activity = activity;
        googleApiClient = new GoogleApiClient.Builder(activity, this, this).addApi(LocationServices.API).build();
        this.listeners = new ArrayList<GPSListener>();
    }

    public void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        this.isGPSConnected = true;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            String location = getAddress(lat, lon);

            if (location != null) {
                notifyGPSListenersOfOnConnected(location);
            } else {
                notifyGPSListenersOfFailure();
            }
        }

    }

    private void notifyGPSListenersOfOnConnected(String location) {
        for (GPSListener i : listeners) {
            i.onGPSConnect(new GPSEvent(this, location));
        }
    }

    private String getAddress(double lat, double lon) {
        Geocoder geocoder = new Geocoder(activity);
        String location = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            Address address = addresses.get(0);
            location = address.getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            return location;
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(activity, "Failed to connect", Toast.LENGTH_SHORT).show();
        notifyGPSListenersOfFailure();
    }

    public void addGPSListener(GPSListener g) {
        listeners.add(g);
    }

    private void notifyGPSListenersOfSuccess(String location) {
        for (GPSListener i : listeners) {
            i.onGPSSuccess(new GPSEvent(this, location));
        }
    }

    private void notifyGPSListenersOfFailure() {
        for (GPSListener i : listeners) {
            i.onGPSFailure(new GPSEvent(this, null));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude(), lon = location.getLongitude();
        String loc = getAddress(lat, lon);
        notifyGPSListenersOfSuccess(loc);
    }

    public void startLocationUpdates() {
        this.isGPSConnected = true;
        LocationRequest lr = LocationRequest.create();
        lr.setInterval(300000);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, lr, this);
        }
    }

    public void stopLocationUpdates(){
        this.isGPSConnected = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    public boolean getIsGPSConnected(){
        return isGPSConnected;
    }

}
