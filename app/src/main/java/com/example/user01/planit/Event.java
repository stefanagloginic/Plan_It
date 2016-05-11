package com.example.user01.planit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Event implements Parcelable{
    protected String eventName;
    protected String eventAddress;
    protected String eventRating;
    protected String eventURL;
    protected Bitmap eventBitmap;
    protected String eventPriceRange;
    protected String eventHours;

    public Event() {}

    public Event(Parcel in) {
        eventName = in.readString();
        eventAddress = in.readString();
        eventRating = in.readString();
        eventURL = in.readString();
        eventPriceRange = in.readString();
        eventBitmap = in.readParcelable(getClass().getClassLoader());
        eventHours = in.readString();
    }

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
    public String getEventPriceRange() { return eventPriceRange; }
    public String getEventHours() { return eventHours; }

    public static final Parcelable.Creator<Event> CREATOR =
            new Parcelable.Creator<Event>() {
                public Event createFromParcel(Parcel in) {
                    return new Event(in);
                }

                @Override
                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventAddress);
        dest.writeString(eventRating);
        dest.writeString(eventURL);
        dest.writeString(eventPriceRange);
        dest.writeParcelable(eventBitmap,flags);
        dest.writeString(eventHours);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
