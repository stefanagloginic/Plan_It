package com.example.user01.planit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;

public class GPS implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private GoogleApiClient googleApiClient;
    private Activity activity;
    private boolean  addressFailed = false;
    private String   location;

    public GPS(Activity activity){
        this.activity = activity;
        googleApiClient = new GoogleApiClient.Builder(activity, this, this).addApi(LocationServices.API).build();
    }

    public void connect(){
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void disconnect(){
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            addressFailed = getAddress(lat,lon);

        }
    }

    private boolean getAddress(double lat, double lon){
        Geocoder geocoder = new Geocoder(activity);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            Address address = addresses.get(0);
            location = address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        catch(IllegalArgumentException e){
            return true;
        }
        return false;
    }




    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        addressFailed = true;
        Toast.makeText(activity, "Failed to connect", Toast.LENGTH_SHORT).show();
    }

    public String getLocation(){
        return location;
    }

    public boolean getAddressFailed(){
        return addressFailed;
    }


}

