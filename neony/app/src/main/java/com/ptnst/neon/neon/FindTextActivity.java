package com.ptnst.neon.neon;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ptnst.neon.neon.utils.FindTextListAdapter;

public class FindTextActivity extends BaseActivity implements View.OnClickListener{

    TextView mTxtTitle;
    int mLevel = 0;
    ListView mListView;
    FindTextListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_text);


        initView();

    }
    private void initView(){
        findViewById(R.id.img_back).setOnClickListener(this);
        mListView = findViewById(R.id.list_view);

        loadBannerAd();

        mTxtTitle = findViewById(R.id.txt_title_large);
        setList(-1, "");
    }

    public void setList(int level, String title){
        mLevel = level;
        mTxtTitle.setText(title);
        if (mLevel == -1){
            String[] arrTopics = getResources().getStringArray(R.array.topics);
            mAdapter = new FindTextListAdapter(this, arrTopics, mLevel);
            mListView.setAdapter(mAdapter);
        }else{
            String[] arrTopics = getResources().getStringArray(getResources().getIdentifier("topic_" + (level + 1), "array", getPackageName()));
            mAdapter = new FindTextListAdapter(this, arrTopics, mLevel);
            mListView.setAdapter(mAdapter);
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            if (mLevel < 0){
                onBackPressed();
            }else{
                setList(-1, "");
            }
        }
    }



}
