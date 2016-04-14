package com.example.user01.planit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;


public class RecyclerActivity extends Activity {
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        initializeAdapter();
    }

    private void initializeAdapter(){
        ArrayList<Event> events = getIntent().getParcelableArrayListExtra("events");
        Log.i("info", "get Event");
        RVAdapter adapter = new RVAdapter(events);
        rv.setAdapter(adapter);
    }

}
