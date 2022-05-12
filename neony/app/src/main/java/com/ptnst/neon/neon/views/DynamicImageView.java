package com.ptnst.neon.neon.views;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

public class DynamicImageView extends AppCompatImageView {
    private float mAspectRatio = 1.0f;

    public void setAspectRatio(float ratio) {
        if (ratio < 0.5f) {
            ratio = 0.5f;
        }
        if (ratio > 2.0f) {
            ratio = 2.0f;
        }
        this.mAspectRatio = ratio;
        requestLayout();
    }

    public DynamicImageView(Context context) {
        super(context);
    }

    public DynamicImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DynamicImageView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (((float) width) / this.mAspectRatio));
    }
}
