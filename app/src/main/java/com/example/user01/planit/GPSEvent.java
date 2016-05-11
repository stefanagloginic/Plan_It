package com.example.user01.planit;

import java.util.EventObject;

/**
 * Created by Angel on 4/19/2016.
 */
public class GPSEvent extends EventObject {
    String location;

    public GPSEvent(Object source, String location ) {
        super(source);
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

}