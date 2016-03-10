package com.tacticsgames.dontdie.renderer;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tacticsgames.dontdie.R;

/**
 * Created by Vlad on 10-Mar-16.
 */
public class AdRenderer {

    private Activity activity;
    private InterstitialAd mInterstitialAd;

    public AdRenderer(Activity activity) {
        this.activity = activity;
        setupAd();
        requestNewInterstitial();
    }

    private void setupAd() {
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(activity.getResources().getString(R.string.interstitial_ad_unit_id));
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9A71D5CD236AB4E5565199A22CB660D")
                .addTestDevice("9C9C6C29924B4A4870047BD11841E1AF")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void showAd() {
        mInterstitialAd.show();
        requestNewInterstitial();
    }

}
