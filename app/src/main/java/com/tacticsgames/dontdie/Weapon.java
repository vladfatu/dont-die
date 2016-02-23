package com.tacticsgames.dontdie;

import android.view.View;

/**
 * Created by vladfatu on 23/02/2016.
 */
public class Weapon {

    private WeaponType weaponType;
    private View view;
    private int animationDuration;

    public Weapon(View view) {
        this.view = view;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }
}
