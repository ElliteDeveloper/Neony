package com.ptnst.neon.neon.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.internal.view.SupportMenu;

import com.ptnst.neon.neon.EditActivity;


public class ViewProvider {
    private static ViewProvider INSTANCE = null;
    private static final int MASK_COLOR = 0xFFFFFF;
    private Paint mAlphaPaint = new Paint();
    private Paint mBitmapPaint;
    private Rect mBitmapSrcRect = new Rect();
    private Rect mCanvasRect = new Rect();
    private Paint mDebugPaint;
    private Rect mDestRect = new Rect();
    private Matrix mMatrix = new Matrix();
    private Paint mShaderPaint;
    private Paint mStrokePaint;

    public static ViewProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewProvider();
        }
        return INSTANCE;
    }

    private ViewProvider() {
        this.mAlphaPaint.setStyle(Style.FILL);
        this.mAlphaPaint.setAntiAlias(true);
        this.mStrokePaint = new Paint();
        this.mStrokePaint.setStyle(Style.STROKE);
        this.mStrokePaint.setAntiAlias(true);
        this.mShaderPaint = new Paint();
        this.mShaderPaint.setAntiAlias(true);
        this.mDebugPaint = new Paint();
        this.mDebugPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas, Bitmap bitmap, int bright, int color, int alpha, Bitmap textBitmap, Rect textRect, int textDegree,  Rect stickerTextRect, int isMoveLine) {
        if (canvas ==  null){
            return;
        }
        if (color == 0xFF000000){
            canvas.drawColor(0xFFA0A0A0);
        }else {
            canvas.drawColor(color);
        }
        int minSize = 0;
        mCanvasRect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        if (canvas.getWidth() >= canvas.getHeight()){
            minSize = canvas.getHeight();
        }else{
            minSize = canvas.getWidth();
        }
        if (bitmap != null && !bitmap.isRecycled()){
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            float scale = (float)bright / 100.0f - 0.5f;
            float translate = scale  * 255.f;
            cm.set(new float[] {
                    1, 0, 0, 0, translate,
                    0, 1, 0, 0, translate,
                    0, 0, 1, 0, translate,
                    0, 0, 0, 1, 0 });
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            mBitmapSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, mBitmapSrcRect, mCanvasRect, paint);
        }
        if (textRect != null && textRect.isEmpty()) {
            int colorNoAlpha = (color & 0xFFFFFF);
            int colorWIthAlpha = (alpha << 24) | colorNoAlpha;
            mMatrix.reset();
            mAlphaPaint.setColor(colorWIthAlpha);
            mStrokePaint.setColor(colorWIthAlpha);
            if (bitmap == null) {
                mAlphaPaint.setColor(color);
                canvas.drawRect(mCanvasRect, mAlphaPaint);
            }
            if (textBitmap != null && !textBitmap.isRecycled()) {
                mBitmapSrcRect.set(0, 0, textBitmap.getWidth(), textBitmap.getHeight());
                if (textDegree != 0) {
                    canvas.rotate(textDegree, textRect.centerX(), textRect.centerY());
                    canvas.drawBitmap(textBitmap, mBitmapSrcRect, textRect, mBitmapPaint);
                    canvas.rotate(-textDegree, textRect.centerX(), textRect.centerY());
                } else {
                    canvas.drawBitmap(textBitmap, mBitmapSrcRect, textRect, null);

                }
            }
        }
        if (isMoveLine < 0){
            return;
        }
        mDebugPaint.setStyle(Style.FILL);
        mDebugPaint.setColor(0x40000000);
        canvas.drawRect(mCanvasRect.left, mCanvasRect.top, mCanvasRect.right - 1, mCanvasRect.bottom, mDebugPaint);
        mDebugPaint.setStyle(Style.STROKE);
        mDebugPaint.setColor(0xFFFFFFFF);
        mDebugPaint.setStrokeWidth(8.0f);
        canvas.drawRect(mCanvasRect.left, mCanvasRect.top, mCanvasRect.right, mCanvasRect.bottom, mDebugPaint);
        mDebugPaint.setStyle(Style.STROKE);
        mDebugPaint.setColor(0x40ffffff);
        mDebugPaint.setStrokeWidth(2.0f);
        int step  = minSize / 16;
        int posY = step;
        while (mCanvasRect.centerY() + posY > 0 && mCanvasRect.centerY() + posY < mCanvasRect.height()) {
            canvas.drawLine(0, mCanvasRect.centerY() - posY, mCanvasRect.width(), mCanvasRect.centerY() - posY, mDebugPaint);
            canvas.drawLine(0, mCanvasRect.centerY() + posY, mCanvasRect.width(), mCanvasRect.centerY() + posY, mDebugPaint);
            posY += step;
        }
        int posX = step;
        while (mCanvasRect.centerX() + posX > 0 && mCanvasRect.centerX() + posX < mCanvasRect.width()) {
            canvas.drawLine(mCanvasRect.centerX() - posX, 0,  mCanvasRect.centerX() - posX, mCanvasRect.height(), mDebugPaint);
            canvas.drawLine(mCanvasRect.centerX() + posX, 0,  mCanvasRect.centerX() + posX, mCanvasRect.height(), mDebugPaint);
            posX += step;
        }
        mDebugPaint.setStrokeWidth(4.0f);
        canvas.drawLine(mCanvasRect.centerX(), 0, mCanvasRect.centerX(), mCanvasRect.height(), mDebugPaint);
        canvas.drawLine(0, mCanvasRect.centerY(), mCanvasRect.width(), mCanvasRect.centerY(), mDebugPaint);
        mDebugPaint.setColor(0x80FF2366);
        if (isMoveLine == EditActivity.TEXT_TOUCH_MODE){
            drawCenterLine(canvas, textRect, mCanvasRect, mDebugPaint);
        }else if (isMoveLine == EditActivity.STICKER_TOUCH_MODE){
            this.drawCenterLine(canvas, stickerTextRect, mCanvasRect, mDebugPaint);
        }
        if (textRect == null){
            return;
        }
        if (isMoveLine != EditActivity.TEXT_TOUCH_MODE){
            return;
        }
        if (textDegree == 0){
            mDebugPaint.setStyle(Style.FILL);
            mDebugPaint.setColor(0x60000000);
            canvas.drawRect(textRect, mDebugPaint);
            mDebugPaint.setStyle(Style.STROKE);
            mDebugPaint.setStrokeWidth(2.0f);
            mDebugPaint.setColor(0x80000000);
            canvas.drawRect(textRect, mDebugPaint);
        }else {
            RectF rectF = getRotateTextRect(textRect, textDegree);
            mDebugPaint.setStyle(Style.FILL);
            mDebugPaint.setColor(0x60000000);
            canvas.drawRect(rectF, mDebugPaint);
            mDebugPaint.setStyle(Style.STROKE);
            mDebugPaint.setStrokeWidth(2.0f);
            mDebugPaint.setColor(0x80000000);
            canvas.drawRect(rectF, mDebugPaint);
        }


    }

    private void setTextDestRect(Canvas canvas, Rect rect, int i) {



        int width = canvas.getWidth() < canvas.getHeight() ? canvas.getWidth() : canvas.getHeight();
        int width1 = canvas.getWidth() > canvas.getHeight() ? canvas.getWidth() : canvas.getHeight();
        width = EditConfig.GetTextPaddingSize(width);
        width1 = width1 / 3;

        this.mDestRect.set(rect);
        if (this.mDestRect.width() > this.mDestRect.height()) {
            this.mDestRect.inset(0, (this.mDestRect.height() - this.mDestRect.width()) / 2);
        } else {
            this.mDestRect.inset((this.mDestRect.width() - this.mDestRect.height()) / 2, 0);
        }
        int i2 = (-width) * i;
        this.mDestRect.inset(i2, i2);
        if (this.mDestRect.width() < width1) {
            width1 = -((width1 - this.mDestRect.width()) / 2);
            this.mDestRect.inset(width1, width1);
        } else if (this.mDestRect.width() > this.mCanvasRect.width() - width) {
            this.mDestRect.set(width, width, this.mCanvasRect.width() - width, this.mCanvasRect.height() - width);
        }
    }

    private RectF getRotateTextRect(Rect rect, int degrees) {
        RectF rectF = new RectF();
        RectF rectF2 = new RectF(rect);
        Matrix matrix = new Matrix();
        matrix.setRotate((float) degrees, (float) rect.centerX(), (float) rect.centerY());
        matrix.mapRect(rectF, rectF2);
        return rectF;
    }

    private void drawCenterLine(Canvas canvas, Rect rect, Rect rect2, Paint paint) {
        if (rect != null) {
            if (rect2 != null) {
                if (Math.abs(rect.centerX() - rect2.centerX()) < 10) {
                    canvas.drawLine((float) this.mCanvasRect.centerX(), 0.0f, (float) this.mCanvasRect.centerX(), (float) this.mCanvasRect.height(), paint);
                }
                if (Math.abs(rect.centerY() - rect2.centerY()) < 10) {
                    canvas.drawLine(0.0f, (float) this.mCanvasRect.centerY(), (float) this.mCanvasRect.width(), (float) this.mCanvasRect.centerY(), paint);
                }
            }
        }
    }

}
