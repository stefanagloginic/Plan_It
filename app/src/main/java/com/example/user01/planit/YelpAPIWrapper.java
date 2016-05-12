package com.example.user01.planit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpAPIWrapper extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private YelpAPI yelpAPI;
    private ArrayList<String> settings;
    private ArrayList<Event> breakfastEvents;
    private ArrayList<Event> lunchEvents;
    private ArrayList<Event> dinnerEvents;
    private CircularProgressView cpv;

    YelpAPIWrapper(Activity activity, ArrayList<String> settings) {
        this.activity = activity;
        this.settings = settings;
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                activity.getString(R.string.consumer_key),
                        activity.getString(R.string.consumer_secret),
                        activity.getString(R.string.token),
                        activity.getString(R.string.token_secret));
        yelpAPI = yelpAPIFactory.createAPI();
        breakfastEvents = new ArrayList<>();
        lunchEvents = new ArrayList<>();
        dinnerEvents = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        cpv = (CircularProgressView) this.activity.findViewById(R.id.progress_circle);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Map<String, String> mornparam = new HashMap<>();
            Map<String, String> noonparam = new HashMap<>();
            Map<String, String> evenparam = new HashMap<>();
            mornparam.put("term", "Breakfast");
            noonparam.put("term", "Lunch");
            evenparam.put("term", "Dinner");
            Call<SearchResponse> call;

            Bitmap bitmap = BitmapFactory.decodeResource(
                    this.activity.getResources(),R.drawable.food);
            bitmap = Bitmap.createScaledBitmap(bitmap,200,200,false);

            ArrayList<Business> breakfast, lunch, dinner;
            switch(settings.get(1)) {
                case "Morning":
                    call = yelpAPI.search(settings.get(0), mornparam);
                    SearchResponse morningResponse = call.execute().body();
                    breakfast = morningResponse.businesses();
                    breakfastEvents.add(new YelpEvent(breakfast.get((int) (Math.random() * 20))));
                    break;
                case "Afternoon":
                    call = yelpAPI.search(settings.get(0), noonparam);
                    SearchResponse noonResponse = call.execute().body();
                    lunch = noonResponse.businesses();
                    lunchEvents.add(new YelpEvent(lunch.get((int) (Math.random() * 20))));
                    break;
                case "Evening":
                    call = yelpAPI.search(settings.get(0), evenparam);
                    SearchResponse eveningResponse = call.execute().body();
                    dinner = eveningResponse.businesses();
                    dinnerEvents.add(new YelpEvent(dinner.get((int) (Math.random() * 20))));
                    break;
                default:
                    call = yelpAPI.search(settings.get(0), mornparam);
                    morningResponse = call.execute().body();

                    call = yelpAPI.search(settings.get(0), noonparam);
                    noonResponse = call.execute().body();

                    call = yelpAPI.search(settings.get(0), evenparam);
                    eveningResponse = call.execute().body();

                    breakfast = morningResponse.businesses();
                    lunch = noonResponse.businesses();
                    dinner = eveningResponse.businesses();
                    for (int i = 0; i < 1; i++) {
                        int random = (int) (Math.random() * 20);
                        breakfastEvents.add(new YelpEvent(breakfast.get(random)));
                        lunchEvents.add(new YelpEvent(lunch.get(random)));
                        dinnerEvents.add(new YelpEvent(dinner.get(random)));
                    }
                    break;
            }
            Retrofit client = new Retrofit
                    .Builder()
                    .baseUrl("http://api.eventful.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            EventfulAPI eventfulAPI = client.create(EventfulAPI.class);
            retrofit2.Call<EventfulModel> eventfulModelCall = eventfulAPI.EventfulList("music");
            ArrayList<EventfulEvent> eventfulEvents = eventfulModelCall.execute().body().getEvents().getEvent();
            for (int i = 0; i < 1; i++) {
                eventfulEvents.get(i).setEventVariables(bitmap);
                breakfastEvents.add(eventfulEvents.get(i));
            }
            EventData.setBreakfastEvents(breakfastEvents);
            EventData.setLunchEvents(lunchEvents);
            EventData.setDinnerEvents(dinnerEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent intent = new Intent(activity,RecyclerActivity.class);

        activity.startActivity(intent);
        cpv.stopAnimation();
    }

}
