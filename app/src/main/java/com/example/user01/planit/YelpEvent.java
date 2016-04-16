package com.example.user01.planit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yelp.clientlib.entities.Business;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class YelpEvent extends Event {
    private String businessHours;
    private YelpHTMLScraper yelpHTMLScraper;

    public YelpEvent(Business b) {
        this.eventName = b.name();
        this.eventAddress = b.location().address().get(0) + ", " + b.location().city();
        this.eventRating = "Rating: "+String.valueOf(b.rating()) + " | " +
                String.valueOf(b.reviewCount()) + " Ratings";
        this.eventURL = b.url();
        this.eventBitmap = LoadImageFromWeb(b.imageUrl());
        yelpHTMLScraper = new YelpHTMLScraper(eventURL);
        this.eventPriceRange = yelpHTMLScraper.getPriceRange();
    }

    public String getBusinessHours() {
        try {
            businessHours = yelpHTMLScraper.getDailyHour("monday");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public static Bitmap LoadImageFromWeb(String url) {
        try {
            URL urlImage = new URL(url);
            Log.i("IMAGE LOADED", url);
            return BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            Log.i("MalformedURLException", url);
            return null;
        } catch (IOException e) {
            Log.i("IOException", url);
            return null;
        }
    }

}
