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
    private ArrayList<String> info;

    YelpAPIWrapper(Activity activity, String location) {
        this.activity = activity;
        this.location = location;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory
                (this.activity.getString(R.string.consumer_key),
                        this.activity.getString(R.string.consumer_secret),
                        this.activity.getString(R.string.token),
                        this.activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        info = new ArrayList<>();
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
            Map<String, String> param = new HashMap<>();
            Call<SearchResponse> call = yelpAPI.search(this.location, param);

            SearchResponse searchResponse = call.execute().body();
            Log.i("info", "Start adding stuff");
            businesses = searchResponse.businesses();
            info.add(businesses.get(10).name());
            /*System.out.println(businesses.get(10).name());
            info.add(businesses.get(10).location().address().get(0));
            info.add(String.valueOf(businesses.get(10).rating()));
            info.add(businesses.get(10).url());*/
            Log.i("info", "Finished adding stuff");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {

        View b = this.activity.findViewById(R.id.button);
        b.setBackgroundResource(android.R.drawable.btn_default);
        b.setEnabled(true);
        Log.i("info", "PostExecute");
    }

   /* public String getBusinessName() {
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
    }*/

    public ArrayList<Business> getBusinesses(){
        return  businesses;
    }
}
