package com.example.user01.planit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Events {

    @SerializedName("event")
    @Expose
    private ArrayList<EventfulEvent> event = new ArrayList<EventfulEvent>();

    public ArrayList<EventfulEvent> getEvent() {
        return event;
    }
}