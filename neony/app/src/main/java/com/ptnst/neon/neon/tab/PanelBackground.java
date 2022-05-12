package com.ptnst.neon.neon.tab;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ptnst.neon.neon.BaseActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.SelectColorActivity;
import com.ptnst.neon.neon.utils.GlobalConst;

import me.relex.circleindicator.CircleIndicator;

public class PanelBackground extends PanelBase {

    int mBright = 50;
    ChangeIntValueCallBack mBrightCallBack;
    ChangeIntValueCallBack mColorCallBack;
    SeekBar mSeekBright;
    TextView mTxtBright;
    ViewPager mViewPager;
    CircleIndicator mIndicator;

    public PanelBackground(Context context) {
        super(context);
    }

    public PanelBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_background, null);
        addView(mRootView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.img_close_panel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCallBack.onClose();
            }
        });
        mSeekBright = mRootView.findViewById(R.id.sb_bright);
        mTxtBright = mRootView.findViewById(R.id.txt_bright);
        mViewPager = mRootView.findViewById(R.id.view_pager);
        mIndicator = mRootView.findViewById(R.id.indicator);
        mSeekBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBright = progress;
                mBrightCallBack.onChangeValue(progress);
                changeBrightText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mRootView.findViewById(R.id.txt_view_all).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectColorActivity.class);
                ((BaseActivity)getContext()).startActivityForResult(intent, GlobalConst.REQUEST_COLOR);
            }
        });
        mViewPager.setAdapter(new ColorPagerAdapter(getContext()));
        mIndicator.setViewPager(mViewPager);
    }

    public void setBright(int bright){
        mBright = bright;
        changeBrightText();
        mSeekBright.setProgress(mBright);
    }

    private void changeBrightText(){
        mTxtBright.setText(String.format("%d", mBright) + "  %");
    }

    public void setOnChangeBright(ChangeIntValueCallBack brightCallback){
        mBrightCallBack = brightCallback;
    }

    public void setOnChangeColor(ChangeIntValueCallBack colorCallback){
        mColorCallBack = colorCallback;
    }

    public class ColorPagerAdapter extends PagerAdapter {

        Context mContext;

        public ColorPagerAdapter(Context context) {
            mContext = context;
        }



        @Override public int getCount() {
            return GlobalConst.BACKGROUND_COLORS.length / 5;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup view, int position) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i<5; i++){
                View v =  LayoutInflater.from(mContext).inflate(R.layout.row_panel_color_image, null);
                final int color = GlobalConst.BACKGROUND_COLORS[position * 5 + i];
                v.findViewById(R.id.img_thumb).setBackgroundColor(color);
                v.findViewById(R.id.img_thumb).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mColorCallBack.onChangeValue(color);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                v.setLayoutParams(params);
                layout.addView(v);
            }
            view.addView(layout);
            return layout;
        }


    }
}
