package com.ptnst.neon.neon;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        initView();

    }
    private void initView(){
        findViewById(R.id.img_back).setOnClickListener(this);
        TextView txtVersion = findViewById(R.id.txt_version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txtVersion.setText(String.format(getString(R.string.version), version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        TextView txtTitle = findViewById(R.id.txt_title_large);
        txtTitle.setText(R.string.about);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();
        }
    }



}
