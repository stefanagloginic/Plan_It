package com.example.user01.planit;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class EventTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RVAdapter rvAdapter;

    public EventTouchHelper(RVAdapter rvAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.rvAdapter = rvAdapter;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        rvAdapter.remove(viewHolder.getAdapterPosition());
    }

    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target ) {
        // NOT IMPLEMENTED
        return false;
    }
}
