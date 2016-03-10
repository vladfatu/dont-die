package com.tacticsgames.dontdie.game.logic;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tacticsgames.dontdie.game.model.WeaponType;
import com.tacticsgames.dontdie.game.model.Weapon;

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

    public void randomizeWeapon(Context context, Weapon weapon, int screenHeight) {
        weapon.setWeaponType(context, getRandomWeaponType(), screenHeight);
        weapon.setAnimationDuration(getRandomDuration(context));
        randomizeWeaponPosition(weapon, screenHeight);
    }

    private void randomizeWeaponPosition(Weapon weapon, int screenHeight) {
        ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(weapon.getView().getLayoutParams());
        params.rightMargin = 0;
        params.bottomMargin = random.nextInt(screenHeight*80/100);
        weapon.getView().setLayoutParams(params);
    }

    private int getRandomDuration(Context context) {
        return (int) (1000 * 10 / (1 + (random.nextFloat() * 3)));
    }

    private WeaponType getRandomWeaponType() {
        return WeaponType.values()[random.nextInt(WeaponType.values().length)];
    }

    private ImageView generateWeaponView(Context context) {
        ImageView weapon = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        weapon.setLayoutParams(layoutParams);

        return weapon;
    }

}
