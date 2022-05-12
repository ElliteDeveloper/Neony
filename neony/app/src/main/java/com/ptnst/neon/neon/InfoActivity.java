package com.ptnst.neon.neon;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.ptnst.neon.neon.dialog.SelectRatioDialog;

public class InfoActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        loadBannerAd();
        initView();

    }
    private void initView(){
        findViewById(R.id.layout_item1).setOnClickListener(this);
        findViewById(R.id.layout_item2).setOnClickListener(this);
        findViewById(R.id.layout_item3).setOnClickListener(this);
        findViewById(R.id.layout_item4).setOnClickListener(this);
        findViewById(R.id.layout_item5).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        TextView txtTitle = findViewById(R.id.txt_title_large);
        txtTitle.setText(R.string.info);
    }



    @SuppressLint("StringFormatInvalid")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();
        }else if (v.getId() == R.id.layout_item1){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.layout_item2){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }else if (v.getId() == R.id.layout_item3){
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.recomment_title));
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    String.format(getString(R.string.recomment_content),
                            "http://play.google.com/store/apps/details?id=" + getPackageName()));

            Intent chooserIntent = Intent.createChooser(shareIntent, getString(R.string.recommend_friend));
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooserIntent);
        }else if (v.getId() == R.id.layout_item4){
            Uri uri = Uri.parse("http://instagram.com/_u/neony.app");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/neony.app")));
            }
        }else if (v.getId() == R.id.layout_item5){
            Intent intent = new Intent(this, TextOnlyActivity.class);
            startActivity(intent);
        }
    }



}
