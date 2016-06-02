package com.example.user01.planit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {
    private ArrayList<Business> businesses;
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView eventImage;
        TextView eventName;
        TextView eventAddress;
        TextView eventRating;
        String url;


        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventImage = (ImageView)itemView.findViewById(R.id.event_image);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);
            eventRating = (TextView)itemView.findViewById(R.id.event_rating);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Loading Website", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    // Constructor for the RVAdapter class
    RVAdapter(ArrayList<Business> events){
        this.businesses = events;
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
        eventViewHolder.eventImage.setImageBitmap(EventData.getBitmap().get(0));
        eventViewHolder.eventName.setText(businesses.get(i).name());
        eventViewHolder.eventAddress.setText(businesses.get(i).location().address().get(0) + ", " + businesses.get(i).location().city());
        eventViewHolder.eventRating.setText("Rating: "+String.valueOf(businesses.get(i).rating()) + " | " +
                String.valueOf(businesses.get(i).reviewCount()) + " Ratings");
        eventViewHolder.url = businesses.get(i).url();
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public void remove(int position) {
        businesses.remove(position);
        notifyItemRemoved(position);
    }

}

