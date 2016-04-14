package com.example.user01.planit;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {
    private ArrayList<Event> events;
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView eventName;
        TextView eventAddress;
        TextView eventRating;
        TextView eventURL;
        TextView eventPriceRange;
        ImageView personPhoto;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);
            eventRating = (TextView)itemView.findViewById(R.id.event_rating);
            eventPriceRange = (TextView)itemView.findViewById(R.id.event_price_range);
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
        v.setPadding(20,20,20,20);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder personViewHolder, int i) {
        personViewHolder.eventName.setText(events.get(i).getEventName());
        personViewHolder.eventAddress.setText(events.get(i).getEventAddress());
        personViewHolder.eventRating.setText(events.get(i).getEventRating());
//        personViewHolder.eventPriceRange.setText(events.get(i).getEventPriceRange());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

