package com.example.user01.planit;

import java.util.ArrayList;

public class EventData {

    private static EventData ourInstance = new EventData();
    private static ArrayList<Event> morningEvents;
    private static ArrayList<Event> afternoonEvents;
    private static ArrayList<Event> eveningEvents;

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

    public static EventData getInstance() {
        return ourInstance;
    }

    private EventData() {
    }
}
