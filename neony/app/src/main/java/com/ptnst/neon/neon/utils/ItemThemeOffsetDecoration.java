package com.ptnst.neon.neon.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class ItemThemeOffsetDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;

    public ItemThemeOffsetDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int divSpace = space / 2;
        if (position == 0){
            outRect.right = divSpace;
            outRect.left = 0;
            outRect.top = space;
            outRect.bottom = space;
        }else if (position == 1){
            outRect.right = 0;
            outRect.left = divSpace;
            outRect.top = space;
            outRect.bottom = space;
        }else{
            int _space = divSpace * 4 / spanCount;
            if (position % spanCount == 2){
                outRect.left = 0;
                outRect.right = _space;
            }else if (position % spanCount == 1){
                outRect.right = 0;
                outRect.left = _space;
            }else{
                outRect.right = _space / 2;
                outRect.left = _space / 2;
            }

            outRect.top = 0;
            outRect.bottom = space;
        }


    }
}
