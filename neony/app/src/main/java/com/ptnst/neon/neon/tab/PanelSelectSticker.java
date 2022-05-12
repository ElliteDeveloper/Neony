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

public class PanelSelectSticker extends PanelBase {

    int MAX_SIZE = 500;
    int MIN_SIZE = 50;

    int mBlur = 0;
    int mSize = 50;
    int mRotate = 0;
    int mShadow = 50;
    int mInner = 0;
    ChangeIntValueCallBack mBlurCallBack;
    ChangeIntValueCallBack mSizeCallBack;
    ChangeIntValueCallBack mRotateCallBack;
    ChangeIntValueCallBack mShadowCallBack;
    ChangeIntValueCallBack mInnerCallBack;
    SeekBar mSeekBlur;
    SeekBar mSeekSize;
    SeekBar mSeekRotate;
    TextView mTxtBlur;
    TextView mTxtSize;
    TextView mTxtRotate;
    ImageView mImgColor;
    SeekBar mSeekShadow;
    TextView mTxtShadow;
    public TextView mTxtDelete;
    SeekBar mSeekInner;
    TextView mTxtInner;

    public PanelSelectSticker(Context context) {
        super(context);
    }

    public PanelSelectSticker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelSelectSticker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_select_sticker, null);
        addView(mRootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.img_close_panel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCallBack.onClose();
            }
        });
        mSeekBlur = mRootView.findViewById(R.id.sb_blur);
        mSeekSize = mRootView.findViewById(R.id.sb_size);
        mSeekRotate = mRootView.findViewById(R.id.sb_rotate);
        mTxtBlur = mRootView.findViewById(R.id.txt_blur);
        mTxtSize = mRootView.findViewById(R.id.txt_size);
        mTxtRotate = mRootView.findViewById(R.id.txt_rotate);
        mImgColor = mRootView.findViewById(R.id.img_neon_color);
        mSeekShadow = mRootView.findViewById(R.id.sb_shadow);
        mTxtShadow = mRootView.findViewById(R.id.txt_shadow);
        mTxtDelete = mRootView.findViewById(R.id.txt_delete);
        mSeekInner = mRootView.findViewById(R.id.sb_inner);
        mTxtInner = mRootView.findViewById(R.id.txt_inner);

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

        mSeekSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mSize = progress - 50;
                if (mSize == 0){
                    mSize = 100;
                }else if (mSize > 0){ // 0 - 50 (100 ~ 500)
                    mSize = 100 + mSize * (MAX_SIZE - 100) / 50;
                }else{  // 20 - 100 ( -80 ~ 0)
                    mSize = 100 + mSize * (100 - MIN_SIZE) / 50;
                }

                mSizeCallBack.onChangeValue(mSize);
                changeSizeText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRotate = progress  * 5 - 180;
                mRotateCallBack.onChangeValue(mRotate);
                changeRotateText();
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
                ((BaseActivity)getContext()).startActivityForResult(intent, GlobalConst.REQUEST_STICKER_NEON_COLOR);
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

    private void changeBlurText(){
        mTxtBlur.setText(String.format("%d", mBlur * 5) + "%");
    }

    public void setSize(int size){
        mSize = size;
        int percent = size - 100; // 20 - 500
        if (percent == 0){
            percent = 50;
        }else if (percent > 0){ // 100 - 500 (0 ~ 400)
            percent = 50 + percent * 50 / (MAX_SIZE - 100);
        }else{  // 20 - 100 ( -80 ~ 0)
            percent = 50 + percent * 50 / (100 - MIN_SIZE);
        }
        changeSizeText();
        mSeekSize.setProgress(percent);
    }

    private void changeSizeText(){
        mTxtSize.setText(String.format("%.02f", ((float)mSize) / 100.0f ) + " x");
    }

    public void setRotate(int rotate){
        mRotate = rotate ;
        changeRotateText();
        mSeekRotate.setProgress(mRotate / 5 + 36);
    }

    public void setShadow(int shadow){
        mShadow = shadow;
        changeShadowText();
        mSeekShadow.setProgress(mShadow);
    }

    private void changeShadowText(){
        mTxtShadow.setText(String.format("%d", mShadow) + "%");
    }

    private void changeRotateText(){
        mTxtRotate.setText(String.format("%d°", mRotate));
    }

    public void setOnChangeBlur(ChangeIntValueCallBack callBack){
        mBlurCallBack = callBack;
    }

    public void setOnChangeSize(ChangeIntValueCallBack callBack){
        mSizeCallBack = callBack;
    }

    public void setOnChangeRotate(ChangeIntValueCallBack callBack){
        mRotateCallBack = callBack;
    }

    public void setOnChangeShadow(ChangeIntValueCallBack callBack){
        mShadowCallBack = callBack;
    }

    public void setOnChangeInner(ChangeIntValueCallBack callBack){
        mInnerCallBack = callBack;
    }

    public void setInnter(int blur){
        mInner = blur;
        changeInnerText();
        mSeekInner.setProgress(mInner);
    }

    private void changeInnerText(){
        mTxtInner.setText(String.format("%d", mInner * 5) + "%");
    }
}
