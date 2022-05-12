package com.ptnst.neon.neon;

import android.animation.Animator;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ptnst.neon.neon.data.StickerData;
import com.ptnst.neon.neon.data.StyleData;
import com.ptnst.neon.neon.data.TextData;
import com.ptnst.neon.neon.dialog.NativeAdDialog;
import com.ptnst.neon.neon.dialog.SelectRatioDialog;
import com.ptnst.neon.neon.tab.PanelBackground;
import com.ptnst.neon.neon.tab.PanelBase;
import com.ptnst.neon.neon.tab.PanelFonts;
import com.ptnst.neon.neon.tab.PanelNeonColor;
import com.ptnst.neon.neon.tab.PanelSelectSticker;
import com.ptnst.neon.neon.tab.PanelStickers;
import com.ptnst.neon.neon.tab.PanelTextEffect;
import com.ptnst.neon.neon.utils.BitmapUtils;
import com.ptnst.neon.neon.utils.GlobalConst;
import com.ptnst.neon.neon.views.AspectFrameLayout;
import com.ptnst.neon.neon.views.BlurTextView;
import com.ptnst.neon.neon.views.ContainImageView;
import com.ptnst.neon.neon.views.NeonView;
import com.ptnst.neon.neon.views.OutGlowImageView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class EditActivity extends BaseActivity implements View.OnClickListener{

    public static final int TEXT_TOUCH_MODE = 1;
    public static final int STICKER_TOUCH_MODE = 2;

    String mFilePath;
    String mPrevText = "";
    boolean mIsShowMenu = false;
    BlurTextView mEditText;
    ContainImageView mImgContain;
    FrameLayout mFrameStickers;
    LinearLayout mLayoutTop;
    LinearLayout mLayoutTopWriteText;
    AspectFrameLayout mFrameRoot;
    LinearLayout mLayoutPanel;
    StickerData mSelectStickerData;
    FrameLayout mFrameAll;
    View mSelectStickerView;
    ProgressBar mProgressView;

    Unregistrar mUnregistrarKeyboard;
    int mTextPaddingSize;

    TextData mDataText;
    StyleData mDataStyle;
    Bitmap mBackGroundBitmap;

    List<StickerData> mDataStickers;
    TextView mTxtTab1;
    TextView mTxtTab2;
    TextView mTxtTab3;
    TextView mTxtTab4;
    TextView mTxtTab5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        }else {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/neony");
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }
    int mTextPadding;
    private void initView(){

        //--------------------hide find text----------------------------
        findViewById(R.id.txt_find_text).setVisibility(View.INVISIBLE);
        //--------------------------------------------------------------


        mEditText = findViewById(R.id.edit_text);
        mImgContain = findViewById(R.id.contain_image);
        mFrameStickers = findViewById(R.id.frame_stickers);
        mLayoutTop = findViewById(R.id.layout_top);
        mLayoutTopWriteText = findViewById(R.id.layout_top1);
        mFrameRoot = findViewById(R.id.frame_aspect);
        mLayoutPanel = findViewById(R.id.layout_panel);
        mFrameAll = findViewById(R.id.frame_edit);
        mFrameAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditFocus()){
                    mCloseCallBack.onClose();
                }
            }
        });

        mProgressView = findViewById(R.id.progress_view);
        mProgressView.setVisibility(View.INVISIBLE);
        mProgressView.setIndeterminate(true);
        mProgressView.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);

        mTxtTab1 = findViewById(R.id.txt_tab1);
        mTxtTab2 = findViewById(R.id.txt_tab2);
        mTxtTab3 = findViewById(R.id.txt_tab3);
        mTxtTab4 = findViewById(R.id.txt_tab4);
        mTxtTab5 = findViewById(R.id.txt_tab5);

        mLayoutTopWriteText.setVisibility(View.GONE);

        findViewById(R.id.txt_save).setOnClickListener(this);
        findViewById(R.id.txt_done).setOnClickListener(this);
        findViewById(R.id.txt_find_text).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
        findViewById(R.id.img_home).setOnClickListener(this);
        mTxtTab1.setOnClickListener(this);
        mTxtTab2.setOnClickListener(this);
        mTxtTab3.setOnClickListener(this);
        mTxtTab4.setOnClickListener(this);
        mTxtTab5.setOnClickListener(this);

        mUnregistrarKeyboard = KeyboardVisibilityEvent.registerEventListener(this, new KeyboardVisibilityEventListener() {
            public void onVisibilityChanged(boolean visible) {
                if (visible) {
                    mLayoutTopWriteText.setVisibility(View.VISIBLE);
                    mLayoutTop.setVisibility(View.GONE);
                    mEditText.setSelection(mEditText.length());
                    mPrevText = mEditText.getText().toString();
                }else {
                    mLayoutTopWriteText.setVisibility(View.GONE);
                    mLayoutTop.setVisibility(View.VISIBLE);
                    if (isEditFocus()) {
                        mEditText.clearFocus();
                    }
                }
            }
        });

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEditText.setBackgroundResource(R.drawable.edit_back);
                    mEditText.setHint("   ");
                    return;
                }
                if (mEditText.length() <= 0) {
//                    mEditText.setBackgroundColor(ContextCompat.getColor(EditActivity.this, R.color.blur2));
                    mEditText.setBackgroundResource(R.color.colorTransparent);
                    mEditText.setHint(R.string.text_here);
                    clearEditTextPos();
                } else {
                    mEditText.setBackgroundResource(R.color.colorTransparent);
                    mEditText.setHint("");
                }
                mDataText.setText(mEditText.getText().toString());
                applyTextData();
                applyStyleData();

            }
        });
        mEditText.setOnTouchListener(new ViewTouchListener(mEditText, TEXT_TOUCH_MODE));
    }



    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        mProgressView.setVisibility(View.INVISIBLE);
    }

    private void initData(){


        mTextPaddingSize = getResources().getDimensionPixelSize(R.dimen.text_padding);
        mDataText = new TextData();
        mDataStickers = new ArrayList();
        mDataStyle = new StyleData();


        mFilePath = getIntent().getStringExtra("filepath");
        int backColor = getIntent().getIntExtra("color", 0x00000000);
        float ratio = getIntent().getFloatExtra("ratio", 1.0f);

        if (mFilePath != null && !mFilePath.equals("")) {
            mBackGroundBitmap = loadBitmap();
            mImgContain.setImageBitmap(mBackGroundBitmap);
        }else{
            mDataStyle.setColor(backColor);
        }
        AssetManager am = getAssets();
        Typeface typeface = Typeface.createFromAsset(am, GlobalConst.FONT_ASSET_FOLDER + "/kor/kr_00/Neony_font.ttf");
        mDataText.setFont(typeface);

        applyTextData();
        applyStickerData();
        applyStyleData();

        mFrameRoot.setAspectRatio(ratio);

    }

    private void clearEditTextPos(){
        mEditText.setTranslationX(0);
        mEditText.setTranslationY(0);
        mDataText.setX((int) mEditText.getTranslationX());
        mDataText.setY((int) mEditText.getTranslationY());
    }


    @Override
    public void onBackPressed(){
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.quit_work)
                .setMessage(R.string.quit_work_comment)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();*/
        NativeAdDialog dlg = new NativeAdDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dlg.show();
    }




    private Bitmap loadBitmap(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            EditActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int wid = displayMetrics.widthPixels;
            int hei = displayMetrics.heightPixels;
            int GetExifOrientation = BitmapUtils.GetExifOrientation(mFilePath);
            int maxSize = BitmapUtils.CalculateMaxBitmapSize(wid, hei);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mFilePath, options);
            options.inSampleSize = BitmapUtils.CalculateInSampleSize(options, maxSize, maxSize);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(mFilePath, options);
            if (GetExifOrientation % 360 != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate((float) GetExifOrientation);
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                bitmap.recycle();
                bitmap = createBitmap;
            }
            return bitmap;
        }else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            EditActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int wid = displayMetrics.widthPixels;
            int hei = displayMetrics.heightPixels;
            int GetExifOrientation = BitmapUtils.GetExifOrientation(mFilePath);
            int maxSize = BitmapUtils.CalculateMaxBitmapSize(wid, hei);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mFilePath, options);
            options.inSampleSize = BitmapUtils.CalculateInSampleSize(options, maxSize, maxSize);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(mFilePath, options);
            if (GetExifOrientation % 360 != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate((float) GetExifOrientation);
                Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                bitmap.recycle();
                bitmap = createBitmap;
            }
            return bitmap;
        }
    }

    private void applyTextData() {
        mDataText.setText(mEditText.getText().toString());
        mDataText.applyTextView(mEditText);
    }

    private void applyStyleData() {
        int color = mDataStyle.getColor();
        int alpha = mDataStyle.getAlpha();
        int bright = mDataStyle.getBright();

        int translationX = (int) mEditText.getTranslationX();
        int translationY = (int) mEditText.getTranslationY();
        mImgContain.setStyleColor(color);
        mImgContain.setStyleAlpha(alpha);
        mImgContain.setStyleBright(bright);
        mImgContain.setTextDegree(mDataText.getDegree());
        mImgContain.setTextArea(mEditText.getLeft() + translationX, mEditText.getTop() + translationY, mEditText.getRight() + translationX, mEditText.getBottom() + translationY);
        mImgContain.invalidate();
    }

    private void applyStickerData() {
        if (mSelectStickerView != null && mSelectStickerData != null){
            mSelectStickerData.applySticker((OutGlowImageView) mSelectStickerView);
            mSelectStickerView.invalidate();
        }
    }


    private boolean isEditFocus() {
        View currentFocus = getCurrentFocus();
        return currentFocus != null && currentFocus.getId() == R.id.edit_text;
    }

    private void showSoftKeyInput() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
    }

    private boolean hideSoftKeyInput() {
        View currentFocus = getCurrentFocus();
        if (currentFocus == null || currentFocus.getId() != R.id.edit_text) {
            return false;
        }
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        mEditText.clearFocus();
        return true;
    }

    private void modifyOutofRectangleSticker() {
        for (int i = 0; i < mFrameStickers.getChildCount(); i++) {
//            modifyOutofRectangle(mFrameStickers.getChildAt(i));
        }
    }
    private void modifyOutofRectangle(View view) {
        Rect rect = getRect(view);
        int translationX = (int) view.getTranslationX();
        if (rect.left < (-mTextPaddingSize)) {
            view.setTranslationX((float) (translationX - (rect.left + mTextPaddingSize)));
        }
        if (rect.right > mImgContain.getWidth() + mTextPaddingSize) {
            view.setTranslationX((float) (translationX - ((rect.right - mImgContain.getWidth()) - mTextPaddingSize)));
        }
        translationX = (int) view.getTranslationY();
        if (rect.top < (-mTextPaddingSize)) {
            view.setTranslationY((float) (translationX - (rect.top + mTextPaddingSize)));
        }
        if (rect.bottom > mImgContain.getHeight() + mTextPaddingSize) {
            view.setTranslationY((float) (translationX - ((rect.bottom - mImgContain.getHeight()) - mTextPaddingSize)));
        }
    }
    private Rect getRect(View view) {
        return new Rect((int) (((float) view.getLeft()) + view.getTranslationX()), (int) (((float) view.getTop()) + view.getTranslationY()), (int) (((float) view.getRight()) + view.getTranslationX()), (int) (((float) view.getBottom()) + view.getTranslationY()));
    }

    private void selectSticker(View view, StickerData stickerData) {
        removeFocusSticker();
        view.setBackgroundResource(R.drawable.sticker_back);
        mSelectStickerView = view;
        mSelectStickerData = stickerData;
        setTab(5);
    }




    private class ViewTouchListener implements View.OnTouchListener {
        private int mEvent;
        private int mMode;
        private int mTranslationOldX = 0;
        private int mTranslationOldY = 0;
        private float mTranslationX;
        private float mTranslationY;
        private View mView;

        public ViewTouchListener(View view, int mode) {
            this.mView = view;
            this.mMode = mode;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("sticker", "translateX=" + mView.getTranslationX() + ", rawX = " + motionEvent.getRawX() + ", width" + mView.getWidth() );
                this.mTranslationX = this.mView.getTranslationX() - motionEvent.getRawX();
                this.mTranslationY = this.mView.getTranslationY() - motionEvent.getRawY();
                this.mTranslationOldX = (int) this.mView.getTranslationX();
                this.mTranslationOldY = (int) this.mView.getTranslationY();
                this.mEvent = 0;

                if (!isEditFocus() || this.mMode != TEXT_TOUCH_MODE) {
                    if (mIsShowMenu || motionEvent.getPointerCount() > 1) {
                        if (mMode == STICKER_TOUCH_MODE && mLayoutPanel.getChildCount() > 0){

                        }else{
                            mCloseCallBack.onClose();
                        }
                        return true;
                    }
                    return true;
                }


                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                Log.e("sticker move", "translateX=" + mView.getTranslationX() + ", rawX = " + motionEvent.getRawX() + ", width" + mView.getWidth() );
                int x = (int) (mTranslationX + motionEvent.getRawX());
                int y = (int) (mTranslationY + motionEvent.getRawY());


                if (mEvent == 0 && Math.abs(x - mTranslationOldX) < 15 && Math.abs(y - mTranslationOldY) < 15) {
                    return false;
                }

                if (isEditFocus() && mMode == TEXT_TOUCH_MODE) {
                    hideSoftKeyInput();
                    return false;
                }
                if (this.mMode == TEXT_TOUCH_MODE ) {
                    if (Math.abs(x) < 15) {
                        x = 0;
                    }
                    if (Math.abs(y) < 15) {
                        y = 0;
                    }
                }else if (this.mMode == STICKER_TOUCH_MODE){
                    /*
                    if (Math.abs(centerX - centerX1) < 15){
                        x = 0;//centerX - mView.getWidth() / 2;
                    }
                    if (Math.abs(centerY - centerY1) < 15){
                        y = 0;//centerY - mView.getHeight() / 2;
                    }
                    */

                    int centerX = (int)(mFrameRoot.getWidth() / 2);
                    int centerY = (int)(mFrameRoot.getHeight() / 2);
                    StickerData data = (StickerData)mView.getTag();
                    int wid = data.firstViewWidth / 2;
                    int hei = data.firstViewHeight / 2;
                    if (Math.abs(centerX - x - wid) < 15){
                        x = centerX - wid;
                    }
                    if (Math.abs(centerY - y - hei) < 15){
                        y = centerY - hei;
                    }
                }

                if (mMode == STICKER_TOUCH_MODE) {
                    mView.setBackgroundResource(R.drawable.sticker_back);
                }
                mView.setTranslationX(x);
                mView.setTranslationY(y);

                mView.invalidate();
                mImgContain.setMoveLine(this.mMode);
                if (mMode == TEXT_TOUCH_MODE) {
                    mImgContain.setTextDegree(mDataText.getDegree());
                    mImgContain.setTextArea(mView.getLeft() + x, mView.getTop() + y, mView.getRight() + x, mView.getBottom() + y);
                } else if (mMode == STICKER_TOUCH_MODE) {
                    mImgContain.setStickerTextArea(mView.getLeft() + x, mView.getTop() + y, mView.getRight() + x, mView.getBottom() + y);
                }
                mImgContain.invalidate();
                if (mEvent != 2  && mMode == STICKER_TOUCH_MODE && mLayoutPanel.getChildCount() > 0){
                    mCloseCallBack.onClose();
                }
                mEvent = 2;

            } else {
                if (mMode == TEXT_TOUCH_MODE) {
                    if (mEvent != 0) {
                        modifyOutofRectangle(mView);
                        mDataText.setX((int) mEditText.getTranslationX());
                        mDataText.setY((int) mEditText.getTranslationY());
                        applyTextData();
                    } else if (isEditFocus()) {
                        return false;
                    } else {

                        mView.requestFocus();
                        showSoftKeyInput();
                    }
                }else if (!(mMode != STICKER_TOUCH_MODE || mView.getTag() == null || !(mView.getTag() instanceof StickerData))) {
                    StickerData stickerData = (StickerData) mView.getTag();
                    if (mEvent == 0) {
                        selectSticker(mView, stickerData);
                    } else {
//                        modifyOutofRectangle(mView);
                        stickerData.setX((int) mView.getTranslationX());
                        stickerData.setY((int) mView.getTranslationY());
                        mView.setBackgroundColor(0x00000000);
                    }

                }else if (mMode == STICKER_TOUCH_MODE){
                    if (mEvent == 0){

                    }else {
                        mView.setBackgroundColor(0x00000000);
                    }
                }
                mTranslationX = -1.0f;
                mTranslationY = -1.0f;
                mEvent = 1;
                mImgContain.setMoveLine(-1);
                mImgContain.invalidate();
            }
            return true;
        }
    }

    private Bitmap getBitmapFromView(View view, Rect rect){
        if (!(rect == null || rect.isEmpty() || rect.width() <= 0)) {
            if (rect.height() > 0) {
                Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
                view.draw(new Canvas(bitmap));
                return bitmap;
            }
        }
        return null;

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        GlobalConst.initStickerFree();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_home){
            onBackPressed();
        }else if (v.getId() == R.id.img_close){
            mEditText.setText(mPrevText);
            hideSoftKeyInput();
        }else if (v.getId() == R.id.txt_save){
            if (!mIsShowMenu) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Bitmap bitmap = getBitmapFromView(mFrameAll, new Rect(0, 0, mFrameAll.getWidth(), mFrameAll.getHeight()));
                    long currentTimeMillis = System.currentTimeMillis();

                    ContentResolver resolver = getContentResolver();
                    Uri uri = GlobalConst.getImageUriFromDCIM(resolver, "neony_" + currentTimeMillis + ".jpg");
                    long count = 0;
                    try {
                        if (uri == null) {
                            Toast.makeText(this, "Failed to create new MediaStore record.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try (final OutputStream stream = resolver.openOutputStream(uri)) {
                            if (stream == null) {
                                Toast.makeText(this, "Failed to open output stream.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.close();
                            Intent intent = new Intent(this, ShareActivity.class);
                            intent.putExtra("filepath", uri.toString());
//                            intent.putExtra("filepath", GlobalConst.getImagePathUriFromDCIM(resolver, "neony_" + currentTimeMillis + ".jpg"));
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        if (uri != null) {
                            resolver.delete(uri, null, null);
                        }
                    }
                }else {
                    Bitmap bitmap = getBitmapFromView(mFrameAll, new Rect(0, 0, mFrameAll.getWidth(), mFrameAll.getHeight()));
                    long currentTimeMillis = System.currentTimeMillis();
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/neony", "neony_" + currentTimeMillis + ".jpg");
                    OutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Intent intent = new Intent(this, ShareActivity.class);
                        intent.putExtra("filepath", file.getAbsolutePath());
                        startActivity(intent);
                        finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if (v.getId() == R.id.txt_find_text){
            hideSoftKeyInput();
            Intent intent = new Intent(this, FindTextActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.txt_done){
            hideSoftKeyInput();
        }else if (v.getId() == R.id.txt_tab1){
            setTab(0);
        }else if (v.getId() == R.id.txt_tab2){
            setTab(1);
        }else if (v.getId() == R.id.txt_tab3){
            setTab(2);
        }else if (v.getId() == R.id.txt_tab4){
            setTab(3);
        }else if (v.getId() == R.id.txt_tab5){
            setTab(4);
        }
    }

    interface runCallback{
        public void show();
    }

    private void closeTab(final runCallback callback){
        mTxtTab1.setBackgroundResource(R.drawable.tab_back_off);
        mTxtTab2.setBackgroundResource(R.drawable.tab_back_off);
        mTxtTab3.setBackgroundResource(R.drawable.tab_back_off);
        mTxtTab4.setBackgroundResource(R.drawable.tab_back_off);
        mTxtTab5.setBackgroundResource(R.drawable.tab_back_off);

        mTxtTab1.setTextColor(getResources().getColor(R.color.colorTabText));
        mTxtTab2.setTextColor(getResources().getColor(R.color.colorTabText));
        mTxtTab3.setTextColor(getResources().getColor(R.color.colorTabText));
        mTxtTab4.setTextColor(getResources().getColor(R.color.colorTabText));
        mTxtTab5.setTextColor(getResources().getColor(R.color.colorTabText));

        mTxtTab1.setEnabled(true);
        mTxtTab2.setEnabled(true);
        mTxtTab3.setEnabled(true);
        mTxtTab4.setEnabled(true);
        mTxtTab5.setEnabled(true);

        if (mLayoutPanel.getChildCount() > 0){
            View v = mLayoutPanel.getChildAt(0);
            v.setVisibility(View.VISIBLE);
            v.setAlpha(1.0f);
            v.setTranslationY(0.0f);
            v.animate().alpha(0.0f).translationY(300.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLayoutPanel.removeAllViews();
                    if (callback != null) {
                        callback.show();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }else{
            if (callback != null) {
                callback.show();
            }
        }

    }

    private void showPanelAnimation(View v){
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0.0f);
        v.setTranslationY(300.0f);
        v.animate().alpha(1.0f).translationY(0.0f).setDuration(300).setListener(null);
    }

    PanelBase.CloseCallBack mCloseCallBack = new PanelBase.CloseCallBack() {
        @Override
        public void onClose() {
            removeFocusSticker();
            mIsShowMenu = false;
            closeTab(null);

        }
    };

    private int checkOpenedPanel(){
        if (mLayoutPanel.getChildCount() > 0){
            if (mLayoutPanel.getChildAt(0) instanceof PanelBackground) {
                return 0;
            }else if (mLayoutPanel.getChildAt(0) instanceof PanelFonts) {
                return 1;
            }else if (mLayoutPanel.getChildAt(0) instanceof PanelTextEffect) {
                return 2;
            }else if (mLayoutPanel.getChildAt(0) instanceof PanelNeonColor) {
                return 3;
            }else if (mLayoutPanel.getChildAt(0) instanceof PanelStickers) {
                return 4;
            }else if (mLayoutPanel.getChildAt(0) instanceof PanelSelectSticker) {
                return 4;
            }else{
                return -1;
            }

        }else{
            return -1;
        }
    }

    private void setTab(int tabPos){
        if (checkOpenedPanel() == tabPos){
            mCloseCallBack.onClose();
            return;
        }

        if (tabPos == 0){
            removeFocusSticker();
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab1.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab1.setTextColor(getResources().getColor(R.color.colorWhite));
                    mIsShowMenu = true;

                    PanelBackground panel = new PanelBackground(EditActivity.this);
                    panel.setOnChangeBright(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataStyle.setBright(value);
                            applyStyleData();
                        }
                    });
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnChangeColor(new PanelBase.ChangeIntValueCallBack() {
                        @Override
                        public void onChangeValue(int value) {
                            mDataStyle.setColor(value);
                            applyStyleData();
                        }
                    });
                    panel.setBright(mDataStyle.getBright());
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                    showPanelAnimation(panel);
                }
            });




        }else if (tabPos == 1){
            removeFocusSticker();
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab2.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab2.setTextColor(getResources().getColor(R.color.colorWhite));

                    mIsShowMenu = true;


                    PanelFonts panel = new PanelFonts(EditActivity.this);
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnSelectListener(new PanelFonts.FontCallBack() {
                        @Override
                        public void onSelect(Typeface typeface) {
                            mDataText.setFont(typeface);
                            applyTextData();
                        }
                    });
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    showPanelAnimation(panel);
                }
            });


        }else if (tabPos == 2){
            removeFocusSticker();
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab3.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab3.setTextColor(getResources().getColor(R.color.colorWhite));
                    mIsShowMenu = true;

                    PanelTextEffect panel = new PanelTextEffect(EditActivity.this);
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnChangeAlign(new PanelBase.ChangeIntValueCallBack() {
                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setAlign(value);
                            applyTextData();
                        }
                    });
                    panel.setOnChangeSize(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setSize(value);
                            applyTextData();
//                    mNeonView.setTextHeight(value * 5);
//                    mNeonView.setPaintLayout();

                        }
                    });
                    panel.setOnChangeRotate(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setDegree(value);
                            applyTextData();


                        }
                    });
                    panel.setOnChangeLineSpace(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setLinespacing(((float)value) / 100.0f);
                            applyTextData();
                        }
                    });
                    panel.setOnChangeLetterSpace(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setLetterspacing(((float)value) / 100.0f - 1.0f);
                            applyTextData();
                        }
                    });
                    panel.setSize(mDataText.getSize());
                    panel.setRotate(mDataText.getDegree());
                    panel.setLineSpace((int)(mDataText.getLinespacing() *  100));
                    panel.setLetterSpace((int)(mDataText.getLetterspacing() *  100 + 100));
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    showPanelAnimation(panel);
                }
            });


        }else if (tabPos == 3){
            removeFocusSticker();
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab4.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab4.setTextColor(getResources().getColor(R.color.colorWhite));
                    mIsShowMenu = true;

                    PanelNeonColor panel = new PanelNeonColor(EditActivity.this);
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnChangeBlur(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setBlur(value);
                            applyTextData();
                        }
                    });
                    panel.setOnChangeShadow(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setShadow(value);
                            applyTextData();
                        }
                    });
                    panel.setOnChangeInner(new PanelBase.ChangeIntValueCallBack() {
                        @Override
                        public void onChangeValue(int value) {
                            mDataText.setInner(value);
                            applyTextData();
                        }
                    });
                    panel.setShadow(mDataText.getShadow());
                    panel.setColor(mDataText.getBlurColor());
                    panel.setBlur(mDataText.getBlur());
                    panel.setInnter(mDataText.getInner());
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    showPanelAnimation(panel);
                }
            });


        }else if (tabPos == 4){
            removeFocusSticker();
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab5.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab5.setTextColor(getResources().getColor(R.color.colorWhite));
                    mIsShowMenu = true;

                    PanelStickers panel = new PanelStickers(EditActivity.this);
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnSelectListener(new PanelStickers.StickerCallBack() {
                        @Override
                        public void onSelect(Bitmap bitmap) {
                            addSticker(bitmap);
                        }
                    });
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    showPanelAnimation(panel);
                }
            });


        }else if (tabPos == 5){
            closeTab(new runCallback() {
                @Override
                public void show() {
                    mTxtTab5.setBackgroundResource(R.drawable.tab_back_on);
                    mTxtTab5.setTextColor(getResources().getColor(R.color.colorWhite));
                    mIsShowMenu = true;

                    PanelSelectSticker panel = new PanelSelectSticker(EditActivity.this);
                    panel.setOnCloseListener(mCloseCallBack);
                    panel.setOnChangeBlur(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mSelectStickerData.setBlur(value);
                            applyStickerData();
                        }
                    });

                    panel.setOnChangeSize(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            float size = (float) value / 100.0f;
                            mSelectStickerData.setScale(size);
                            float width = mStickerSize * size;
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mSelectStickerView.getLayoutParams();
                            int centerX = params.leftMargin + params.width / 2;
                            int centerY = params.topMargin + params.height / 2;
                            params.width = (int) (width + 2 * mStickerPadding);
                            params.height = (int) (width * mSelectStickerData.getOriginalHeight() / mSelectStickerData.getOrigianlWidth() + 2 * mStickerPadding);
                            params.leftMargin = centerX - params.width / 2;
                            params.topMargin = centerY - params.height / 2;
                            mSelectStickerView.setLayoutParams(params);
                            applyStickerData();
                            mSelectStickerView.requestLayout();
                            mSelectStickerView.setOnTouchListener(new ViewTouchListener(mSelectStickerView, STICKER_TOUCH_MODE));
                        }
                    });
                    panel.setOnChangeRotate(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mSelectStickerData.setDegree(value);
                            applyStickerData();


                        }
                    });

                    panel.mTxtDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCloseCallBack.onClose();
                            mFrameStickers.removeView(mSelectStickerView);
                        }
                    });

                    panel.setOnChangeShadow(new PanelBase.ChangeIntValueCallBack(){

                        @Override
                        public void onChangeValue(int value) {
                            mSelectStickerData.setShadow(value);
                            applyStickerData();
                        }
                    });

                    panel.setOnChangeInner(new PanelBase.ChangeIntValueCallBack() {
                        @Override
                        public void onChangeValue(int value) {
                            mSelectStickerData.setInner(value);
                            applyStickerData();
                        }
                    });

                    panel.setShadow(mDataText.getShadow());
                    panel.setColor(mSelectStickerData.getColor());
                    panel.setBlur((mSelectStickerData.getBlur()));
                    panel.setRotate(mSelectStickerData.getDegree());
                    panel.setSize((int)(mSelectStickerData.getScale() * 100.0f));
                    panel.setInnter(mSelectStickerData.getInner());
                    mLayoutPanel.addView(panel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    showPanelAnimation(panel);
                }
            });

        }
    }
    NeonView mNeonView;
    int mStickerSize = 0;
    int mStickerPadding = 0;
    private void addSticker(Bitmap bm){
        OutGlowImageView imgView = new OutGlowImageView(this);
        mStickerPadding = getResources().getDimensionPixelSize(R.dimen.sticker_padding);
        imgView.setPadding(mStickerPadding, mStickerPadding , mStickerPadding, mStickerPadding);
        imgView.setImageBitmap(bm);
        StickerData data = new StickerData();
        data.setBlur(10);
        data.setColor(getResources().getColor(R.color.colorPink));
        imgView.setTag(data);
        imgView.setOnTouchListener(new ViewTouchListener(imgView, STICKER_TOUCH_MODE));


        mStickerSize = getResources().getDimensionPixelSize(R.dimen.sticker_size);
        data.setOriginalSize(bm.getWidth(), bm.getHeight());
        int wid = mStickerSize + mStickerPadding * 2;
        int hei = mStickerSize * bm.getHeight() / bm.getWidth() + mStickerPadding * 2;

        data.firstViewWidth = wid;
        data.firstViewHeight = hei;

        mFrameStickers.addView(imgView, new FrameLayout.LayoutParams(wid, hei));
        data.applySticker(imgView);
        imgView.setVisibility(View.VISIBLE);
        imgView.setAlpha(0.0f);
        imgView.animate().alpha(1.0f).setDuration(500).setListener(null);

        /*mNeonView = new NeonView(this);
        mNeonView.setNeonText("Hello world", Typeface.DEFAULT);
        mNeonView.setMainColor(0xffFF2366);
        mNeonView.setOutlineWidth(0.0f);
        mNeonView.setGlowSpread(30.0f);
        mNeonView.setDrawMode(2);
        mNeonView.setBrightness(1.0f);
        mNeonView.setTextHeight(50);
        mNeonView.createBitmap();
        mFrameStickers.addView(mNeonView, new FrameLayout.LayoutParams(800, 150));
        mNeonView.setPaintLayout();
        mNeonView.setOnTouchListener(new ViewTouchListener(mNeonView, STICKER_TOUCH_MODE));*/
    }

    private void removeFocusSticker(){
        for (int i= 0; i< mFrameStickers.getChildCount(); i++){
            mFrameStickers.getChildAt(i).setBackgroundColor(0x00000000);
            mFrameStickers.getChildAt(i).invalidate();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GlobalConst.REQUEST_COLOR) {
                int colorBack = data.getIntExtra("color", 0xFFFFFFFF);
                mDataStyle.setColor(colorBack);
                applyStyleData();
            }else if (requestCode == GlobalConst.REQUEST_NEON_COLOR) {
                int colorBack = data.getIntExtra("color", 0xFFFFFFFF);
                mDataText.setBlurColor(colorBack);
                applyTextData();
                if (mLayoutPanel.getChildCount() > 0 && mLayoutPanel.getChildAt(0) instanceof PanelNeonColor){
                    PanelNeonColor panel = (PanelNeonColor)mLayoutPanel.getChildAt(0);
                    panel.setColor(colorBack);
                }
            }else if (requestCode == GlobalConst.REQUEST_STICKER_NEON_COLOR) {
                int colorBack = data.getIntExtra("color", 0xFFFFFFFF);
                mSelectStickerData.setColor(colorBack);
                applyStickerData();
                if (mLayoutPanel.getChildCount() > 0 && mLayoutPanel.getChildAt(0) instanceof PanelSelectSticker){
                    PanelSelectSticker panel = (PanelSelectSticker)mLayoutPanel.getChildAt(0);
                    panel.setColor(colorBack);
                }
            }else if (requestCode == GlobalConst.REQUEST_BUY_STICKER) {
                int pos = data.getIntExtra("pos", 0);
                int tryFree = data.getIntExtra("try", 0);
                if (mLayoutPanel.getChildCount() > 0 && mLayoutPanel.getChildAt(0) instanceof PanelStickers){
                    PanelStickers panel = (PanelStickers)mLayoutPanel.getChildAt(0);
                    if (tryFree == 1){
                        panel.tryFreeSticker(pos);
                    }else {
                        panel.buySticker(pos);
                    }
                }
            }else if (requestCode == GlobalConst.REQUEST_BUY_FONT) {
                int cateNo = data.getIntExtra("cateNo", 0);
                int fontNo = data.getIntExtra("fontNo", 0);
                if (mLayoutPanel.getChildCount() > 0 && mLayoutPanel.getChildAt(0) instanceof PanelFonts){
                    PanelFonts panel = (PanelFonts)mLayoutPanel.getChildAt(0);
                    panel.buyFont(cateNo, fontNo, 1);
                }
            }
        }
    }

}
