package com.ptnst.neon.neon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.widget.ImageView;

public class OutGlowImageView extends ImageView {
    int MAX_STEP = 20;
    int mBlurColor = 0xFFff1111;
    Bitmap mSrcBitmap;
    Bitmap mBitmap;
    Bitmap mBitmapBlur;
    int mBlurInnerRaidus = 0;
    Bitmap mBitmapInner;
    int[] offsetXY;
    int mBlurRadius = 0;
    int mShadow = 50;

    public OutGlowImageView(Context context) {
        super(context);
    }

    public OutGlowImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OutGlowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBlurColor(int color){
        this.mBlurColor = color;
    }

    public void setImageSrcBitmap(Bitmap src){
        if (mSrcBitmap != null && !mSrcBitmap.isRecycled()){
            mSrcBitmap.recycle();
        }
        mSrcBitmap = src;

    }

    public void setShadow(int shadow){
        this.mShadow = shadow;
    }

    public void setImageInner(int size) {
        mBlurInnerRaidus = size;
    }

    public void setImageBlur(int size){

        mBlurRadius = size;
        /*

        if (mBitmapBlur != null && !mBitmapBlur.isRecycled()){
            mBitmapBlur.recycle();
        }
        if (size == 0){
            mBlurRadius = 0;
            mBitmapBlur = null;
        }else {

            int blurSize = getPaddingLeft() * size / MAX_STEP;
            mBlurRadius = blurSize;
            Paint ptBlur = new Paint(Paint.ANTI_ALIAS_FLAG);
//            ptBlur.setFlags(65);
//            ptBlur.setStrokeJoin(Paint.Join.ROUND);

            ptBlur.setMaskFilter(new BlurMaskFilter(blurSize, BlurMaskFilter.Blur.NORMAL));

            offsetXY = new int[2];
            mBitmapBlur = mBitmap.extractAlpha(ptBlur, offsetXY);


        }
        */
    }
    public static final int REPEAT_SHADOW = 8;

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
        int shadowTop = getPaddingTop() * mShadow / 100 / 2;
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
    }
    @Override
    protected void onDraw(Canvas canvas) {


        freeze();
        if ( mBlurRadius > 0) {

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
            drawShadow(canvas, mBitmap);
            super.onDraw(canvas);
        }

        if (mBlurInnerRaidus > 0) {
            drawInner(canvas, mBitmap);
        }

        unfreeze();
    }



    private boolean frozen = false;

    public void freeze(){

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
