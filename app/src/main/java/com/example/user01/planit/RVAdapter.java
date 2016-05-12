package com.example.user01.planit;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {
    private ArrayList<Event> events;
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView eventImage;
        TextView eventName;
        TextView eventAddress;
        TextView eventRating;
        TextView eventPriceRange;
        TextView eventHours;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventImage = (ImageView)itemView.findViewById(R.id.event_image);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);
            eventRating = (TextView)itemView.findViewById(R.id.event_rating);
//            eventPriceRange = (TextView)itemView.findViewById(R.id.event_price_range);
//            eventHours = (TextView)itemView.findViewById(R.id.event_hours);
        }
    }

    // Constructor for the RVAdapter class
    RVAdapter(ArrayList<Event> events){
        this.events = events;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return (new EventViewHolder(v));
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventImage.setImageBitmap(events.get(i).getEventBitmap());
        eventViewHolder.eventName.setText(events.get(i).getEventName());
        eventViewHolder.eventAddress.setText(events.get(i).getEventAddress());
        eventViewHolder.eventRating.setText(events.get(i).getEventRating());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void remove(int position) {
        events.remove(position);
        notifyItemRemoved(position);
    }

}

