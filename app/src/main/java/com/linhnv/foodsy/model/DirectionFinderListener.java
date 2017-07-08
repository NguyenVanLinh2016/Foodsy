package com.linhnv.foodsy.model;

import java.util.List;

/**
 * Created by linhnv on 07/07/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
