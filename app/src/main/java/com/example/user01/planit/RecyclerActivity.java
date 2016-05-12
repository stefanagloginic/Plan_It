package com.example.user01.planit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecyclerActivity extends Activity {
    private RecyclerView morningRV;
    private RecyclerView afternoonRV;
    private RecyclerView eveningRV;

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
        morningRV.setHasFixedSize(true);
        afternoonRV = (RecyclerView) findViewById(R.id.rv2);
        afternoonRV.setHasFixedSize(true);
        eveningRV = (RecyclerView) findViewById(R.id.rv3);
        eveningRV.setHasFixedSize(true);

        CustomLayoutManager breakfastLayout = new CustomLayoutManager(this);
        CustomLayoutManager lunchLayout = new CustomLayoutManager(this);
        CustomLayoutManager dinnerLayout = new CustomLayoutManager(this);
        breakfastLayout.setRecycleChildrenOnDetach(true);
        lunchLayout.setRecycleChildrenOnDetach(true);
        dinnerLayout.setRecycleChildrenOnDetach(true);

        morningRV.setLayoutManager(breakfastLayout);
        afternoonRV.setLayoutManager(lunchLayout);
        eveningRV.setLayoutManager(dinnerLayout);

        initializeAdapter();
    }

    private void initializeAdapter(){
        ArrayList<Event> breakfastEvents = EventData.getMorningEvents();
        ArrayList<Event> lunchEvents = EventData.getAfternoonEvents();
        ArrayList<Event> dinnerEvents = EventData.getDinnerEvents();

        RVAdapter breakfastAdapter = new RVAdapter(breakfastEvents);
        RVAdapter lunchAdapter = new RVAdapter(lunchEvents);
        RVAdapter dinnerAdapter = new RVAdapter(dinnerEvents);

        ItemTouchHelper.Callback callback = new EventTouchHelper(breakfastAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(morningRV);

        callback = new EventTouchHelper(lunchAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(afternoonRV);

        callback = new EventTouchHelper(dinnerAdapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(eveningRV);

        morningRV.setAdapter(breakfastAdapter);
        afternoonRV.setAdapter(lunchAdapter);
        eveningRV.setAdapter(dinnerAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}