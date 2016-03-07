package com.tacticsgames.dontdie;

/**
 * Created by vladfatu on 23/02/2016.
 */
public enum WeaponType {

    KUNAI(R.mipmap.kunai, 3.4, 10, 5),
    NINJA_STAR(R.mipmap.shuriken, 7.8, 15, 15);

    private double heightPercentage;
    private double widthDeductionPercentage;
    private double heightDeductionPercentage;
    private int imageId;

    WeaponType(int imageId, double heightPercentage, double widthDeductionPercentage, double heightDeductionPercentage) {
        this.imageId = imageId;
        this.heightPercentage = heightPercentage;
    }

    public int getImageId() {
        return imageId;
    }

    public double getHeightPercentage() {
        return heightPercentage;
    }

    public double getWidthDeductionPercentage() {
        return widthDeductionPercentage;
    }

    public double getHeightDeductionPercentage() {
        return heightDeductionPercentage;
    }
}
