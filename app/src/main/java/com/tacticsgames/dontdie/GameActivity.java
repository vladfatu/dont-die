package com.tacticsgames.dontdie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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
    private Bitmap penguinBitmap;
    private Bitmap penguinBitmap1;
    private Bitmap penguinBitmap2;
    private Bitmap penguinBitmap3;
    private View spikesLayout;
    private View gameOverLayout;
    private TextView gameOverScore;
    private RelativeLayout gameLayout;
    private TextView gameOverMessage;
    private TextView startGameCounterView;
    private View leftLayout;
    private int startGameCounter;
    private boolean gameOver;
    private WeaponType killedBy;
    private boolean killedByCeiling;
    private boolean dead;

    private int bottomInPixels;
    private int passedObstacles;

    private Map<Integer, Weapon> weaponMap;
    private WeaponGenerator weaponGenerator;
    private InsultPicker insultPicker;
    private int gameCount;

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
        gameOverMessage = TextView.class.cast(findViewById(R.id.gameOverMessage));
        startGameCounterView = TextView.class.cast(findViewById(R.id.startGameCounter));
        leftLayout = findViewById(R.id.leftLayout);

        insultPicker = new InsultPicker();
        weaponGenerator = new WeaponGenerator();
        updatePenguinAnimationSizes();
        updatePenguinToStandardImage();
        initialiseWeapons();
        startGame();
    }

    private void updatePenguinToStandardImage() {
        penguinImage.setImageBitmap(penguinBitmap);
    }

    private void updatePenguinAnimationSizes() {
        penguinBitmap = getScaledPenguinBitmap(R.mipmap.penguin);
        penguinBitmap1 = getScaledPenguinBitmap(R.mipmap.penguin_1);
        penguinBitmap2 = getScaledPenguinBitmap(R.mipmap.penguin_2);
        penguinBitmap3 = getScaledPenguinBitmap(R.mipmap.penguin_3);
    }

    private Bitmap getScaledPenguinBitmap(int imageId) {
        Bitmap penguinBitmap = BitmapFactory.decodeResource(getResources(), imageId);
        int newHeight = getScreenHeight() * 12/100;
        double ratio = (double)newHeight/penguinBitmap.getHeight();
        int newWidth = (int) (penguinBitmap.getWidth() * ratio);
        return Bitmap.createScaledBitmap(penguinBitmap, newWidth, newHeight, true);
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

    private void startGame() {
        translateImageDownWithAnimation();
        killedByCeiling = false;
        killedBy = null;
        dead = false;
        gameOverLayout.setVisibility(View.GONE);
        startGameCounterView.setVisibility(View.VISIBLE);
        rotate(penguinImage, 0);
        passedObstacles = 0;
        startGameCounterView.setText("3");
        startGameCounter = 3;
        makeWeaponsInvisible();
        showStartGameCounterAnimation();
    }

    private void showStartGameCounterAnimation() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (startGameCounter <= 0) {
                    startGameCounterView.setVisibility(View.GONE);
                } else if (startGameCounter == 1) {
                    startWeaponAnimations();
                    startGameCounter--;
                    startGameCounterView.setText(R.string.go);
                    showStartGameCounterAnimation();
                } else {
                    startGameCounter--;
                    startGameCounterView.setText(Integer.toString(startGameCounter));
                    showStartGameCounterAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startGameCounterView.startAnimation(fadeIn);
    }

    private void initialiseWeapons() {
        weaponMap = new HashMap<>();
        for (int i = 0; i < WEAPON_COUNT; i++) {
            Weapon weapon = weaponGenerator.generateWeapon(this);
            weaponMap.put(i, weapon);
            gameLayout.addView(weapon.getView());
        }
        leftLayout.bringToFront();
    }

    private void startWeaponAnimations() {
        gameOver = false;
        for (int i = 0; i < WEAPON_COUNT; i++) {
            startWeaponAnimation(i);
        }
    }

    private void startWeaponAnimation(final int id) {
        Weapon weapon = weaponMap.get(id);
        weaponGenerator.randomizeWeapon(this, weapon, getScreenHeight());
        rotate(weapon.getView(), 0);
        setAlpha(weapon.getView(), 255);

        boolean rotate = weapon.getWeaponType() == WeaponType.NINJA_STAR;
        Animation a = new TranslateXAnimation(weapon.getView(), getScreenWidth(), rotate, weapon);
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

    private void makeWeaponsInvisible() {
        for (int i = 0; i < WEAPON_COUNT; i++) {
            setAlpha(weaponMap.get(i).getView(), 0);
        }
    }

    private void setAlpha(ImageView imageView, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setImageAlpha(alpha);
        } else {
            imageView.setAlpha(alpha);
        }
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
        penguinImage.startAnimation(a);

        AnimationDrawable animation = new AnimationDrawable();
        animation.setOneShot(false);
        animation.addFrame(new BitmapDrawable(getResources(), penguinBitmap1), 50);
        animation.addFrame(new BitmapDrawable(getResources(), penguinBitmap2), 50);
        animation.addFrame(new BitmapDrawable(getResources(), penguinBitmap3), 50);
        penguinImage.setImageDrawable(animation);
        animation.start();
    }

    private void translateImageFallWithAnimation() {
        updatePenguinToStandardImage();
        Animation a = new TranslateYAnimation(penguinImage, -bottomInPixels, true);
        a.setDuration(PixelConverter.getDpFromPixels(this, bottomInPixels) * 10 / 5);
        penguinImage.startAnimation(a);
    }

    public void onLayoutClick(View view) {
        if (!dead) {
            translateImageUpWithAnimation();
        }
    }

    public void onRetryClicked(View view) {
        startGame();
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (size.x >= size.y) {
            return size.x;
        } else {
            return size.y;
        }
    }

    private int getScreenHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (size.y <= size.x) {
            return size.y;
        } else {
            return size.x;
        }
    }

    private void showGameOver() {
        if (!gameOver) {
            gameOver = true;
            dead = true;
            translateImageFallWithAnimation();
            gameCount++;
            showInsult();
            if (gameCount % 5 == 0) {
                showAd();
            }
            submitScoreToLeaderBoard(R.string.leaderboard_best_score, passedObstacles);
            submitEvent(R.string.event_games_played, 1);
            submitEvent(R.string.event_total_score, passedObstacles);
            updateAchievements();
            gameOverScore.setText(Integer.toString(passedObstacles));
            gameOverLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAchievements() {
        unlockAchievement(R.string.achievement_first_blood);
        if (killedByCeiling) {
            unlockAchievement(R.string.achievement_killed_by_spikes);
        } else if (killedBy == WeaponType.KUNAI) {
            unlockAchievement(R.string.achievement_killed_by_a_kunai);
        } else if (killedBy == WeaponType.NINJA_STAR) {
            unlockAchievement(R.string.achievement_killed_by_a_ninja_star);
        }

        if (passedObstacles >= 25) {
            unlockAchievement(R.string.achievement_die_hard_25);
        }
        if (passedObstacles >= 50) {
            unlockAchievement(R.string.achievement_die_hard_50);
        }
        if (passedObstacles >= 100) {
            unlockAchievement(R.string.achievement_die_hard_100);
        }
        if (passedObstacles >= 250) {
            unlockAchievement(R.string.achievement_die_hard_250);
        }
        if (passedObstacles >= 500) {
            unlockAchievement(R.string.achievement_die_hard_500);
        }
        if (passedObstacles >= 1000) {
            unlockAchievement(R.string.achievement_die_hard_1000);
        }
        incrementAchievement(R.string.achievement_died_50_times, 1);
        incrementAchievement(R.string.achievement_died_100_times, 1);
        incrementAchievement(R.string.achievement_died_1000_times, 1);
        incrementAchievement(R.string.achievement_total_score_1000, passedObstacles);
        incrementAchievement(R.string.achievement_total_score_10000, passedObstacles);
    }

    private void showInsult() {
        gameOverMessage.setText(insultPicker.pickInsult(gameCount));
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
                killedByCeiling = true;
                showGameOver();
            }
        }
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
            if (targetView.getLeft() > PixelConverter.getPixelsFromDp(GameActivity.this, 3)) {
                ViewGroup.MarginLayoutParams params = ViewGroup.MarginLayoutParams.class.cast(targetView.getLayoutParams());
                params.rightMargin = (int) (maxRightMargin * interpolatedTime);
                targetView.setLayoutParams(params);

                if (rotate) {
                    float angle = 360 * 7 * (1 - interpolatedTime);
                    rotate(targetView, angle);
                }

                if (CollisionChecker.areViewsColliding(penguinImage, targetView, PixelConverter.getPixelsFromDp(GameActivity.this, 5))) {
                    killedBy = weapon.getWeaponType();
                    showGameOver();
                }
            }
        }
    }
}
