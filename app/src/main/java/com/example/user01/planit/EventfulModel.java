package com.example.user01.planit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventfulModel {

    @SerializedName("last_item")
    @Expose
    private Object lastItem;
    @SerializedName("total_items")
    @Expose
    private String totalItems;
    @SerializedName("first_item")
    @Expose
    private Object firstItem;
    @SerializedName("page_number")
    @Expose
    private String pageNumber;
    @SerializedName("page_size")
    @Expose
    private String pageSize;
    @SerializedName("page_items")
    @Expose
    private Object pageItems;
    @SerializedName("search_time")
    @Expose
    private String searchTime;
    @SerializedName("page_count")
    @Expose
    private String pageCount;
    @SerializedName("events")
    @Expose
    private Events events;

    public Events getEvents() {
        return events;
    }

}