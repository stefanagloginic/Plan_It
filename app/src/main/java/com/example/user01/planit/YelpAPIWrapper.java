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

import retrofit.Call;

public class YelpAPIWrapper extends AsyncTask<Void, Void, Void> {
    private Activity activity;
    private YelpAPI yelpAPI;
    private ArrayList<String> settings;
    private ArrayList<Event> breakfastEvents;
    private ArrayList<Event> lunchEvents;
    private ArrayList<Event> dinnerEvents;
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
        breakfastEvents = new ArrayList<>();
        lunchEvents = new ArrayList<>();
        dinnerEvents = new ArrayList<>();
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
            Map<String, String> mornparam = new HashMap<>();
            Map<String, String> noonparam = new HashMap<>();
            Map<String, String> evenparam = new HashMap<>();
            mornparam.put("term", "Breakfast");
            noonparam.put("term", "Lunch");
            evenparam.put("term", "Dinner");
            Call<SearchResponse> call;

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
                    for (int i = 0; i < 4; i++) {
                        int random = (int) (Math.random() * 20);
                        breakfastEvents.add(new YelpEvent(breakfast.get(random)));
                        lunchEvents.add(new YelpEvent(lunch.get(random)));
                        dinnerEvents.add(new YelpEvent(dinner.get(random)));
                    }
                    break;
            }
//            Retrofit client = new Retrofit
//                    .Builder()
//                    .baseUrl("http://api.eventful.com/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            EventfulAPI eventfulAPI = client.create(EventfulAPI.class);
//            Call<EventfulModel> eventfulModelCall = eventfulAPI.EventfulList();
//            ArrayList<EventfulEvent> eventfulEvents = eventfulModelCall.execute().body().getEvents().getEvent();
//            for (int i = 0; i < 3; i++) {
//                eventfulEvents.get(i).setEventVariables();
//                yelpEvents.add(eventfulEvents.get(i));
//            }
//            Log.i("info","eventName");
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
        intent.putParcelableArrayListExtra("breakfast", breakfastEvents);
        intent.putParcelableArrayListExtra("lunch", lunchEvents);
        intent.putParcelableArrayListExtra("dinner", dinnerEvents);

        activity.startActivity(intent);
        Log.i("info", "FUUUUCK");
        cpv.stopAnimation();
    }

}
