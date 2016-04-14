package com.example.user01.planit;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import com.yelp.clientlib.entities.Business;

import java.io.IOException;


public class YelpEvent extends Event implements Parcelable{
    private Activity activity;
    private String businessHours;
    private YelpHTMLWrapper yelpHTMLWrapper;

    public YelpEvent(Parcel in) {
        eventName = in.readString();
        eventAddress = in.readString();
        eventRating = in.readString();
        eventURL = in.readString();
        eventPriceRange = in.readString();
    }

    public YelpEvent(Activity activity,Business b) {
        this.activity = activity;
        this.eventName = b.name();
        this.eventAddress = b.location().address().get(0);
        this.eventRating = String.valueOf(b.rating());
        this.eventURL = b.url();
        yelpHTMLWrapper = new YelpHTMLWrapper(activity, eventURL);
        yelpHTMLWrapper.execute();
    }

    public static final Parcelable.Creator<YelpEvent> CREATOR =
            new Parcelable.Creator<YelpEvent>() {
                public YelpEvent createFromParcel(Parcel in) {
                    return new YelpEvent(in);
                }

                @Override
                public YelpEvent[] newArray(int size) {
                    return new YelpEvent[size];
                }
            };

    public String getBusinessHours() {
        try {
            businessHours = yelpHTMLWrapper.getDailyHour("monday");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public String getEventPriceRange() {
        return eventPriceRange = yelpHTMLWrapper.getPriceRange();
    }


}
