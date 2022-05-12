package com.ptnst.neon.neon.tab;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ptnst.neon.neon.BaseActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.SelectColorActivity;
import com.ptnst.neon.neon.utils.GlobalConst;

public class PanelNeonColor extends PanelBase {

    int mBlur = 0;
    int mShadow = 50;
    int mInner = 0;
    ChangeIntValueCallBack mBlurCallBack;
    ChangeIntValueCallBack mShadowCallBack;
    ChangeIntValueCallBack mInnerCallBack;
    SeekBar mSeekBlur;
    TextView mTxtBlur;
    ImageView mImgColor;
    SeekBar mSeekShadow;
    TextView mTxtShadow;
    SeekBar mSeekInner;
    TextView mTxtInner;

    public PanelNeonColor(Context context) {
        super(context);
    }

    public PanelNeonColor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelNeonColor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_neon_color, null);
        addView(mRootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.img_close_panel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCallBack.onClose();
            }
        });
        mSeekBlur = mRootView.findViewById(R.id.sb_blur);
        mTxtBlur = mRootView.findViewById(R.id.txt_blur);
        mSeekShadow = mRootView.findViewById(R.id.sb_shadow);
        mSeekInner = mRootView.findViewById(R.id.sb_inner);
        mTxtInner = mRootView.findViewById(R.id.txt_inner);
        mTxtShadow = mRootView.findViewById(R.id.txt_shadow);
        mImgColor = mRootView.findViewById(R.id.img_neon_color);

        mSeekBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBlur = progress;
                mBlurCallBack.onChangeValue(progress);
                changeBlurText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekShadow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShadow = progress;
                mShadowCallBack.onChangeValue(progress);
                changeShadowText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekInner.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mInner = progress;
                mInnerCallBack.onChangeValue(progress);
                changeInnerText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mRootView.findViewById(R.id.img_neon_color).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectColorActivity.class);
                ((BaseActivity)getContext()).startActivityForResult(intent, GlobalConst.REQUEST_NEON_COLOR);
            }
        });

    }

    public void setColor(int color){
        mImgColor.setBackgroundColor(color);
    }

    public void setBlur(int blur){
        mBlur = blur;
        changeBlurText();
        mSeekBlur.setProgress(mBlur);
    }

    public void setShadow(int shadow){
        mShadow = shadow;
        changeShadowText();
        mSeekShadow.setProgress(mShadow);
    }

    public void setInnter(int blur){
        mInner = blur;
        changeInnerText();
        mSeekInner.setProgress(mInner);
    }

    private void changeInnerText(){
        mTxtInner.setText(String.format("%d", mInner * 5) + "%");
    }

    private void changeBlurText(){
        mTxtBlur.setText(String.format("%d", mBlur * 5) + "%");
    }

    private void changeShadowText(){
        mTxtShadow.setText(String.format("%d", mShadow) + "%");
    }

    public void setOnChangeBlur(ChangeIntValueCallBack callBack){
        mBlurCallBack = callBack;
    }

    public void setOnChangeInner(ChangeIntValueCallBack callBack){
        mInnerCallBack = callBack;
    }

    public void setOnChangeShadow(ChangeIntValueCallBack callBack){
        mShadowCallBack = callBack;
    }
}
