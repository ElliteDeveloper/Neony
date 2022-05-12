package com.ptnst.neon.neon.tab;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.data.TextAttr;

public class PanelTextEffect extends PanelBase {

    int mAlign = Gravity.CENTER;
    int mSize = 20;
    int mRotate = 0;
    int mLineSpace;
    int mLetterSpace;
    ChangeIntValueCallBack mAlignCallBack;
    ChangeIntValueCallBack mSizeCallBack;
    ChangeIntValueCallBack mRotateCallBack;
    ChangeIntValueCallBack mLineSpaceCallBack;
    ChangeIntValueCallBack mLetterSpaceCallBack;
    SeekBar mSeekSize;
    SeekBar mSeekRotate;
    SeekBar mSeekLineSpace;
    SeekBar mSeekLetterSpace;
    TextView mTxtSize;
    TextView mTxtRotate;
    TextView mTxtLineSpace;
    TextView mTxtLetterSpace;

    public PanelTextEffect(Context context) {
        super(context);
    }

    public PanelTextEffect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelTextEffect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_text_effect, null);
        addView(mRootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.img_close_panel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCallBack.onClose();
            }
        });
        mSeekSize = mRootView.findViewById(R.id.sb_size);
        mSeekRotate = mRootView.findViewById(R.id.sb_rotate);
        mSeekLineSpace = mRootView.findViewById(R.id.sb_line_space);
        mSeekLetterSpace = mRootView.findViewById(R.id.sb_letter_space);
        mTxtSize = mRootView.findViewById(R.id.txt_size);
        mTxtRotate = mRootView.findViewById(R.id.txt_rotate);
        mTxtLineSpace = mRootView.findViewById(R.id.txt_line_space);
        mTxtLetterSpace = mRootView.findViewById(R.id.txt_letter_space);

        mSeekSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSize = progress;
                mSizeCallBack.onChangeValue(progress + 10);
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

        mSeekLineSpace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLineSpace = progress;
                mLineSpaceCallBack.onChangeValue(mLineSpace);
                changeLineSpaceText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekLetterSpace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLetterSpace = progress;
                mLetterSpaceCallBack.onChangeValue(mLetterSpace);
                changeLetterSpaceText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRootView.findViewById(R.id.img_left_align).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlignCallBack.onChangeValue(TextAttr.ALIGN_FONT_LEFT);
            }
        });
        mRootView.findViewById(R.id.img_center_align).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlignCallBack.onChangeValue(TextAttr.ALIGN_FONT_CENTER);
            }
        });
        mRootView.findViewById(R.id.img_right_align).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlignCallBack.onChangeValue(TextAttr.ALIGN_FONT_RIGHT);
            }
        });
    }

    public void setSize(int size){
        mSize = size - 10;
        changeSizeText();
        mSeekSize.setProgress(mSize);
    }

    private void changeSizeText(){
        mTxtSize.setText(String.format("%d", mSize + 10));
    }

    public void setRotate(int rotate){
        mRotate = rotate ;
        changeRotateText();
        mSeekRotate.setProgress(mRotate / 5 + 36);
    }

    private void changeRotateText(){
        mTxtRotate.setText(String.format("%d°", mRotate));
    }

    public void setLineSpace(int lineSpace){
        mLineSpace = lineSpace;
        changeLineSpaceText();
        mSeekLineSpace.setProgress(mLineSpace);
    }

    private void changeLineSpaceText(){
        mTxtLineSpace.setText(String.format("%.2f", ((float)mLineSpace)/100.0f));
    }

    public void setLetterSpace(int letterSpace){
        mLetterSpace = letterSpace;
        changeLetterSpaceText();
        mSeekLetterSpace.setProgress(mLetterSpace);
    }

    private void changeLetterSpaceText(){
        mTxtLetterSpace.setText(String.format("%.2f", ((float)mLetterSpace)/100.0f - 1.0f) );
    }

    public void setOnChangeAlign(ChangeIntValueCallBack callBack){
        mAlignCallBack = callBack;
    }

    public void setOnChangeSize(ChangeIntValueCallBack callBack){
        mSizeCallBack = callBack;
    }

    public void setOnChangeRotate(ChangeIntValueCallBack callBack){
        mRotateCallBack = callBack;
    }

    public void setOnChangeLineSpace(ChangeIntValueCallBack callBack){
        mLineSpaceCallBack = callBack;
    }

    public void setOnChangeLetterSpace(ChangeIntValueCallBack callBack){
        mLetterSpaceCallBack = callBack;
    }
}
