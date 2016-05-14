package com.example.user01.planit;


import android.graphics.Bitmap;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class EventData {
    private static Bitmap bitmap;
    private static EventData ourInstance = new EventData();
    private static ArrayList<Event> morningEvents;
    private static ArrayList<Event> afternoonEvents;
    private static ArrayList<Event> eveningEvents;
    private static ArrayList<Business> morningRestaurants;
    private static ArrayList<Business> afternoonRestaurants;
    private static ArrayList<Business> eveningRestaurants;

    public static void setBitmap(Bitmap bitmap) {
        EventData.bitmap = bitmap;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static ArrayList<Event> getMorningEvents() {
        return morningEvents;
    }

    public static ArrayList<Event> getAfternoonEvents() {
        return afternoonEvents;
    }

    public static ArrayList<Event> getDinnerEvents() {
        return eveningEvents;
    }

    public static void setMorningEvents(ArrayList<Event> morningEvents) {
        EventData.morningEvents = morningEvents;
    }

    public static void setAfternoonEvents(ArrayList<Event> afternoonEvents) {
        EventData.afternoonEvents = afternoonEvents;
    }

    public static void setDinnerEvents(ArrayList<Event> dinnerEvents) {
        EventData.eveningEvents = dinnerEvents;
    }

    public static void setMorningRestaurant(ArrayList<Business> breakfastRestaurants) {
        EventData.morningRestaurants = breakfastRestaurants;
    }

    public static void setAfternoonRestaurant(ArrayList<Business> lunchRestaurants) {
        EventData.afternoonRestaurants = lunchRestaurants;
    }

    public static void setEveningRestaurant(ArrayList<Business> dinnerRestaurants) {
        EventData.eveningRestaurants = dinnerRestaurants;
    }

    public static ArrayList<Business> getMorningRestaurants() {
        return morningRestaurants;
    }

    public static ArrayList<Business> getAfternoonRestaurants() {
        return afternoonRestaurants;
    }

    public static ArrayList<Business> getEveningRestaurants() {
        return eveningRestaurants;
    }

    public static EventData getInstance() {
        return ourInstance;
    }

    private EventData() {
    }
}
