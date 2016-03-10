package com.tacticsgames.dontdie.game.logic;

import android.graphics.Rect;

import com.tacticsgames.dontdie.game.model.Collidable;

public class CollisionChecker {

    public static boolean areViewsColliding(Collidable collidable1, Collidable collidable2) {
        int widthOffset = getWidthOffset(collidable1);
        int heightOffset = getHeightOffset(collidable1);
        Rect rect1 = new Rect(collidable1.getView().getLeft() + widthOffset, collidable1.getView().getTop() + heightOffset, collidable1.getView().getRight() - widthOffset, collidable1.getView().getBottom() - heightOffset);
        widthOffset = getWidthOffset(collidable2);
        heightOffset = getHeightOffset(collidable2);
        Rect rect2 = new Rect(collidable2.getView().getLeft() + widthOffset, collidable2.getView().getTop() + heightOffset, collidable2.getView().getRight() - widthOffset, collidable2.getView().getBottom() - heightOffset);
        if (rect1.intersect(rect2)) {
            System.out.println("Game Over!");
            System.out.println("rect1: " + rect1.toString());
            System.out.println("rect2: " + rect2.toString());
            return true;
        } else {
            return false;
        }
    }

    private static int getWidthOffset(Collidable collidable) {
        return (int) (collidable.getView().getWidth() * collidable.getWidthDeductionPercentage() / 100);
    }

    private static int getHeightOffset(Collidable collidable) {
        return  (int) (collidable.getView().getHeight() * collidable.getHeightDeductionPercentage() / 100);
    }

}
