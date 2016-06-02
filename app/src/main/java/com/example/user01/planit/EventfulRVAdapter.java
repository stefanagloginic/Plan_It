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

import java.util.ArrayList;

public class EventfulRVAdapter extends RecyclerView.Adapter<EventfulRVAdapter.EventViewHolder> {

    private ArrayList<EventfulEvent> events;

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView eventImage;
        TextView eventName;
        TextView eventAddress;
        String eventUrl;

        public EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventImage = (ImageView)itemView.findViewById(R.id.event_image);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventAddress = (TextView)itemView.findViewById(R.id.event_address);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Going to web page", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(eventUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);
                }
            });
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
        holder.eventImage.setImageBitmap(EventData.getBitmap().get(3));
        holder.eventName.setText(events.get(i).getEventName());
        holder.eventAddress.setText(events.get(i).getEventAddress());
        holder.eventUrl = events.get(i).getEventURL();
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
