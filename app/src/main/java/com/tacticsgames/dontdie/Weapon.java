package com.tacticsgames.dontdie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * Created by vladfatu on 23/02/2016.
 */
public class Weapon implements Collidable{

    private WeaponType weaponType;
    private ImageView view;
    private int animationDuration;

    public Weapon(ImageView view) {
        this.view = view;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Context context, WeaponType weaponType, int screenHeight) {
        this.weaponType = weaponType;
        view.setImageBitmap(getImageBitmap(context, weaponType, screenHeight));
    }

    private Bitmap getImageBitmap(Context context, WeaponType weaponType, int screenHeight) {
        Bitmap weaponBitmap = BitmapFactory.decodeResource(context.getResources(), weaponType.getImageId());
        int newHeight = (int) (screenHeight * weaponType.getHeightPercentage()/100);
        double ratio = (double)newHeight/weaponBitmap.getHeight();
        int newWidth = (int) (weaponBitmap.getWidth() * ratio);
        return Bitmap.createScaledBitmap(weaponBitmap, newWidth, newHeight, true);
    }

    @Override
    public double getWidthDeductionPercentage() {
        return weaponType.getWidthDeductionPercentage();
    }

    @Override
    public double getHeightDeductionPercentage() {
        return weaponType.getHeightDeductionPercentage();
    }

    @Override
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
