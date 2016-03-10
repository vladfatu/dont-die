package com.tacticsgames.dontdie.renderer;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.tacticsgames.dontdie.game.logic.CollisionChecker;
import com.tacticsgames.dontdie.game.logic.WeaponGenerator;
import com.tacticsgames.dontdie.game.model.Penguin;
import com.tacticsgames.dontdie.game.model.Weapon;
import com.tacticsgames.dontdie.game.model.WeaponType;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class WeaponRenderer {

    private Activity activity;
    private WeaponGenerator weaponGenerator;
    private Penguin penguin;
    private WeaponRendererListener weaponRendererListener;

    public WeaponRenderer(Activity activity, Penguin penguin, WeaponRendererListener weaponRendererListener) {
        weaponGenerator = new WeaponGenerator();
        this.activity = activity;
        this.penguin = penguin;
        this.weaponRendererListener = weaponRendererListener;
    }

    public void startWeaponAnimation(final Weapon weapon) {
        weaponGenerator.randomizeWeapon(activity, weapon, PixelConverter.getScreenHeight(activity));
        ImageViewRenderer.rotate(weapon.getView(), 0);
        ImageViewRenderer.setAlpha(weapon.getView(), 255);

        boolean rotate = weapon.getWeaponType() == WeaponType.NINJA_STAR;
        Animation a = new TranslateXAnimation(weapon.getView(), PixelConverter.getScreenWidth(activity), rotate, weapon);
        a.setDuration(weapon.getAnimationDuration());
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                weaponRendererListener.onWeaponAnimationFinished(weapon);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        weapon.getView().startAnimation(a);

    }

    private class TranslateXAnimation extends Animation {

        private int maxRightMargin;
        private ImageView targetView;
        private boolean rotate;
        private Weapon weapon;

        public TranslateXAnimation(ImageView targetView, int maxRightMargin, boolean rotate, Weapon weapon) {
            super();
            this.maxRightMargin = maxRightMargin;
            this.targetView = targetView;
            this.rotate = rotate;
            this.weapon = weapon;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (targetView.getLeft() > PixelConverter.getPixelsFromDp(activity, 3)) {
                ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
                params.rightMargin = (int) (maxRightMargin * interpolatedTime);
                targetView.setLayoutParams(params);

                if (rotate) {
                    float angle = 360 * 7 * (1 - interpolatedTime);
                    ImageViewRenderer.rotate(targetView, angle);
                }

                if (CollisionChecker.areViewsColliding(penguin, weapon)) {
                    weaponRendererListener.onWeaponCollidedWithPenguin(weapon);
                }
            }
        }
    }

    public interface WeaponRendererListener {
        void onWeaponAnimationFinished(Weapon weapon);
        void onWeaponCollidedWithPenguin(Weapon weapon);
    }

}
