package com.example.user01.planit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class RecyclerActivity extends Activity {
    private RecyclerView breakfastRV;
    private RecyclerView lunchRV;
    private RecyclerView dinnerRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/plan.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        breakfastRV = (RecyclerView) findViewById(R.id.rv);
        breakfastRV.setHasFixedSize(true);
        lunchRV = (RecyclerView) findViewById(R.id.rv2);
        lunchRV.setHasFixedSize(true);
        dinnerRV = (RecyclerView) findViewById(R.id.rv3);
        dinnerRV.setHasFixedSize(true);

        CustomLayoutManager breakfastLayout = new CustomLayoutManager(this);
        CustomLayoutManager lunchLayout = new CustomLayoutManager(this);
        CustomLayoutManager dinnerLayout = new CustomLayoutManager(this);
        breakfastLayout.setRecycleChildrenOnDetach(true);
        lunchLayout.setRecycleChildrenOnDetach(true);
        dinnerLayout.setRecycleChildrenOnDetach(true);


//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setRecycleChildrenOnDetach(true);
//        LinearLayoutManager llm2 = new LinearLayoutManager(this);
//        llm2.setRecycleChildrenOnDetach(true);
//        LinearLayoutManager llm3 = new LinearLayoutManager(this);
//        llm3.setRecycleChildrenOnDetach(true);

        breakfastRV.setLayoutManager(breakfastLayout);
        lunchRV.setLayoutManager(lunchLayout);
        dinnerRV.setLayoutManager(dinnerLayout);

        initializeAdapter();
    }

    private void initializeAdapter(){
        ArrayList<Event> breakfastEvents = EventData.getBreakfastEvents();
        ArrayList<Event> lunchEvents = EventData.getLunchEvents();
        ArrayList<Event> dinnerEvents = EventData.getDinnerEvents();

        RVAdapter breakfastAdapter = new RVAdapter(breakfastEvents);
        RVAdapter lunchAdapter = new RVAdapter(lunchEvents);
        RVAdapter dinnerAdapter = new RVAdapter(dinnerEvents);

//        ItemTouchHelper.Callback breakfastCallback = new EventTouchHelper(breakfastAdapter);
//        ItemTouchHelper breakfastHelper = new ItemTouchHelper(breakfastCallback);
//        breakfastHelper.attachToRecyclerView(breakfastRV);
//
//        ItemTouchHelper.Callback lunchCallback = new EventTouchHelper(lunchAdapter);
//        ItemTouchHelper lunchHelper = new ItemTouchHelper(lunchCallback);
//        lunchHelper.attachToRecyclerView(lunchRV);
//
//        ItemTouchHelper.Callback dinnerCallback = new EventTouchHelper(dinnerAdapter);
//        ItemTouchHelper dinnerHelper = new ItemTouchHelper(dinnerCallback);
//        dinnerHelper.attachToRecyclerView(dinnerRV);

        ItemTouchHelper.Callback callback = new EventTouchHelper(breakfastAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(breakfastRV);

        callback = new EventTouchHelper(lunchAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(lunchRV);

        callback = new EventTouchHelper(dinnerAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(dinnerRV);

        breakfastRV.setAdapter(breakfastAdapter);
        lunchRV.setAdapter(lunchAdapter);
        dinnerRV.setAdapter(dinnerAdapter);
    }

}