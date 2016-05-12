package com.example.user01.planit;

import android.graphics.Bitmap;

public class Event {
    protected String eventName;
    protected String eventAddress;
    protected String eventRating;
    protected String eventURL;
    protected Bitmap eventBitmap;
    protected String eventPriceRange;
    protected String eventHours;

    public Event() {}

    public String getEventName() {
        return eventName;
    }
    public String getEventAddress() {
        return eventAddress;
    }
    public String getEventRating() {
        return eventRating;
    }
    public String getEventURL() { return eventURL; }

    public Bitmap getEventBitmap() {return eventBitmap; }


}
