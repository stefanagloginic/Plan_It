package com.example.user01.planit;

import android.app.Activity;

import com.yelp.clientlib.entities.Business;

import org.jsoup.nodes.Document;

import java.io.IOException;


public class YelpEvent {
    Activity activity;
    String businessName;
    String businessAddress;
    String businessRating;
    String businessURL;
    //String businessHours;
    //YelpHTMLGetter docGetter;
    //Document doc;
    //String businessPriceRange;


    public YelpEvent(Activity activity,Business b) {
        this.activity = activity;
        businessName = b.name();
        businessAddress = b.location().address().get(0);
        businessRating = String.valueOf(b.rating());
        businessURL = b.url();
        //docGetter = new YelpHTMLGetter(activity, businessURL);
        //docGetter.execute();
        //doc = docGetter.getDoc();

        //parser = new YelpHTMLParser(activity, businessURL, "friday");
        //businessHours = parser.getDailyHours();

    }

    public String getBusinessName() { return businessName; }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public String getBusinessRating() {
        return businessRating;
    }

    public String getBusinessURL() {
        return businessURL;
    }

    /*public String getBusinessHours() {
        return businessHours;
    }*/
}
