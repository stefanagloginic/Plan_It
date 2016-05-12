package com.example.user01.planit;

import java.util.ArrayList;

/**
 * Created by Jimmyle on 5/11/16.
 */
public class EventData {

    private static EventData ourInstance = new EventData();
    private static ArrayList<Event> breakfastEvents;
    private static ArrayList<Event> lunchEvents;
    private static ArrayList<Event> dinnerEvents;

    public static ArrayList<Event> getBreakfastEvents() {
        return breakfastEvents;
    }

    public static ArrayList<Event> getLunchEvents() {
        return lunchEvents;
    }

    public static ArrayList<Event> getDinnerEvents() {
        return dinnerEvents;
    }

    public static void setBreakfastEvents(ArrayList<Event> breakfastEvents) {
        EventData.breakfastEvents = breakfastEvents;
    }

    public static void setLunchEvents(ArrayList<Event> lunchEvents) {
        EventData.lunchEvents = lunchEvents;
    }

    public static void setDinnerEvents(ArrayList<Event> dinnerEvents) {
        EventData.dinnerEvents = dinnerEvents;
    }

    public static EventData getInstance() {
        return ourInstance;
    }

    private EventData() {
    }
}
