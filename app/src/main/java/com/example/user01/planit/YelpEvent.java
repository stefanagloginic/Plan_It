package com.example.user01.planit;

import com.yelp.clientlib.entities.Business;

import java.io.IOException;


public class YelpEvent extends Event {
    private String businessHours;
    private YelpHTMLScraper yelpHTMLScraper;

    public YelpEvent(Business b) {
        this.eventName = b.name();
        this.eventAddress = b.location().address().get(0);
        this.eventRating = String.valueOf(b.rating());
        this.eventURL = b.url();
        yelpHTMLScraper = new YelpHTMLScraper(eventURL);
        this.eventPriceRange = yelpHTMLScraper.getPriceRange();
    }

    public String getBusinessHours() {
        try {
            businessHours = yelpHTMLScraper.getDailyHour("monday");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessHours;
    }

}
