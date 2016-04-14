package com.example.user01.planit;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class YelpHTMLWrapper extends AsyncTask<Void, Void, String>{
    private Activity activity;
    private String businessURL;
    private ArrayList<String> hours;
    private Document doc;
    private static int NUMTIMES = 0;

    YelpHTMLWrapper(Activity activity, String businessURL) {
        this.activity = activity;
        this.businessURL = businessURL;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            doc = Jsoup.connect(businessURL).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            getHours();
            Log.i("info","getHours");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getHours() throws IOException {
        hours = new ArrayList<>();
        Element table = doc.select("table").first();
        Elements rows = table.select("tbody").select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements day = row.select("th");
            Elements time = row.select("td");
            hours.add(day.get(0).text() + " " + time.get(0).text());
        }
        NUMTIMES++;
        if (NUMTIMES > 4 ) {
            Button button = (Button) this.activity.findViewById(R.id.button);
            button.setBackgroundResource(android.R.drawable.btn_default);
            button.setEnabled(true);
            NUMTIMES = 0;
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
            return  6;
    }

    public String getPriceRange() {
        Element range = doc.select("dd.nowrap.price-description").first();
        return range.text();
    }
}
