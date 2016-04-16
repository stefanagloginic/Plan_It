package com.example.user01.planit;

import android.content.Context;
import android.content.Intent;
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

public class YelpAPIWrapper extends AsyncTask<Void, Void, Void> {
    private Context context;
    private YelpAPI yelpAPI;
    private ArrayList<String> settings;
    private ArrayList<Business> businesses;
    private ArrayList<Event> yelpEvents;

    YelpAPIWrapper(Context context, ArrayList<String> settings) {
        this.context = context;
        this.settings = settings;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                this.context.getString(R.string.consumer_key),
                        this.context.getString(R.string.consumer_secret),
                        this.context.getString(R.string.token),
                        this.context.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        yelpEvents = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Map<String, String> param = new HashMap<>();

            Call<SearchResponse> call = yelpAPI.search(settings.get(0), param);

            SearchResponse searchResponse = call.execute().body();
            businesses = searchResponse.businesses();

            int randomNum = (int) (Math.random() * 20);
            yelpEvents.add(new YelpEvent(businesses.get(randomNum)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("info", "After while loop");
        Intent intent = new Intent(context,RecyclerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra("events", yelpEvents);
        context.startActivity(intent);
    }

}