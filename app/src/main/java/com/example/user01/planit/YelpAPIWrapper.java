package com.example.user01.planit;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;

public class YelpAPIWrapper extends AsyncTask<Void, String, ArrayList<String>> {
    private Activity activity;
    private String  location;
    private YelpAPI yelpAPI;
    private ArrayList<Business> businesses;
    private ArrayList<Event> yelpEvents;

    YelpAPIWrapper(Activity activity, String location) {
        this.activity = activity;
        this.location = location;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory
                (this.activity.getString(R.string.consumer_key),
                        this.activity.getString(R.string.consumer_secret),
                        this.activity.getString(R.string.token),
                        this.activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        View b = this.activity.findViewById(R.id.button);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setEnabled(false);
    }
    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        try {
            Log.i("info", "DoInBackground");
            Map<String, String> param = new HashMap<>();
            Call<SearchResponse> call = yelpAPI.search(this.location, param);

            SearchResponse searchResponse = call.execute().body();
            Log.i("info", "Start adding stuff");
            businesses = searchResponse.businesses();
            Log.i("info", "Finished adding stuff");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        yelpEvents = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yelpEvents.add(new YelpEvent(activity,businesses.get(i)));
        }
    }

    public ArrayList<Event> getYelpEvents(){
        return yelpEvents;
    }
}
