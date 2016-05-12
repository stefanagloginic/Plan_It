package com.example.user01.planit;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class YelpHTMLScraper {
    private String businessURL;
    private ArrayList<String> hours;
    private Document doc;
    private String priceRange;

    YelpHTMLScraper(String businessURL) {
        this.businessURL = businessURL;
        try {
            doc = Jsoup.connect(businessURL).userAgent("Mozilla").get();
            getHours();
            //priceRange = doc.select("dd.nowrap.price-description").first().text();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getHours() throws IOException {
        hours = new ArrayList<>();
        Element table = doc.select("table").first();
        Elements rows = table.select("tbody").select("tr");

        for (int i = 0; i < rows.size(); i++) {
            if (rows.size() != 0) {
                Element row = rows.get(i);
                Elements day = row.select("th");
                Elements time = row.select("td");
                if ((day.size() > 0) && (time.size() > 0)) {
                    hours.add(day.get(0).text() + " " + time.get(0).text());
                } else {
                    hours.add("N/A");
                }
            }
        }
    }

    public String getDailyHour(String dayOfWeek) throws IOException {
        int dayNum = convertDayToInt(dayOfWeek);
        return hours.get(dayNum);
    }

    private int convertDayToInt(String dayOfWeek) {
        if (dayOfWeek.toLowerCase().contains("mon"))
            return 0;
        else if (dayOfWeek.toLowerCase().contains("tues"))
            return 1;
        else if (dayOfWeek.toLowerCase().contains("wednes"))
            return 2;
        else if (dayOfWeek.toLowerCase().contains("thurs"))
            return 3;
        else if (dayOfWeek.toLowerCase().contains("fri"))
            return 4;
        else if (dayOfWeek.toLowerCase().contains("satur"))
            return 5;
        else
            return 6;
    }

    public String getPriceRange() {
        return priceRange;
    }
}
