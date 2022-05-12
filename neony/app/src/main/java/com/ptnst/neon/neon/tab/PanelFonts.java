package com.ptnst.neon.neon.tab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptnst.neon.neon.EditActivity;
import com.ptnst.neon.neon.PurchaseFontActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.model.FontCateData;
import com.ptnst.neon.neon.model.FontData;
import com.ptnst.neon.neon.utils.ApiClient;
import com.ptnst.neon.neon.utils.DownloadProgressInterceptor;
import com.ptnst.neon.neon.utils.GlobalConst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanelFonts extends PanelBase {



    FontCallBack mCallBack;

    ViewPager mViewPager;
    CircleIndicator mIndicator;
    List<FontCateData> mListCate;
    LinearLayout mLayoutCate;

    TextView mTxtCount;
    ImageView mImgSample;
    TextView mTxtView;
    TextView mTxtPanelName;


    View mSelectView;

    public PanelFonts(Context context) {
        super(context);
    }

    public PanelFonts(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PanelFonts(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface FontCallBack{
        public void onSelect(Typeface typeface);
    }



    public void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.panel_fonts, null);
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
        mViewPager = mRootView.findViewById(R.id.view_pager);
        mIndicator = mRootView.findViewById(R.id.indicator);

        mListCate = new ArrayList<>();
        Gson gson = new Gson();
        try {
            InputStream open = mContext.getAssets().open("fonts.json");
            String readStream = readStream(open);
            Type listType = new TypeToken<List<FontCateData>>(){}.getType();
            mListCate = gson.fromJson(readStream, listType);


            String fontPurchased = GlobalConst.getPurchasedFonts(getContext());
            List<FontCateData> listSaved = null;
            if (fontPurchased != null && !fontPurchased.equals("")){
                listSaved = gson.fromJson(fontPurchased, listType);
            }





            for (int i = 0; i<mListCate.size(); i++){
                FontCateData cate = mListCate.get(i);
                for (int j= 0; j<cate.fonts.size(); j++){
                    FontData data = cate.fonts.get(j);
                    data.lang = cate.lang;
                    if (listSaved != null){
                        try {
                            FontData savedData = listSaved.get(i).fonts.get(j);
                            if (savedData.purchase == 1) {
                                data.purchase = savedData.purchase;
                            }
                            if (savedData.need_download == 2) {
                                data.need_download = savedData.need_download;
                                data.download_url = savedData.download_url;
                            }
                        }catch (Exception e){

                        }
                    }

                    //todo : already purchase process
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        draw();
        selectItem(0);
    }

    public void setOnSelectListener(FontCallBack callBack){
        mCallBack = callBack;
    }

    public void draw(){
        for (int i = 0; i<mListCate.size(); i++){
            FontCateData cate = mListCate.get(i);
            View v = LayoutInflater.from(getContext()).inflate(R.layout.row_panel_font_cate, null);
            v.setTag(cate);
            TextView txtCate = v.findViewById(R.id.txt_cate);
            txtCate.setText(mListCate.get(i).lang);
            mLayoutCate.addView(v, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

        String json = new Gson().toJson(mListCate);
        GlobalConst.setPurcahsedFont(getContext(), json);
    }

    public void buyFont(int type, int pos, int value){
        FontData data = mListCate.get(type).fonts.get(pos);
        data.purchase = value;
        saveCurrentData();
//        selectItem(pos);
        TextView txtBuy = mSelectView.findViewById(R.id.txt_buy);

        if (data.need_download == 1){
            txtBuy.setVisibility(View.VISIBLE);
            txtBuy.setText(R.string.download);
            txtBuy.setBackgroundResource(R.drawable.btn_ellipse_white_border_small);

        }else{
            txtBuy.setVisibility(View.GONE);
        }

    }



    public void selectItem(int pos){
        if (pos == mListCate.size()){
            return;
        }



        for (int i = 0; i < mLayoutCate.getChildCount(); i++){
            View v = mLayoutCate.getChildAt(i);
            final FontCateData data = (FontCateData) v.getTag();
            TextView txtCate = v.findViewById(R.id.txt_cate);
            if (i == pos) {
                txtCate.setBackgroundColor(mContext.getResources().getColor(R.color.colorPink));
                List<FontData> fonts =  data.fonts;
                mViewPager.setAdapter(new FontPagerAdapter(getContext(), fonts , i));
                mIndicator.setViewPager(mViewPager);

            }else{
                txtCate.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));


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


    public class FontPagerAdapter extends PagerAdapter {

        Context mContext;
        List<FontData> mListData;
        int mCateNo = 0;
        int margin;
        public FontPagerAdapter(Context context, List<FontData> listData, int cateNo) {
            mContext = context;
            mListData = listData;
            margin = getResources().getDimensionPixelSize(R.dimen.theme_spacing);
            mCateNo = cateNo;
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
                final int pos = position * 4 + i;
                if (pos < mListData.size()) {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.row_panel_font, null);
                    final RelativeLayout layoutItem = v.findViewById(R.id.layout_item);
                    layoutItem.setBackgroundResource(R.drawable.btn_gray_border_round);
                    final FontData item = mListData.get(pos);
                    ImageView imgFont = v.findViewById(R.id.img_font);
                    TextView txtBuy = v.findViewById(R.id.txt_buy);
                    GlobalConst.showImageFromAsset(mContext, GlobalConst.FONT_ASSET_PATH + item.lang + "/" + item.path + "/" + item.icon, imgFont);
                    if (item.purchase == 1) { //default purchased

                        if (item.need_download == 1){
                            txtBuy.setVisibility(View.VISIBLE);
                            txtBuy.setText(R.string.download);
                            txtBuy.setBackgroundResource(R.drawable.btn_ellipse_white_border_small);
                        }else{
                            txtBuy.setVisibility(View.GONE);
                        }
                    } else { //buy
                        txtBuy.setVisibility(View.VISIBLE);
                        txtBuy.setText(R.string.buy);
                        txtBuy.setBackgroundResource(R.drawable.btn_ellipse_pink_small);

                    }
                    layoutItem.findViewById(R.id.layout_item).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            layoutItem.setBackgroundResource(R.drawable.btn_pink_border_round);
                            //                       mCallBack.onSelect(getBitmapFromAsset(mContext, GlobalConst.STICKER_ASSET_FOLDER + "/" + item));

                            if (item.purchase == 1){
                                if (item.need_download == 0) {
                                    AssetManager am = mContext.getAssets();
                                    Typeface typeface = Typeface.createFromAsset(am, GlobalConst.FONT_ASSET_FOLDER + "/" + item.lang + "/" +  item.path + "/" +  item.file);
                                    mCallBack.onSelect(typeface);
                                }else if (item.need_download == 1){
                                    mSelectView = v;
                                    downloadFont(item, v);
                                }else {
                                    try {
                                        Typeface typeface = Typeface.createFromFile(item.download_url);
                                        if (typeface != null) {
                                            mCallBack.onSelect(typeface);
                                            return;
                                        }
                                    }catch (Exception e){

                                    }
                                }

                            }else if (item.purchase == 0){
                                mSelectView = v;
                                Intent intent = new Intent(mContext, PurchaseFontActivity.class);
                                intent.putExtra("cateNo", mCateNo);
                                intent.putExtra("fontNo", pos);
                                intent.putExtra("data", item);
                                ((EditActivity)mContext).startActivityForResult(intent, GlobalConst.REQUEST_BUY_FONT);
                            }
                        }
                    });
                    LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.weight = 1;
                    v.setLayoutParams(params);

                    layout.addView(v);
                }else{
                    View v = LayoutInflater.from(mContext).inflate(R.layout.row_panel_font, null);
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

        private void downloadFont(final FontData data, final View selectView){
            final TextView txtBuy = selectView.findViewById(R.id.txt_buy);
            final ProgressBar progressBar = selectView.findViewById(R.id.progress_view);
            txtBuy.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.getIndeterminateDrawable().setColorFilter(getContext().getResources().getColor(R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
            DownloadProgressInterceptor.DownloadProgressListener listener = new DownloadProgressInterceptor.DownloadProgressListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    if (progressBar != null && progressBar.isAttachedToWindow()){
                        Log.e("font", "progress=" + (100 * bytesRead / contentLength));
                        progressBar.setProgress((int) (100 * bytesRead / contentLength));
                    }
                }
            };
            ApiClient.getDownloadApiClient(listener).downloadFont(data.download_url).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ((EditActivity)mContext).hideProgress();
                    if (response.isSuccessful()){
                        final ResponseBody body = response.body();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                InputStream in = body.byteStream();

                                if (in == null){
                                    return;
                                }
                                String fontFileName =  data.name + ".ttf";
                                final File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH, fontFileName);
                                FileOutputStream fos = null;

                                long count = 0L;
                                try {
                                    fos = new FileOutputStream(file);
                                    byte[] buffer = new byte[1024 * 4];
                                    for (int n; -1 != (n = in.read(buffer)); count += (long) n) {
                                        fos.write(buffer, 0, n);
                                        final long progress = count;

                                    }
                                    data.download_url = file.getAbsolutePath();
                                    data.need_download = 2;
                                    saveCurrentData();
                                    in.close();
                                    fos.close();
                                    ((EditActivity) mContext).runOnUiThread(new Runnable() {
                                        @TargetApi(Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void run() {
                                            if (selectView.isAttachedToWindow()) {
                                                txtBuy.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    return;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                            }
                        }).start();


                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (selectView.isShown()) {
                        txtBuy.setVisibility(View.VISIBLE);

                    }
                }
            });
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
