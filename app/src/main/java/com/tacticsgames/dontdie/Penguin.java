package com.tacticsgames.dontdie;

import android.view.View;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class Penguin implements Collidable{

    private final static double PENGUIN_WIDTH_DEDUCTION_PERCENTAGE = 25;
    private final static double PENGUIN_HEIGHT_DEDUCTION_PERCENTAGE = 15;

    private View view;

    public Penguin(View view) {
        this.view = view;
    }

    @Override
    public double getWidthDeductionPercentage() {
        return PENGUIN_WIDTH_DEDUCTION_PERCENTAGE;
    }

    @Override
    public double getHeightDeductionPercentage() {
        return PENGUIN_HEIGHT_DEDUCTION_PERCENTAGE;
    }

    @Override
    public View getView() {
        return view;
    }

}
