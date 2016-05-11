package com.example.user01.planit;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by user01 on 5/10/2016.
 */
public class CustomLayoutManager extends LinearLayoutManager {

    CustomLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
