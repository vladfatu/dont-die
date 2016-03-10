package com.tacticsgames.dontdie.renderer;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.tacticsgames.dontdie.R;

/**
 * Created by Vlad on 07-Mar-16.
 */
public class CounterRenderer {

    private int startGameCounter;
    private TextView startGameCounterView;
    private CounterRendererListener counterRendererListener;

    public CounterRenderer(TextView startGameCounterView, CounterRendererListener counterRendererListener) {
        this.startGameCounterView = startGameCounterView;
        this.counterRendererListener = counterRendererListener;
    }

    public void showStartGameCounterAnimation() {
        startGameCounter = 3;
        startGameCounterView.setText("3");
        startGameCounterView.setVisibility(View.VISIBLE);
        updateStartGameCounterAnimation();
    }

    private void updateStartGameCounterAnimation() {
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
                    counterRendererListener.onCounterFinished();
                    startGameCounter--;
                    startGameCounterView.setText(R.string.go);
                    updateStartGameCounterAnimation();
                } else {
                    startGameCounter--;
                    startGameCounterView.setText(Integer.toString(startGameCounter));
                    updateStartGameCounterAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startGameCounterView.startAnimation(fadeIn);
    }

    public interface CounterRendererListener {
        void onCounterFinished();
    }

}
