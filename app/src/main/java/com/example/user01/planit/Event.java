package com.example.user01.planit;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Event implements Parcelable{

    protected String eventName;
    protected String eventAddress;
    protected String eventRating;
    protected String eventURL;
    protected String eventPriceRange;

    public Event() {}

    public Event(Parcel in) {
        eventName = in.readString();
        eventAddress = in.readString();
        eventRating = in.readString();
        eventURL = in.readString();
        eventPriceRange = in.readString();
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

    public String getEventPriceRange() { return eventPriceRange; }

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
        Log.i("info","Write Parcel");
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
