package com.tacticsgames.dontdie;

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

    private ImageView circleImage;
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
        circleImage = ImageView.class.cast(findViewById(R.id.circleImage));
        gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverScore = TextView.class.cast(findViewById(R.id.gameOverScore));

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
        Animation a = new TranslateXAnimation(weapon.getView(), getScreenWidth());
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
        weapon.getView().startAnimation(a);
    }

    private void translateImageUpWithAnimation() {
        Animation a = new TranslateYAnimation(circleImage, PixelConverter.getPixelsFromDp(this, JUMP_LENGTH));
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
        circleImage.startAnimation(a);
    }

    private void translateImageDownWithAnimation() {
        Animation a = new TranslateYAnimation(circleImage, -bottomInPixels);
        a.setDuration(PixelConverter.getDpFromPixels(this, bottomInPixels) * 10);
        circleImage.startAnimation(a);
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

    private class TranslateYAnimation extends Animation {

        private int initialMargin;
        private int bottomMarginDelta;
        private View targetView;

        public TranslateYAnimation(View targetView, int bottomMarginDelta) {
            super();
            this.bottomMarginDelta = bottomMarginDelta;
            this.targetView = targetView;
            this.initialMargin = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams()).bottomMargin;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
            bottomInPixels = initialMargin + (int) (bottomMarginDelta * interpolatedTime);
            params.bottomMargin = bottomInPixels;
            targetView.setLayoutParams(params);
        }
    }

    private class TranslateXAnimation extends Animation {

        private int rightMargin;
        private View targetView;

        public TranslateXAnimation(View targetView, int rightMargin) {
            super();
            this.rightMargin = rightMargin;
            this.targetView = targetView;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
            params.rightMargin = (int) (rightMargin * interpolatedTime);
            targetView.setLayoutParams(params);
            if (CollisionChecker.areViewsColliding(circleImage, targetView, PixelConverter.getPixelsFromDp(GameActivity.this, 5))) {
                showGameOver();
            }
        }
    }
}
