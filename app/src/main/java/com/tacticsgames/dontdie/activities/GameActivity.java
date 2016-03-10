package com.tacticsgames.dontdie.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tacticsgames.dontdie.game.logic.InsultPicker;
import com.tacticsgames.dontdie.game.model.GameInfo;
import com.tacticsgames.dontdie.game.model.Penguin;
import com.tacticsgames.dontdie.renderer.AdRenderer;
import com.tacticsgames.dontdie.renderer.CounterRenderer;
import com.tacticsgames.dontdie.renderer.ImageViewRenderer;
import com.tacticsgames.dontdie.R;
import com.tacticsgames.dontdie.game.model.Spikes;
import com.tacticsgames.dontdie.game.model.Weapon;
import com.tacticsgames.dontdie.game.logic.WeaponGenerator;
import com.tacticsgames.dontdie.game.model.WeaponType;
import com.tacticsgames.dontdie.renderer.PenguinRenderer;
import com.tacticsgames.dontdie.renderer.WeaponRenderer;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends PlayServicesActivity {

    private static final int WEAPON_COUNT = 10;

    private ImageView penguinImage;
    private View gameOverLayout;
    private TextView gameOverScore;
    private RelativeLayout gameLayout;
    private TextView gameOverMessage;
    private View leftLayout;

    private Map<Integer, Weapon> weaponMap;
    private WeaponGenerator weaponGenerator;
    private InsultPicker insultPicker;
    private int gameCount;
    private GameInfo gameInfo;

    private PenguinRenderer penguinRenderer;
    private CounterRenderer counterRenderer;
    private WeaponRenderer weaponRenderer;
    private AdRenderer adRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameInfo = new GameInfo();
        initialiseLayouts();
        initialiseRenderers();
        initialiseGameLogic();
        initialiseGameLayout();
        initialiseWeapons();
        startGame();
    }

    private void initialiseLayouts() {
        gameLayout = RelativeLayout.class.cast(findViewById(R.id.game_content));
        penguinImage = ImageView.class.cast(findViewById(R.id.circleImage));
        gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverScore = TextView.class.cast(findViewById(R.id.gameOverScore));
        gameOverMessage = TextView.class.cast(findViewById(R.id.gameOverMessage));
        leftLayout = findViewById(R.id.leftLayout);
    }

    private void initialiseRenderers() {
        adRenderer = new AdRenderer(this);
        counterRenderer = new CounterRenderer(TextView.class.cast(findViewById(R.id.startGameCounter)), new GameCounterRendererListener());
        Penguin penguin = new Penguin(penguinImage);
        Spikes spikes = new Spikes(findViewById(R.id.spikesLayout));
        penguinRenderer = new PenguinRenderer(this, penguin, spikes, new GamePenguinRendererListener());
        weaponRenderer = new WeaponRenderer(this, penguin, new GameWeaponRendererListener());
    }

    private void initialiseGameLogic() {
        insultPicker = new InsultPicker();
        weaponGenerator = new WeaponGenerator();
    }

    public void initialiseGameLayout() {
        gameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!gameInfo.isDead()) {
                        penguinRenderer.translateImageUpWithAnimation();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected String getScreenName() {
        return getLocalClassName();
    }

    private void startGame() {
        penguinRenderer.translateImageDownWithAnimation();
        gameInfo.reset();
        gameOverLayout.setVisibility(View.GONE);
        ImageViewRenderer.rotate(penguinImage, 0);
        makeWeaponsInvisible();
        counterRenderer.showStartGameCounterAnimation();
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
        gameInfo.setGameOver(false);
        for (int i = 0; i < WEAPON_COUNT; i++) {
            weaponRenderer.startWeaponAnimation(weaponMap.get(i));
        }
    }

    private void makeWeaponsInvisible() {
        for (int i = 0; i < WEAPON_COUNT; i++) {
            ImageViewRenderer.setAlpha(weaponMap.get(i).getView(), 0);
        }
    }

    public void onRetryClicked(View view) {
        startGame();
    }

    private void showGameOver() {
        if (!gameInfo.isGameOver()) {
            gameInfo.setGameOver(true);
            gameInfo.setDead(true);
            penguinRenderer.translateImageFallWithAnimation();
            gameCount++;
            showInsult();
            if (gameCount % 5 == 0) {
                adRenderer.showAd();
            }
            submitScoreToLeaderBoard(R.string.leaderboard_best_score, gameInfo.getScore());
            submitEvent(R.string.event_games_played, 1);
            submitEvent(R.string.event_total_score, gameInfo.getScore());
            updateAchievements();
            gameOverScore.setText(Integer.toString(gameInfo.getScore()));
            gameOverLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAchievements() {
        unlockAchievement(R.string.achievement_first_blood);
        if (gameInfo.isKilledByCeiling()) {
            unlockAchievement(R.string.achievement_killed_by_spikes);
        } else if (gameInfo.getKilledBy() == WeaponType.KUNAI) {
            unlockAchievement(R.string.achievement_killed_by_a_kunai);
        } else if (gameInfo.getKilledBy() == WeaponType.NINJA_STAR) {
            unlockAchievement(R.string.achievement_killed_by_a_ninja_star);
        }

        if (gameInfo.getScore() >= 25) {
            unlockAchievement(R.string.achievement_die_hard_25);
        }
        if (gameInfo.getScore() >= 50) {
            unlockAchievement(R.string.achievement_die_hard_50);
        }
        if (gameInfo.getScore() >= 100) {
            unlockAchievement(R.string.achievement_die_hard_100);
        }
        if (gameInfo.getScore() >= 250) {
            unlockAchievement(R.string.achievement_die_hard_250);
        }
        if (gameInfo.getScore() >= 500) {
            unlockAchievement(R.string.achievement_die_hard_500);
        }
        if (gameInfo.getScore() >= 1000) {
            unlockAchievement(R.string.achievement_die_hard_1000);
        }
        incrementAchievement(R.string.achievement_died_50_times, 1);
        incrementAchievement(R.string.achievement_died_100_times, 1);
        incrementAchievement(R.string.achievement_died_1000_times, 1);
        incrementAchievement(R.string.achievement_total_score_1000, gameInfo.getScore());
        incrementAchievement(R.string.achievement_total_score_10000, gameInfo.getScore());
    }

    private void showInsult() {
        gameOverMessage.setText(insultPicker.pickInsult(gameCount));
    }

    private class GameCounterRendererListener implements CounterRenderer.CounterRendererListener {

        @Override
        public void onCounterFinished() {
            startWeaponAnimations();
        }
    }

    private class GamePenguinRendererListener implements PenguinRenderer.PenguinRendererListener {

        @Override
        public void onKilledByCeiling() {
            gameInfo.setKilledByCeiling(true);
            showGameOver();
        }
    }

    private class GameWeaponRendererListener implements WeaponRenderer.WeaponRendererListener {

        @Override
        public void onWeaponAnimationFinished(Weapon weapon) {
            if (!gameInfo.isGameOver()) {
                weaponRenderer.startWeaponAnimation(weapon);
                gameInfo.incrementPassedObstacles();
            }
        }

        @Override
        public void onWeaponCollidedWithPenguin(Weapon weapon) {
            gameInfo.setKilledBy(weapon.getWeaponType());
            showGameOver();
        }
    }

}
