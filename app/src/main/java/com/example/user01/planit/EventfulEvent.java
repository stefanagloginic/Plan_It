package com.example.user01.planit;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventfulEvent extends Event {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("venue_address")
    @Expose
    private String venueAddress;

    public void setEventVariables(Bitmap bitmap) {
        this.eventName = title;
        this.eventAddress = venueAddress + " " + cityName;
        this.eventRating = "N/A";
        this.eventURL = url;
        this.eventPriceRange = "N/A";
        this.eventBitmap = bitmap;
}

}