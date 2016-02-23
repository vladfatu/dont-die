package com.tacticsgames.dontdie;

import android.widget.ImageView;

/**
 * Created by vladfatu on 23/02/2016.
 */
public class Weapon {

    private WeaponType weaponType;
    private ImageView view;
    private int animationDuration;

    public Weapon(ImageView view) {
        this.view = view;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
        view.setImageResource(weaponType.getImageId());
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }
}
