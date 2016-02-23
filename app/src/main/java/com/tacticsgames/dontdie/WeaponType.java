package com.tacticsgames.dontdie;

/**
 * Created by vladfatu on 23/02/2016.
 */
public enum WeaponType {

    KUNAI(R.mipmap.kunai),
//    SHURIKEN(R.mipmap.shuriken),
    NINJA_STAR(R.mipmap.shuriken);

    private int imageId;

    WeaponType(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }
}
