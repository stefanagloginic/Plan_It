package com.example.user01.planit;

import android.app.Activity;

import com.yelp.clientlib.entities.Business;

import java.io.IOException;


public class YelpEvent extends Event{
    private Activity activity;
    private String businessHours;
    private YelpHTMLWrapper yelpHTMLWrapper;

    public YelpEvent(Activity activity,Business b) {
        this.activity = activity;
        this.eventName = b.name();
        this.eventAddress = b.location().address().get(0);
        this.eventRating = String.valueOf(b.rating());
        this.eventURL = b.url();
        yelpHTMLWrapper = new YelpHTMLWrapper(activity, eventURL);
        yelpHTMLWrapper.execute();
    }

    public String getBusinessName() { return eventName; }

    public String getBusinessAddress() { return eventAddress; }

    public String getBusinessRating() {
        return eventRating;
    }

    public String getBusinessURL() {
        return eventURL;
    }

    public String getBusinessHours() {
        try {
            businessHours = yelpHTMLWrapper.getDailyHour("monday");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public String getBusinessPriceRange() {
        return eventPriceRange = yelpHTMLWrapper.getPriceRange();
    }

}
