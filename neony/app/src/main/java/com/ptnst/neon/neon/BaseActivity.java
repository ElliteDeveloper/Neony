package com.ptnst.neon.neon;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BaseActivity extends AppCompatActivity {
    AdView mAdView;
    public void loadBannerAd(){
        AdRequest adRequest = new AdRequest.Builder().build();//.addTestDevice("55A65AD550C58702609E8BF0A0F1E3FF").build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
    }

}
