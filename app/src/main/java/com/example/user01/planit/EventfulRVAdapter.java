package com.example.user01.planit;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventfulRVAdapter extends RecyclerView.Adapter<EventfulRVAdapter.EventViewHolder> {

    private ArrayList<EventfulEvent> events;

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView eventImage;
        TextView eventName;
        TextView eventAddress;
        TextView eventUrl;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventImage = (ImageView)itemView.findViewById(R.id.event_image);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);
            eventUrl = (TextView)itemView.findViewById(R.id.event_url);
        }
    }

    EventfulRVAdapter(ArrayList<EventfulEvent> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        return (new EventViewHolder(v));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int i) {
        holder.eventImage.setImageBitmap(EventData.getBitmap().get(1));
        holder.eventName.setText(events.get(i).getEventName());
        holder.eventAddress.setText(events.get(i).getEventAddress());
        holder.eventUrl.setText(events.get(i).getEventURL());
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
