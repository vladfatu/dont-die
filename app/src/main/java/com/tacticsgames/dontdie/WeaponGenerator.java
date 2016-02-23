package com.tacticsgames.dontdie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by vladfatu on 23/02/2016.
 */
public class WeaponGenerator {

    public Random random;

    public WeaponGenerator() {
        random = new Random();
    }

    public Weapon generateWeapon(Context context) {
        return new Weapon(generateWeaponView(context));
    }

    public void randomizeWeapon(Context context, Weapon weapon, int screenWidth) {
        weapon.setWeaponType(getRandomWeaponType());
        weapon.setAnimationDuration(getRandomDuration(context, screenWidth));
        randomizeWeaponPosition(weapon);
    }

    private void randomizeWeaponPosition(Weapon weapon) {
        ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(weapon.getView().getLayoutParams());
        params.rightMargin = 0;
        params.bottomMargin = random.nextInt(900);
        weapon.getView().setLayoutParams(params);
    }

    private int getRandomDuration(Context context, int screenWidth) {
        return (int) (PixelConverter.getDpFromPixels(context, screenWidth) * 10 / (1 + (random.nextFloat() * 3)));
    }

    private WeaponType getRandomWeaponType() {
        return WeaponType.values()[random.nextInt(WeaponType.values().length)];
    }

    private View generateWeaponView(Context context) {
        ImageView weapon = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        weapon.setLayoutParams(layoutParams);

        weapon.setImageResource(R.drawable.circle);
        return weapon;
    }

}
