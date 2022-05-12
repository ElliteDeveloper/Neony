package com.ptnst.neon.neon.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptnst.neon.neon.PurchaseStickerActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.model.StickerCateData;
import com.ptnst.neon.neon.utils.GlobalConst;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class PanelStickers extends PanelBase {



    StickerCallBack mCallBack;

    ViewPager mViewPager;
    CircleIndicator mIndicator;
    List<StickerCateData> mListStickers;
    LinearLayout mLayoutCate;
    LinearLayout mLayoutPage1;
    LinearLayout mLayoutPage2;

    TextView mTxtCount;
    ImageView mImgSample;
    TextView mTxtView;
    TextView mTxtPanelName;



    public PanelStickers(Context context) {
        super(context);
    }

    public PanelStickers(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelStickers(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface StickerCallBack{
        public void onSelect(Bitmap bitmap);
    }



    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_stickers, null);
        addView(mRootView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.findViewById(R.id.img_close_panel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCallBack.onClose();
            }
        });

        mTxtCount = mRootView.findViewById(R.id.txt_count);
        mImgSample = mRootView.findViewById(R.id.img_sample);
        mTxtView = mRootView.findViewById(R.id.btn_view);
        mTxtPanelName = mRootView.findViewById(R.id.txt_pack_name);
        mLayoutCate = mRootView.findViewById(R.id.layout_category);
        mLayoutPage1 = mRootView.findViewById(R.id.layout_page1);
        mLayoutPage2 = mRootView.findViewById(R.id.layout_page2);
        mViewPager = mRootView.findViewById(R.id.view_pager);
        mIndicator = mRootView.findViewById(R.id.indicator);

        mListStickers = new ArrayList<>();
        Gson gson = new Gson();
        try {
            InputStream open = mContext.getAssets().open("stickers.json");
            String readStream = readStream(open);
            Type listType = new TypeToken<List<StickerCateData>>(){}.getType();

            mListStickers = gson.fromJson(readStream, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //check purchased
        Type listType = new TypeToken<List<StickerCateData>>(){}.getType();
        String stickerPurchased = GlobalConst.getPurchasedStickers(getContext());
        List<StickerCateData> listSaved = null;
        if (stickerPurchased != null && !stickerPurchased.equals("")){
            listSaved = gson.fromJson(stickerPurchased, listType);
        }
        for (int i = 0; i<mListStickers.size(); i++){
            StickerCateData data= mListStickers.get(i);
            if (data.purchase == 0){
                if (GlobalConst.mTryStickerPosition[i] == 1){
                    data.purchase = 1;
                }else if (listSaved != null){
                    try {
                        StickerCateData savedData = listSaved.get(i);
                        if (savedData.purchase == 1) {
                            data.purchase = savedData.purchase;
                        }
                    }catch (Exception e){

                    }
                }

            }
        }


        draw();
        selectItem(0);
    }

    public void setOnSelectListener(StickerCallBack callBack){
        mCallBack = callBack;
    }

    public void draw(){
        for (int i = 0; i<mListStickers.size(); i++){
            StickerCateData cate = mListStickers.get(i);
            View v = LayoutInflater.from(getContext()).inflate(R.layout.row_panel_sticker_image, null);
            v.setTag(cate);
            mLayoutCate.addView(v, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            final int pos = i;
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(pos);
                }
            });
        }


    }

    private void saveCurrentData(){

        String json = new Gson().toJson(mListStickers);
        GlobalConst.setPurcahsedSticker(getContext(), json);
    }

    public void buySticker(int pos){
        mListStickers.get(pos).purchase = 1;
        saveCurrentData();
        selectItem(pos);
    }

    public void tryFreeSticker(int pos){
        mListStickers.get(pos).purchase = 1;
        GlobalConst.mTryStickerPosition[pos] = 1;
        selectItem(pos);
    }


    public void selectItem(int pos){
        if (pos == mListStickers.size()){
            return;
        }



        for (int i = 0; i<mLayoutCate.getChildCount(); i++){
            View v = mLayoutCate.getChildAt(i);
            final StickerCateData data = (StickerCateData) v.getTag();
            ImageView imgThumb = v.findViewById(R.id.img_thumb);
            if (i == pos) {
                GlobalConst.showImageFromAsset(mContext, GlobalConst.STICKER_ASSET_PATH + data.path + "/s_icon_selected.png", imgThumb);
                if (data.purchase == 1){
                    mLayoutPage1.setVisibility(View.VISIBLE);
                    mLayoutPage2.setVisibility(View.INVISIBLE);
                    List<String> paths =  new ArrayList<>();
                    for (int j = 0; j<data.count; j++){
                        String path = data.path + "/" + String.format("s_%02d.png", j + 1);
                        paths.add(path);
                    }
                    mViewPager.setAdapter(new StickerPagerAdapter(getContext(), paths));
                    mIndicator.setViewPager(mViewPager);
                }else{
                    mLayoutPage1.setVisibility(View.INVISIBLE);
                    mLayoutPage2.setVisibility(View.VISIBLE);

                    mTxtCount.setText(String.format(mContext.getString(R.string.sticker_count), data.count));
                    mTxtPanelName.setText(data.name);
                    final int index = i;
                    mTxtView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PurchaseStickerActivity.class);
                            intent.putExtra("pos", index);
                            intent.putExtra("data", data);
                            ((Activity)mContext).startActivityForResult(intent, GlobalConst.REQUEST_BUY_STICKER);
                        }
                    });
                    mImgSample.setImageBitmap(null);
                    GlobalConst.showImageFromAsset(mContext, GlobalConst.STICKER_ASSET_PATH  + data.path + "/s_preview.png", mImgSample);
                }
            }else{
                GlobalConst.showImageFromAsset(mContext, GlobalConst.STICKER_ASSET_PATH + data.path + "/s_icon.png", imgThumb);


            }
        }


    }

    public String readStream(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        if (inputStream == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                try {
                    String readLine = bufferedReader2.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                } catch (IOException e) {

                } catch (Throwable t) {
                }
            }
            inputStream.close();
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e) {

        }
        return stringBuffer.toString();
    }


    public class StickerPagerAdapter extends PagerAdapter {

        Context mContext;
        List<String> mListData;

        public StickerPagerAdapter(Context context, List<String> listData) {
            mContext = context;
            mListData = listData;
        }



        @Override public int getCount() {
            int remain = mListData.size() % 4;
            if (remain == 0) {
                return mListData.size() / 4;
            }else{
                return mListData.size() / 4 + 1;
            }
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
            for (int i = 0; i<4; i++){
                int pos = position * 4 + i;
                if (pos < mListData.size()){
                    View v =  LayoutInflater.from(mContext).inflate(R.layout.row_panel_sticker_image, null);
                    final String item = mListData.get(pos);
                    ImageView imgView = v.findViewById(R.id.img_thumb);
                    GlobalConst.showImageFromAsset(mContext, GlobalConst.STICKER_ASSET_PATH + item, imgView);

                    v.findViewById(R.id.img_thumb).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCallBack.onSelect(getBitmapFromAsset(mContext, GlobalConst.STICKER_ASSET_FOLDER + "/" + item));
                        }
                    });
                    LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.weight = 1;
                    v.setLayoutParams(params);
                    layout.addView(v);
                }else {
                    View v =  LayoutInflater.from(mContext).inflate(R.layout.row_panel_sticker_image, null);
                    LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.weight = 1;
                    v.setLayoutParams(params);
                    v.setVisibility(View.INVISIBLE);
                    layout.addView(v);
                }
            }

            view.addView(layout);
            return layout;
        }
        public Bitmap getBitmapFromAsset(Context context, String filePath) {
            AssetManager assetManager = context.getAssets();

            InputStream istr;
            Bitmap bitmap = null;
            try {
                istr = assetManager.open(filePath);
                bitmap = BitmapFactory.decodeStream(istr);
            } catch (IOException e) {
                // handle exception
            }

            return bitmap;
        }

    }




}
