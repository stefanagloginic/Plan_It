package com.example.user01.planit;

import com.example.user01.planit.GPSEvent;

public interface GPSListener {
    void onGPSSuccess(GPSEvent e);

    void onGPSFailure(GPSEvent e);

    void onGPSConnect(GPSEvent e);
}