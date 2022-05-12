package com.ptnst.neon.neon;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TextOnlyActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        loadBannerAd();
        initView();

    }
    private void initView(){

        findViewById(R.id.img_back).setOnClickListener(this);

        TextView txtTitle = findViewById(R.id.txt_title_large);
        txtTitle.setText(R.string.open_source_library);

        TextView txtContent = findViewById(R.id.txt_content);
        txtContent.setText(R.string.license_text);

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();

        }
    }



}
