package com.example.user01.planit;

import android.app.Activity;

import java.util.ArrayList;


public class YelpComboWrapper {
    private boolean APIReady;
    private Activity activity;
    private String location;
    private YelpAPIWrapper yelp;

    public YelpComboWrapper(Activity activity,String location) {
        this.activity = activity;
        this.location = location;
        this.executeYelpAPIWrapper();
    }

    private void executeYelpAPIWrapper(){
        yelp = new YelpAPIWrapper(activity, location);
        yelp.execute();
    }

    public ArrayList<Event> createYelpEventArray(){
        return yelp.getYelpEvents();
    }
}
