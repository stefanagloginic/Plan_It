package com.example.user01.planit;

/**
 * Created by Angel on 4/19/2016.
 */
public interface GPSListener {
    void onGPSSuccess(GPSEvent e);

    void onGPSFailure(GPSEvent e);
}
