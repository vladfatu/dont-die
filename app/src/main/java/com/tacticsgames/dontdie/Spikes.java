package com.tacticsgames.dontdie;

import android.view.View;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class Spikes implements Collidable {

    private final static double SPIKES_WIDTH_DEDUCTION_PERCENTAGE = 0;
    private final static double SPIKES_HEIGHT_DEDUCTION_PERCENTAGE = -30;

    private View view;

    public Spikes(View view) {
        this.view = view;
    }

    @Override
    public double getWidthDeductionPercentage() {
        return SPIKES_WIDTH_DEDUCTION_PERCENTAGE;
    }

    @Override
    public double getHeightDeductionPercentage() {
        return SPIKES_HEIGHT_DEDUCTION_PERCENTAGE;
    }

    @Override
    public View getView() {
        return view;
    }

}
