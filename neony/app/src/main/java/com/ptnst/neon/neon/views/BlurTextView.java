package com.ptnst.neon.neon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

public class BlurTextView extends EditText {
    int MAX_STEP = 20;
    int mBlurColor = 0xFFFF2366;
    public Bitmap mBitmapBlur;
    int[] offsetXY;
    int mBlurRadius = 0;
    int mBlurInnerRaidus = 0;
    int mShadow = 50;
    Bitmap mBitmapInner;

    public BlurTextView(Context context) {
        super(context);
    }

    public BlurTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BlurTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBlurColor(int color){
        this.mBlurColor = color;
    }


    public void setImageBlur(int size) {
        mBlurRadius = size;
    }

    public void setImageInner(int size) {
        mBlurInnerRaidus = size;
    }

    public void setShadow(int shadow){
        this.mShadow = shadow;
    }
    Bitmap mBitmap;

    public static final int REPEAT_SHADOW = 5;
    private void drawGlow(Canvas canvas, Bitmap firstBitmap){



        int STEP = 10;
        Bitmap bitmap0 = firstBitmap;
        int blurSize = (int) (getPaddingLeft() * 1.5f * mBlurRadius / MAX_STEP);
        if (blurSize < STEP){
            if (mBitmapBlur != null){
                mBitmapBlur.recycle();
            }
            int[] offsetXY = new int[2];
            offsetXY[0] = 0;
            offsetXY[1] = 0;
            Paint ptBlur = new Paint();
            ptBlur.setMaskFilter(new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL));
            ptBlur.setColor(mBlurColor);
            mBitmapBlur = bitmap0.extractAlpha(ptBlur, offsetXY);
            if (mBitmapBlur != null) {


                Rect rectSrc = new Rect(0, 0, mBitmapBlur.getWidth(), mBitmapBlur.getHeight());
                Rect rectDes = new Rect(0, 0,
                        mBitmapBlur.getWidth(),
                        mBitmapBlur.getHeight());
                Paint paint = new Paint();
                paint.setColor(mBlurColor);
                canvas.translate(offsetXY[0], offsetXY[1]);
                for (int i = 0; i < REPEAT_SHADOW; i++) {
                    canvas.drawBitmap(mBitmapBlur, rectSrc, rectDes, paint);
                }
                canvas.translate(-offsetXY[0], -offsetXY[1]);
            }
        }else {

            int[] dx = new int[]{0, 0};
            while (blurSize > 0) {

                int temp = STEP;
                if (blurSize < STEP){
                    temp = blurSize;
                    blurSize = 0;
                }else {
                    blurSize -= STEP;
                }
                int[] offsetXY = new int[2];
                Paint ptBlur = new Paint();
                ptBlur.setMaskFilter(new BlurMaskFilter(temp, BlurMaskFilter.Blur.NORMAL));
                ptBlur.setColor(mBlurColor);
                mBitmapBlur = bitmap0.extractAlpha(ptBlur, offsetXY);
                offsetXY[0] += dx[0];
                offsetXY[1] += dx[1];
                if (bitmap0 != firstBitmap) {
                    bitmap0.recycle();
                }
                if (mBitmapBlur != null) {
                    Rect rectSrc = new Rect(0, 0, mBitmapBlur.getWidth(), mBitmapBlur.getHeight());
                    Rect rectDes = new Rect(0, 0,
                            mBitmapBlur.getWidth(),
                            mBitmapBlur.getHeight());
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(mBlurColor);
//                    paint.setAlpha(alpha);
                    canvas.translate(offsetXY[0], offsetXY[1]);
                    if (blurSize == 0) {
                        Bitmap bitmap = Bitmap.createBitmap(mBitmapBlur.getWidth(), mBitmapBlur.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas1 = new Canvas(bitmap);

                        for (int i = 0; i < REPEAT_SHADOW; i++) {
                            canvas1.drawBitmap(mBitmapBlur, rectSrc, rectDes, paint);
                        }
                        Bitmap bitmap1 = rs(getContext(), bitmap, 15);
                        canvas.drawBitmap(bitmap1, rectSrc, rectDes, paint);
                        bitmap1.recycle();
                        mBitmapBlur.recycle();
                    }
                    canvas.translate(-offsetXY[0], -offsetXY[1]);
                    bitmap0 = mBitmapBlur;
                }


                dx[0] = offsetXY[0];
                dx[1] = offsetXY[1];
            }
        }

    }

    private void drawShadow(Canvas canvas, Bitmap bitmap){
        int shadowTop = getPaddingTop() * mShadow / 100 / 4;
        if (shadowTop > 0) {
            Bitmap bitmap1 = rs(getContext(), bitmap, 3);
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(0x80111111, PorterDuff.Mode.MULTIPLY));
            canvas.drawBitmap(bitmap1, 0, shadowTop, paint);
        }
    }

    private void drawInner(Canvas canvas, Bitmap bitmap){

        if (mBitmapInner != null && !mBitmapInner.isRecycled()){
            mBitmapInner.recycle();
            mBitmapInner = null;
        }

        Paint ptBlur = new Paint();
//        ptBlur.setMaskFilter(new BlurMaskFilter(getTextSize() * mBlurInnerRaidus  / 15 / MAX_STEP, BlurMaskFilter.Blur.INNER));
        ptBlur.setMaskFilter(new BlurMaskFilter((float)mBlurInnerRaidus  , BlurMaskFilter.Blur.INNER));
        int[] offsetXY = new int[2];
        mBitmapInner = bitmap.extractAlpha(ptBlur, offsetXY);



        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(mBlurColor, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xffffffff);
        canvas.drawBitmap(mBitmapInner, 0, 0, paint);
//        canvas.drawBitmap(mBitmapInner, 0, 0, paint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        freeze();

        if (!hasFocus() && mBlurRadius > 0) {

            if (mBitmapBlur != null){
                mBitmapBlur.recycle();
            }

            mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(mBitmap);
            super.onDraw(canvas1);

            drawGlow(canvas, mBitmap);
            drawShadow(canvas, mBitmap);

            super.onDraw(canvas);



        }else{
            if (!hasFocus()) {
                mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(mBitmap);
                super.onDraw(canvas1);
                drawShadow(canvas, mBitmap);

            }
            super.onDraw(canvas);

        }

        if (!hasFocus()){
            if (mBlurInnerRaidus > 0) {
                drawInner(canvas, mBitmap);
            }
        }

        unfreeze();


    }



    private int[] lockedCompoundPadding;
    private boolean frozen = false;

    public void freeze(){
        lockedCompoundPadding = new int[]{
                getCompoundPaddingLeft(),
                getCompoundPaddingRight(),
                getCompoundPaddingTop(),
                getCompoundPaddingBottom()
        };
        frozen = true;
    }

    public void unfreeze(){
        frozen = false;
    }

    @Override
    public void postInvalidate(){
        if(!frozen) super.postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom){
        if(!frozen) super.postInvalidate(left, top, right, bottom);
    }

    @Override
    public void invalidate(){
        if(!frozen)	super.invalidate();
    }

    @Override
    public void invalidate(Rect rect){
        if(!frozen) super.invalidate(rect);
    }

    @Override
    public void invalidate(int l, int t, int r, int b){
        if(!frozen) super.invalidate(l,t,r,b);
    }

    @Override
    public int getCompoundPaddingLeft(){
        return !frozen ? super.getCompoundPaddingLeft() : lockedCompoundPadding[0];
    }

    @Override
    public int getCompoundPaddingRight(){
        return !frozen ? super.getCompoundPaddingRight() : lockedCompoundPadding[1];
    }

    @Override
    public int getCompoundPaddingTop(){
        return !frozen ? super.getCompoundPaddingTop() : lockedCompoundPadding[2];
    }

    @Override
    public int getCompoundPaddingBottom(){
        return !frozen ? super.getCompoundPaddingBottom() : lockedCompoundPadding[3];
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static Bitmap rs(Context context, Bitmap bitmap, int radius) throws RSRuntimeException {
        RenderScript rs = null;
        Allocation input = null;
        Allocation output = null;
        ScriptIntrinsicBlur blur = null;
        try {
            rs = RenderScript.create(context);
            rs.setMessageHandler(new RenderScript.RSMessageHandler());
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            blur.setInput(input);
            blur.setRadius(radius);
            blur.forEach(output);
            output.copyTo(bitmap);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
            if (input != null) {
                input.destroy();
            }
            if (output != null) {
                output.destroy();
            }
            if (blur != null) {
                blur.destroy();
            }
        }

        return bitmap;
    }
}
