package com.example.user01.planit;

import java.util.EventObject;

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