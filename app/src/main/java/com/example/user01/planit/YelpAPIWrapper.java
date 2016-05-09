package com.example.user01.planit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpAPIWrapper extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private YelpAPI yelpAPI;
    private ArrayList<String> settings;
    private ArrayList<Business> businesses;
    private ArrayList<Event> yelpEvents;
    private ProgressDialog dialog;
    private CircularProgressView cpv;

    YelpAPIWrapper(Activity activity, ArrayList<String> settings) {
        this.activity = activity;
        this.settings = settings;
        dialog = new ProgressDialog(activity);
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                activity.getString(R.string.consumer_key),
                        activity.getString(R.string.consumer_secret),
                        activity.getString(R.string.token),
                        activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        yelpEvents = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        cpv = (CircularProgressView) this.activity.findViewById(R.id.progress_circle);
//        this.dialog.setMessage("Loading your day");
//        this.dialog.setCanceledOnTouchOutside(false);
//        this.dialog.setCancelable(false);
//        this.dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Map<String, String> param = new HashMap<>();
            switch(settings.get(1)) {
                case "Morning":
                    param.put("term","Breakfast");
                    break;
                case "Afternoon":
                    param.put("term","Lunch");
                    break;
                case "Evening":
                    param.put("term","Dinner");
                    break;
                default:
                    break;
            }

            retrofit.Call<SearchResponse> searchResponseCall = yelpAPI.search(settings.get(0), param);
            SearchResponse searchResponse = searchResponseCall.execute().body();
            businesses = searchResponse.businesses();

            if (settings.get(1).equals("All Day")) {
                for (int i = 0; i < 3; i++) {
                    yelpEvents.add(new YelpEvent(businesses.get((int) (Math.random() * 20))));
                }
            } else {
                yelpEvents.add(new YelpEvent(businesses.get((int) (Math.random() * 20))));
            }
            Retrofit client = new Retrofit
                    .Builder()
                    .baseUrl("http://api.eventful.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            EventfulAPI eventfulAPI = client.create(EventfulAPI.class);
            Call<EventfulModel> eventfulModelCall = eventfulAPI.EventfulList();
            ArrayList<EventfulEvent> eventfulEvents = eventfulModelCall.execute().body().getEvents().getEvent();
            for (int i = 0; i < 3; i++) {
                eventfulEvents.get(i).setEventVariables();
                yelpEvents.add(eventfulEvents.get(i));
            }
            Log.i("info","eventName");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("info", "After while loop");
        Intent intent = new Intent(activity,RecyclerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra("events", yelpEvents);

//       if (dialog.isShowing()) {
//            dialog.dismiss();
//        }

        activity.startActivity(intent);
        cpv.stopAnimation();
    }

}
