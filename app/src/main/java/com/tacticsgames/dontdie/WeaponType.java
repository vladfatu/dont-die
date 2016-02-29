package com.tacticsgames.dontdie;

/**
 * Created by vladfatu on 23/02/2016.
 */
public enum WeaponType {

    KUNAI(R.mipmap.kunai, 3.4),
//    SHURIKEN(R.mipmap.shuriken),
    NINJA_STAR(R.mipmap.shuriken, 7.8);

    private double heightPercentage;
    private int imageId;

    WeaponType(int imageId, double heightPercentage) {
        this.imageId = imageId;
        this.heightPercentage = heightPercentage;
    }

    public int getImageId() {
        return imageId;
    }

    public double getHeightPercentage() {
        return heightPercentage;
    }
}
