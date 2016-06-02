package com.example.user01.planit;


import android.graphics.Bitmap;

import com.yelp.clientlib.entities.Business;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EventData {
    private static ArrayList<Bitmap> bitmaps;
    private static EventData ourInstance = new EventData();
    private static ArrayList<EventfulEvent> Events;
    private static ArrayList<Movie> movies;
    private static ArrayList<Business> morningRestaurants;
    private static ArrayList<Business> afternoonRestaurants;
    private static ArrayList<Business> eveningRestaurants;
    private static ArrayList<Business> hikes;
    private static ArrayList<Business> museums;

    public static void setMovies(ArrayList<Movie> movies) { EventData.movies = movies; }

    public static ArrayList<Movie> getMovies() { return EventData.movies; }

    public static void setBitmap(ArrayList<Bitmap> bitmap) {
        EventData.bitmaps = bitmap;
    }

    public static ArrayList<Bitmap> getBitmap() {
        return bitmaps;
    }

    public static ArrayList<EventfulEvent> getEvents() {
        return EventData.Events;
    }

    public static void setEvents(ArrayList<EventfulEvent> events) {
        Events = events;
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

    public static void setHikes(ArrayList<Business> hikes) {
        EventData.hikes = hikes;
    }

    public static void setMuseums(ArrayList<Business> museums) {
        EventData.museums = museums; }

    public static ArrayList<Business> getMorningRestaurants() {
        return morningRestaurants;
    }

    public static ArrayList<Business> getAfternoonRestaurants() {
        return afternoonRestaurants;
    }

    public static ArrayList<Business> getEveningRestaurants() {
        return eveningRestaurants;
    }

    public static ArrayList<Business> getHikes() { return hikes; }

    public static ArrayList<Business> getMuseums() {return museums; }

    public static EventData getInstance() {
        return ourInstance;
    }

    private EventData() {
    }
}
