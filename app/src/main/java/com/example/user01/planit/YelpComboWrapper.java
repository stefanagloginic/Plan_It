package com.example.user01.planit;

import android.app.Activity;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;


public class YelpComboWrapper {
    private boolean APIReady;
    private Activity activity;
    private String location;
    private YelpAPIWrapper yelp;
    private ArrayList<Business> businessArrayList;
    //need Natasha's class that grabs times and price range

    public YelpComboWrapper(Activity activity,String location) {
        this.activity = activity;
        this.location = location;
        this.executeYelpAPIWrapper();


        //need to integrate natasha's class
    }

    private void executeYelpAPIWrapper(){
        yelp = new YelpAPIWrapper(activity, location);
        yelp.execute();
    }


    public ArrayList<YelpEvent> createYelpEventArray(){
        businessArrayList = yelp.getBusinesses();
        ArrayList<YelpEvent> temp = new ArrayList<YelpEvent>();
        for(Business i : businessArrayList){
            YelpEvent event = new YelpEvent(activity,i);
            temp.add(event);
        }
        return temp;
    }
}
