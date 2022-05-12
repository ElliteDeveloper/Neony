package com.ptnst.neon.neon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

//import io.fabric.sdk.android.Fabric;

//import com.google.ads.mediation.tapjoy.TapjoyAdapter;
//import com.google.android.ads.mediationtestsuite.MediationTestSuite;


public class LogoActivity extends BaseActivity {
    private InterstitialAd mInterstitialAd;

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    ImageView mImgLogo;
    LinearLayout mLayoutBack;
    private boolean mIsNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_logo);
        mImgLogo = findViewById(R.id.img_logo);
        mLayoutBack = findViewById(R.id.layout_back);
        /*ViewAnimator
                .animate(mLayoutBack)
                .translationY(-1000, 0)
                .alpha(0, 1)
                .duration(1000)
                .thenAnimate(mImgLogo)
                .scale(1f, 0.5f, 1f)
                .accelerate()
                .duration(1000)
                .start();*/
//        MobileAds.initialize(this, getString(R.string.admob_app_id));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                loadAd();
            }
        });
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(LogoActivity.this);
                        }
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        gotoNext();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        gotoNext();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
//
//                        String error =
//                                String.format(
//                                        "domain: %s, code: %d, message: %s",
//                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
//                        Toast.makeText(
//                                LogoActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
//                                .show();
//                        Log.d("ttt", error);
                        gotoNext();
                    }
                });
    }


    private void gotoNext() {

        if (!mIsNext) {
            mIsNext = true;
            Intent mainIntent = new Intent(LogoActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
//            MediationTestSuite.launch(this, getResources().getString(R.string.admob_app_id));

        } else {

        }


    }


}
