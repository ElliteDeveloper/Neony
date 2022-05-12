package com.ptnst.neon.neon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class AspectFrameLayout extends FrameLayout {
    private double mTargetAspect = -1.0d;

    public AspectFrameLayout(Context context) {
        super(context);
    }

    public AspectFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setAspectRatio(double d) {
        if (d < 0.0d) {
            throw new IllegalArgumentException();
        }
        if (this.mTargetAspect != d) {
            this.mTargetAspect = d;
            requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int size2;
        if (this.mTargetAspect > 0.0d) {
            size = MeasureSpec.getSize(widthMeasureSpec);
            int paddingLeft = getPaddingLeft() + getPaddingRight();
            int paddingTop = getPaddingTop() + getPaddingBottom();
            size -= paddingLeft;
            size2 = MeasureSpec.getSize(heightMeasureSpec) - paddingTop;
            double d = (double) size;
            double d2 = (double) size2;
            double d3 = (mTargetAspect / (d / d2)) - 1.0d;
            if (Math.abs(d3) < 0.01d) {

            } else {
                if (d3 > 0.0d) {
                    size2 = (int) (d / mTargetAspect);
                } else {
                    size = (int) (d2 * mTargetAspect);
                }

                size2 += paddingTop;
                size = MeasureSpec.makeMeasureSpec(size + paddingLeft, MeasureSpec.EXACTLY );
                size2 = MeasureSpec.makeMeasureSpec(size2, MeasureSpec.EXACTLY );
                super.onMeasure(size, size2);
                return;
            }
        }
        size = widthMeasureSpec;
        size2 = heightMeasureSpec;
        super.onMeasure(size, size2);
    }
}
