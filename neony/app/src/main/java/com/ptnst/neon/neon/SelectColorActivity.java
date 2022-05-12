package com.ptnst.neon.neon;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ptnst.neon.neon.utils.ColorGridAdapter;
import com.ptnst.neon.neon.utils.GlobalConst;
import com.ptnst.neon.neon.utils.ItemOffsetDecoration;

public class SelectColorActivity extends BaseActivity implements View.OnClickListener{


    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    GridLayoutManager mGridManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_color);

        initView();
        loadBannerAd();
    }
    private void initView(){

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mGridManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(mGridManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.color_spacing);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(spacingInPixels, 5);
        mRecyclerView.addItemDecoration(itemDecoration);

        mAdapter = new ColorGridAdapter(GlobalConst.BACKGROUND_COLORS);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.img_back).setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();
        }
    }


}

