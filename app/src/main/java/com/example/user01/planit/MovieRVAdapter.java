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

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.EventViewHolder> {

    private ArrayList<Movie> movies;

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView movieImage;
        TextView movieName;
        TextView movieDescription;
        String movieURL;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            movieImage = (ImageView)itemView.findViewById(R.id.movie_image);
            movieName = (TextView)itemView.findViewById(R.id.movie_name);
            movieDescription = (TextView)itemView.findViewById(R.id.movie_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Going to web page", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(movieURL);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(intent);

                }
            });
        }
    }

    MovieRVAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return (new EventViewHolder(v));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int i) {
        holder.movieImage.setImageBitmap(EventData.getBitmap().get(2));
        holder.movieName.setText(movies.get(i).getTitle());
        holder.movieDescription.setText(movies.get(i).getOverview());
        holder.movieURL = "https://www.google.com/#q=" + movies.get(i).getTitle();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void remove(int position) {
        movies.remove(position);
        notifyItemRemoved(position);
    }
}
