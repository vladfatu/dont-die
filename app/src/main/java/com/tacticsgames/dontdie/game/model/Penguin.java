package com.tacticsgames.dontdie.game.model;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class Penguin implements Collidable {

    private final static double PENGUIN_WIDTH_DEDUCTION_PERCENTAGE = 25;
    private final static double PENGUIN_HEIGHT_DEDUCTION_PERCENTAGE = 15;

    private ImageView penguinImage;

    public Penguin(ImageView imageView) {
        this.penguinImage = imageView;
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
        return penguinImage;
    }

    public ImageView getImageView () {
        return penguinImage;
    }

}
