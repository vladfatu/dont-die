package com.tacticsgames.dontdie.renderer;

import android.graphics.Matrix;
import android.os.Build;
import android.widget.ImageView;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class ImageViewRenderer {

    public static void rotate(ImageView targetView, float angle) {
        Matrix matrix = new Matrix();
        targetView.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate(angle, targetView.getDrawable().getBounds().width() / 2, targetView.getDrawable().getBounds().height() / 2);
        targetView.setImageMatrix(matrix);
    }

    public static void setAlpha(ImageView imageView, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setImageAlpha(alpha);
        } else {
            imageView.setAlpha(alpha);
        }
    }

}
