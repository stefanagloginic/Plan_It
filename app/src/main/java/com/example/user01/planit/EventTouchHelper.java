package com.example.user01.planit;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class EventTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RVAdapter rvAdapter;
    private EventfulRVAdapter eventfulAdapter;
    private MovieRVAdapter movieAdapter;
    private HikeRVAdapter hikeAdapter;
    private MuseumRVAdapter museumAdapter;

    public EventTouchHelper(RVAdapter rvAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.rvAdapter = rvAdapter;
    }

    public EventTouchHelper(EventfulRVAdapter eventfulRVAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.eventfulAdapter = eventfulRVAdapter;
    }

    public EventTouchHelper(MovieRVAdapter movieRVAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.movieAdapter = movieRVAdapter;
    }

    public EventTouchHelper(HikeRVAdapter hikeAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.hikeAdapter = hikeAdapter;
    }

    public EventTouchHelper(MuseumRVAdapter museumAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.museumAdapter = museumAdapter;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (eventfulAdapter != null) {
            eventfulAdapter.remove(viewHolder.getAdapterPosition());
        }
        else if (rvAdapter != null) {
            rvAdapter.remove(viewHolder.getAdapterPosition());
        }
        else if (movieAdapter != null){
            movieAdapter.remove(viewHolder.getAdapterPosition());
        }
        else if (hikeAdapter != null) {
            hikeAdapter.remove(viewHolder.getAdapterPosition());
        }
        else if (museumAdapter != null) {
            museumAdapter.remove(viewHolder.getAdapterPosition());
        }
    }

    // NOT MEANT TO BE IMPLEMENTED
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target ) {
        return false;
    }
}
