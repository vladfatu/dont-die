package com.tacticsgames.dontdie.renderer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.tacticsgames.dontdie.R;
import com.tacticsgames.dontdie.game.logic.CollisionChecker;
import com.tacticsgames.dontdie.game.model.Penguin;
import com.tacticsgames.dontdie.game.model.Spikes;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class PenguinRenderer {

    private static final int JUMP_LENGTH = 50;

    private Activity activity;
    private Penguin penguin;
    private Spikes spikes;
    private PenguinRendererListener penguinRendererListener;

    private int bottomInPixels;

    private Bitmap penguinBitmap;
    private Bitmap penguinBitmap1;
    private Bitmap penguinBitmap2;
    private Bitmap penguinBitmap3;

    public PenguinRenderer(Activity activity, Penguin penguin, Spikes spikes, PenguinRendererListener penguinRendererListener) {
        this.activity = activity;
        this.penguin = penguin;
        this.spikes = spikes;
        this.penguinRendererListener = penguinRendererListener;
        updatePenguinAnimationSizes();
        updatePenguinToStandardImage();
    }

    private void updatePenguinAnimationSizes() {
        penguinBitmap = getScaledPenguinBitmap(R.mipmap.penguin);
        penguinBitmap1 = getScaledPenguinBitmap(R.mipmap.penguin_1);
        penguinBitmap2 = getScaledPenguinBitmap(R.mipmap.penguin_2);
        penguinBitmap3 = getScaledPenguinBitmap(R.mipmap.penguin_3);
    }

    private void updatePenguinToStandardImage() {
        penguin.getImageView().setImageBitmap(penguinBitmap);
    }

    private Bitmap getScaledPenguinBitmap(int imageId) {
        Bitmap penguinBitmap = BitmapFactory.decodeResource(activity.getResources(), imageId);
        int newHeight = PixelConverter.getScreenHeight(activity) * 12/100;
        double ratio = (double)newHeight/penguinBitmap.getHeight();
        int newWidth = (int) (penguinBitmap.getWidth() * ratio);
        return Bitmap.createScaledBitmap(penguinBitmap, newWidth, newHeight, true);
    }

    public void translateImageUpWithAnimation() {
        Animation a = new TranslateYAnimation(penguin.getImageView(), PixelConverter.getPixelsFromDp(activity, JUMP_LENGTH), false);
        a.setDuration(JUMP_LENGTH * 5);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                translateImageDownWithAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        penguin.getImageView().startAnimation(a);
    }

    public void translateImageDownWithAnimation() {
        Animation a = new TranslateYAnimation(penguin.getImageView(), -bottomInPixels, false);
        a.setDuration(PixelConverter.getDpFromPixels(activity, bottomInPixels) * 10);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updatePenguinToStandardImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        penguin.getImageView().startAnimation(a);

        AnimationDrawable animation = new AnimationDrawable();
        animation.setOneShot(false);
        animation.addFrame(new BitmapDrawable(activity.getResources(), penguinBitmap1), 50);
        animation.addFrame(new BitmapDrawable(activity.getResources(), penguinBitmap2), 50);
        animation.addFrame(new BitmapDrawable(activity.getResources(), penguinBitmap3), 50);
        penguin.getImageView().setImageDrawable(animation);
        animation.start();
    }

    public void translateImageFallWithAnimation() {
        updatePenguinToStandardImage();
        Animation a = new TranslateYAnimation(penguin.getImageView(), -bottomInPixels, true);
        a.setDuration(PixelConverter.getDpFromPixels(activity, bottomInPixels) * 10 / 5);
        penguin.getImageView().startAnimation(a);
    }

    private class TranslateYAnimation extends Animation {

        private int initialMargin;
        private int bottomMarginDelta;
        private ImageView targetView;
        private boolean rotate;

        public TranslateYAnimation(ImageView targetView, int bottomMarginDelta, boolean rotate) {
            super();
            this.bottomMarginDelta = bottomMarginDelta;
            this.targetView = targetView;
            this.initialMargin = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams()).bottomMargin;
            this.rotate = rotate;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
            bottomInPixels = initialMargin + (int) (bottomMarginDelta * interpolatedTime);
            params.bottomMargin = bottomInPixels;
            targetView.setLayoutParams(params);

            if (rotate) {
                float angle = 90 * interpolatedTime;
                ImageViewRenderer.rotate(targetView, angle);
            }

            if (CollisionChecker.areViewsColliding(penguin, spikes)) {
                penguinRendererListener.onKilledByCeiling();
            }
        }
    }

    public interface PenguinRendererListener {
        void onKilledByCeiling();
    }

}
