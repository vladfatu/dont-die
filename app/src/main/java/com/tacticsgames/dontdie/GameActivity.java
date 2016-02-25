package com.tacticsgames.dontdie;

import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends PlayServicesActivity {

    private static final int JUMP_LENGTH = 50;
    private static final int WEAPON_COUNT = 10;

    private InterstitialAd mInterstitialAd;

    private ImageView penguinImage;
    private View spikesLayout;
    private View gameOverLayout;
    private TextView gameOverScore;
    private RelativeLayout gameLayout;
    private boolean gameOver;

    private int bottomInPixels;
    private int passedObstacles;

    private Map<Integer, Weapon> weaponMap;
    private WeaponGenerator weaponGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        setupAd();
        requestNewInterstitial();

        gameLayout = RelativeLayout.class.cast(findViewById(R.id.game_content));
        penguinImage = ImageView.class.cast(findViewById(R.id.circleImage));
        gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverScore = TextView.class.cast(findViewById(R.id.gameOverScore));
        spikesLayout = findViewById(R.id.spikesLayout);

        weaponGenerator = new WeaponGenerator();
        initialiseWeapons();
        startObstacles();
    }

    private void setupAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9A71D5CD236AB4E5565199A22CB660D")
                .addTestDevice("9C9C6C29924B4A4870047BD11841E1AF")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void showAd() {
        mInterstitialAd.show();
        requestNewInterstitial();
    }

    @Override
    protected String getScreenName() {
        return getLocalClassName();
    }

    private void startObstacles(){
        gameOverLayout.setVisibility(View.GONE);
        rotate(penguinImage, 0);
        gameOver = false;
        passedObstacles = 0;
        startWeaponAnimations();
    }

    private void initialiseWeapons() {
        weaponMap = new HashMap<>();
        for (int i=0; i< WEAPON_COUNT; i++) {
            Weapon weapon = weaponGenerator.generateWeapon(this);
            weaponMap.put(i, weapon);
            gameLayout.addView(weapon.getView());
        }
    }

    private void startWeaponAnimations() {
        for (int i=0; i< WEAPON_COUNT; i++) {
            startWeaponAnimation(i);
        }
    }

    private void startWeaponAnimation(final int id) {
        Weapon weapon = weaponMap.get(id);
        weaponGenerator.randomizeWeapon(this, weapon, getScreenWidth());
        rotate(weapon.getView(), 0);

        boolean rotate = weapon.getWeaponType() == WeaponType.NINJA_STAR;
        Animation a = new TranslateXAnimation(weapon.getView(), getScreenWidth(), rotate);
        a.setDuration(weapon.getAnimationDuration());
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!gameOver) {
                    startWeaponAnimation(id);
                    passedObstacles++;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        animationSet.addAnimation(a);
        weapon.getView().startAnimation(a);

    }

    private void translateImageUpWithAnimation() {
        Animation a = new TranslateYAnimation(penguinImage, PixelConverter.getPixelsFromDp(this, JUMP_LENGTH), false);
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
        penguinImage.startAnimation(a);
    }

    private void translateImageDownWithAnimation() {
        Animation a = new TranslateYAnimation(penguinImage, -bottomInPixels, false);
        a.setDuration(PixelConverter.getDpFromPixels(this, bottomInPixels) * 10);
        penguinImage.startAnimation(a);
    }

    private void translateImageFallWithAnimation() {
        Animation a = new TranslateYAnimation(penguinImage, -bottomInPixels, true);
        a.setDuration(PixelConverter.getDpFromPixels(this, bottomInPixels) * 10 / 5);
        penguinImage.startAnimation(a);
    }

    public void onLayoutClick(View view) {
        translateImageUpWithAnimation();
    }

    public void onRetryClicked(View view) {
        startObstacles();
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void showGameOver() {
        if (!gameOver) {
            translateImageFallWithAnimation();
            showAd();
            gameOver = true;
            unlockAchievement(R.string.achievement_first_blood);
            submitScoreToLeaderBoard(R.string.leaderboard_best_score, passedObstacles);
            submitEvent(R.string.event_games_played, 1);
            submitEvent(R.string.event_total_score, passedObstacles);
            incrementAchievement(R.string.achievement_total_score_1000, passedObstacles);
            gameOverScore.setText(Integer.toString(passedObstacles));
            gameOverLayout.setVisibility(View.VISIBLE);
        }
    }

    private void rotate(ImageView targetView, float angle) {
        Matrix matrix = new Matrix();
        targetView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate(angle, targetView.getDrawable().getBounds().width() / 2, targetView.getDrawable().getBounds().height() / 2);
        targetView.setImageMatrix(matrix);
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
                rotate(targetView, angle);
            }

            if (CollisionChecker.areViewsColliding(targetView, spikesLayout, PixelConverter.getPixelsFromDp(GameActivity.this, 1))) {
                showGameOver();
            }
        }
    }

    private class TranslateXAnimation extends Animation {

        private int rightMargin;
        private ImageView targetView;
        private boolean rotate;

        public TranslateXAnimation(ImageView targetView, int rightMargin, boolean rotate) {
            super();
            this.rightMargin = rightMargin;
            this.targetView = targetView;
            this.rotate = rotate;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
            params.rightMargin = (int) (rightMargin * interpolatedTime);
            targetView.setLayoutParams(params);

            if (rotate) {
                float angle = 360 * 7 * (1 - interpolatedTime);
                rotate(targetView, angle);
            }

            if (CollisionChecker.areViewsColliding(penguinImage, targetView, PixelConverter.getPixelsFromDp(GameActivity.this, 5))) {
                showGameOver();
            }
        }
    }
}
