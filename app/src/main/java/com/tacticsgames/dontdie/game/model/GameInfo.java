package com.tacticsgames.dontdie.game.model;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class GameInfo {

    private boolean gameOver;
    private WeaponType killedBy;
    private boolean killedByCeiling;
    private boolean dead;
    private int passedObstacles;

    public void reset() {
        killedByCeiling = false;
        killedBy = null;
        dead = false;
        passedObstacles = 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public WeaponType getKilledBy() {
        return killedBy;
    }

    public void setKilledBy(WeaponType killedBy) {
        this.killedBy = killedBy;
    }

    public boolean isKilledByCeiling() {
        return killedByCeiling;
    }

    public void setKilledByCeiling(boolean killedByCeiling) {
        this.killedByCeiling = killedByCeiling;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void incrementPassedObstacles() {
        passedObstacles++;
    }

    public int getScore() {
        return passedObstacles;
    }
}
