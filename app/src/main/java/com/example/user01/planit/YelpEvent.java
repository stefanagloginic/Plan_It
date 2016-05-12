package com.example.user01.planit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yelp.clientlib.entities.Business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


public class YelpEvent extends Event {
    private String businessHours;
    private YelpHTMLScraper yelpHTMLScraper;
    private Activity activity;

    public YelpEvent(Business b, Activity a) {
        this.activity = a;
        this.eventName = b.name();
        this.eventAddress = b.location().address().get(0) + ", " + b.location().city();
        this.eventRating = "Rating: "+String.valueOf(b.rating()) + " | " +
                String.valueOf(b.reviewCount()) + " Ratings";
        this.eventURL = b.url();
//        this.eventBitmap = LoadImageFromWeb(b.imageUrl());
        this.eventBitmap = loadFoodIcon();
        yelpHTMLScraper = new YelpHTMLScraper(eventURL);
        this.eventPriceRange = yelpHTMLScraper.getPriceRange();
        this.eventHours = getBusinessHours();
    }

    public String getBusinessHours() {
        String businessHours = "";
        Calendar c = Calendar.getInstance();
        int dayOfWeekInt = c.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeekInt) {
            case (1):
                dailyHourScraperCall("Sunday");
            case (2):
                dailyHourScraperCall("Monday");
            case (3):
                dailyHourScraperCall("Tuesday");
            case (4):
                dailyHourScraperCall("Wednesday");
            case (5):
                dailyHourScraperCall("Thursday");
            case (6):
                dailyHourScraperCall("Friday");
            case (7):
                dailyHourScraperCall("Saturday");
        }
        return businessHours;
    }

    public String dailyHourScraperCall(String dayOfWeek) {
        try {
            businessHours = yelpHTMLScraper.getDailyHour(dayOfWeek);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public Bitmap loadFoodIcon() {
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.yelp_pin);
        bitmap = Bitmap.createScaledBitmap(bitmap, 200,200, false);
        return bitmap;
    }



}
