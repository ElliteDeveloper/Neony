package com.ptnst.neon.neon.tab;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class PanelBase extends LinearLayout {
    View mRootView;
    CloseCallBack mCloseCallBack;
    Context mContext;

    public PanelBase(Context context) {
        super(context, null);
        mContext = context;
        init();
    }

    public PanelBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
        init();
    }

    public PanelBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init(){

    }

    public interface CloseCallBack{
        public void onClose();
    }

    public interface ChangeIntValueCallBack{
        public void onChangeValue(int value);
    }

    public interface ChangeFloatValueCallBack{
        public void onChangeValue(float value);
    }



    public void setOnCloseListener(CloseCallBack closeCallBack){
        mCloseCallBack = closeCallBack;
    }

}
