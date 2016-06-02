package com.example.user01.planit;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class HikeRVAdapter extends RecyclerView.Adapter<HikeRVAdapter.EventViewHolder> {
    private ArrayList<Business> hikes;
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView eventImage;
        TextView eventName;
        TextView eventAddress;
        TextView eventRating;


        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventImage = (ImageView)itemView.findViewById(R.id.event_image);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);
            eventRating = (TextView)itemView.findViewById(R.id.event_rating);
        }
    }

    // Constructor for the RVAdapter class
    HikeRVAdapter(ArrayList<Business> events){
        this.hikes = events;
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
        eventViewHolder.eventImage.setImageBitmap(EventData.getBitmap().get(1));
        eventViewHolder.eventName.setText(hikes.get(i).name());

        eventViewHolder.eventAddress.setText(hikes.get(i).location().address().get(0) + ", " + hikes.get(i).location().city());
        eventViewHolder.eventRating.setText("Rating: "+String.valueOf(hikes.get(i).rating()) + " | " +
                String.valueOf(hikes.get(i).reviewCount()) + " Ratings");
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public void remove(int position) {
        hikes.remove(position);
        notifyItemRemoved(position);
    }

}

