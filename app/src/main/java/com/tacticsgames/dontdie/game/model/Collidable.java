package com.tacticsgames.dontdie.game.model;

import android.view.View;

/**
 * Created by Vlad on 07-Mar-16.
 */
public interface Collidable {

    double getWidthDeductionPercentage();

    double getHeightDeductionPercentage();

    View getView();

}
