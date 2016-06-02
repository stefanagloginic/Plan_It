package com.example.user01.planit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecyclerActivity extends Activity {
    private RecyclerView morningRV;
    private RecyclerView afternoonRV;
    private RecyclerView eveningRV;
    private RecyclerView eventfulRV;
    private RecyclerView movieRV;
    private RecyclerView hikeRV;
    private RecyclerView museumRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/RobotoL.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        morningRV = (RecyclerView) findViewById(R.id.rv);
        afternoonRV = (RecyclerView) findViewById(R.id.rv2);
        eveningRV = (RecyclerView) findViewById(R.id.rv3);
        eventfulRV = (RecyclerView) findViewById(R.id.rv4);
        movieRV = (RecyclerView) findViewById(R.id.rv5);
        hikeRV = (RecyclerView) findViewById(R.id.rv6);
        museumRV = (RecyclerView) findViewById(R.id.museums);

        CustomLayoutManager breakfastLayout = new CustomLayoutManager(this);
        CustomLayoutManager lunchLayout = new CustomLayoutManager(this);
        CustomLayoutManager dinnerLayout = new CustomLayoutManager(this);
        CustomLayoutManager eventfulLayout = new CustomLayoutManager(this);
        CustomLayoutManager movieLayout = new CustomLayoutManager(this);
        CustomLayoutManager hikeLayout = new CustomLayoutManager(this);
        CustomLayoutManager museumLayout = new CustomLayoutManager(this);

        breakfastLayout.setRecycleChildrenOnDetach(true);
        lunchLayout.setRecycleChildrenOnDetach(true);
        dinnerLayout.setRecycleChildrenOnDetach(true);
        eventfulLayout.setRecycleChildrenOnDetach(true);
        movieLayout.setRecycleChildrenOnDetach(true);
        hikeLayout.setRecycleChildrenOnDetach(true);
        museumLayout.setRecycleChildrenOnDetach(true);

        morningRV.setLayoutManager(breakfastLayout);
        afternoonRV.setLayoutManager(lunchLayout);
        eveningRV.setLayoutManager(dinnerLayout);
        eventfulRV.setLayoutManager(eventfulLayout);
        movieRV.setLayoutManager(movieLayout);
        hikeRV.setLayoutManager(hikeLayout);
        museumRV.setLayoutManager(museumLayout);

        initializeAdapter();
    }

    private void initializeAdapter(){
        ArrayList<Business> breakfastEvents = EventData.getMorningRestaurants();
        ArrayList<Business> lunchEvents = EventData.getAfternoonRestaurants();
        ArrayList<Business> dinnerEvents = EventData.getEveningRestaurants();
        ArrayList<EventfulEvent> eventfulEvents = EventData.getEvents();
        ArrayList<Movie> movies = EventData.getMovies();
        ArrayList<Business> hikeEvents = EventData.getHikes();
        ArrayList<Business> museumEvents = EventData.getMuseums();

        RVAdapter breakfastAdapter = new RVAdapter(breakfastEvents);
        RVAdapter lunchAdapter = new RVAdapter(lunchEvents);
        RVAdapter dinnerAdapter = new RVAdapter(dinnerEvents);
        EventfulRVAdapter eventfulRVAdapter = new EventfulRVAdapter(eventfulEvents);
        MovieRVAdapter movieRVAdapter = new MovieRVAdapter(movies);
        HikeRVAdapter hikeAdapter = new HikeRVAdapter(hikeEvents);
        MuseumRVAdapter museumRVAdapter = new MuseumRVAdapter(museumEvents);

        ItemTouchHelper.Callback callback = new EventTouchHelper(breakfastAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(morningRV);

        callback = new EventTouchHelper(lunchAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(afternoonRV);

        callback = new EventTouchHelper(dinnerAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(eveningRV);

        callback = new EventTouchHelper(eventfulRVAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(eventfulRV);

        callback = new EventTouchHelper(movieRVAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(movieRV);

        callback = new EventTouchHelper(hikeAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(hikeRV);

        callback = new EventTouchHelper(museumRVAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(museumRV);

        morningRV.setAdapter(breakfastAdapter);
        afternoonRV.setAdapter(lunchAdapter);
        eveningRV.setAdapter(dinnerAdapter);
        eventfulRV.setAdapter(eventfulRVAdapter);
        movieRV.setAdapter(movieRVAdapter);
        hikeRV.setAdapter(hikeAdapter);
        museumRV.setAdapter(museumRVAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}