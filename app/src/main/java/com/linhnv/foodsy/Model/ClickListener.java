package com.linhnv.foodsy.Model;

import android.view.View;

/**
 * Created by linhnv on 11/06/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
