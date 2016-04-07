package com.example.user01.planit;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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
    private YelpAPI yelpAPI;
    private ArrayList<Business> businesses;
    private ArrayList<String> info;

    YelpAPIWrapper(Activity activity) {
        this.activity = activity;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory
                (this.activity.getString(R.string.consumer_key),
                        this.activity.getString(R.string.consumer_secret),
                        this.activity.getString(R.string.token),
                        this.activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        info = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        try {
            Map<String, String> param = new HashMap<>();
            Call<SearchResponse> call = yelpAPI.search("Goleta", param);

            SearchResponse searchResponse = call.execute().body();
            Log.i("info", "Start adding stuff");
            businesses = searchResponse.businesses();
            info.add(businesses.get(0).name());
            info.add(businesses.get(0).location().address().get(0));
            info.add(String.valueOf(businesses.get(0).rating()));
            info.add(businesses.get(0).url());
            Log.i("info", "Finished adding stuff");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        super.onPostExecute(s);
        Log.i("info", "PostExecute");
    }

    public String getBusinessName() {
        return info.get(0);
    }
    public String getBusinessAddress() {
        return info.get(1);
    }

    public String getBusinessRating() {
        return info.get(2);
    }

    public String getBusinessURL() {
        return info.get(3);
    }

}
