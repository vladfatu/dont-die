package com.tacticsgames.dontdie.renderer;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tacticsgames.dontdie.R;

/**
 * Created by Vlad on 10-Mar-16.
 */
public class TutorialRenderer {

    private Activity activity;
    private TutorialRendererListener tutorialRendererListener;

    private int tutorialPage;

    private RelativeLayout tutorialLayout;
    private ImageView tutorialBackground;
    private ImageButton tutorialNextButton;
    private ImageButton tutorialSkipButton;
    private ImageButton tutorialStartButton;

    public TutorialRenderer(Activity activity, TutorialRendererListener tutorialRendererListener) {
        this.activity = activity;
        this.tutorialRendererListener = tutorialRendererListener;
        tutorialPage = 1;
        initialiseViews();
        initialiseClickListeners();
    }

    private void initialiseViews() {
        tutorialLayout = RelativeLayout.class.cast(activity.findViewById(R.id.tutorialLayout));
        tutorialBackground = ImageView.class.cast(activity.findViewById(R.id.tutorialBackground));
        tutorialNextButton = ImageButton.class.cast(activity.findViewById(R.id.tutorialNextButton));
        tutorialSkipButton = ImageButton.class.cast(activity.findViewById(R.id.tutorialSkipButton));
        tutorialStartButton = ImageButton.class.cast(activity.findViewById(R.id.tutorialStartButton));
    }

    private void initialiseClickListeners() {
        tutorialNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorialNextButtonClicked();
            }
        });
        tutorialSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorialSkipButtonClicked();
            }
        });
        tutorialStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorialStartButtonClicked();
            }
        });
    }

    public void startTutorial() {
        tutorialLayout.setVisibility(View.VISIBLE);
    }

    private void onTutorialNextButtonClicked() {
        if (tutorialPage < 2) {
            tutorialPage++;
            tutorialBackground.setImageResource(R.mipmap.tutorial_chalkboard_second);
        } else {
            tutorialBackground.setImageResource(R.mipmap.tutorial_chalkboard_third);
            showStartButton();
        }
    }

    private void onTutorialSkipButtonClicked() {
        closeTutorial();
    }

    private void onTutorialStartButtonClicked() {
        closeTutorial();
    }

    private void showStartButton() {
        tutorialStartButton.setVisibility(View.VISIBLE);
        tutorialNextButton.setVisibility(View.GONE);
        tutorialSkipButton.setVisibility(View.GONE);
    }

    private void closeTutorial() {
        tutorialLayout.setVisibility(View.GONE);
        tutorialRendererListener.onTutorialFinished();
    }

    public interface TutorialRendererListener {
        void onTutorialFinished();
    }
}
