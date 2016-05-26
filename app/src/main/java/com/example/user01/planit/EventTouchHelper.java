package com.example.user01.planit;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class EventTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RVAdapter rvAdapter;
    private EventfulRVAdapter eventfulAdapter;

    public EventTouchHelper(RVAdapter rvAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.rvAdapter = rvAdapter;
    }

    public EventTouchHelper(EventfulRVAdapter eventfulRVAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.eventfulAdapter = eventfulRVAdapter;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (rvAdapter == null) {
            eventfulAdapter.remove(viewHolder.getAdapterPosition());
        } else {
            rvAdapter.remove(viewHolder.getAdapterPosition());
        }
    }

    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target ) {
        // NOT MEANT TO BE IMPLEMENTED
        return false;
    }
}
