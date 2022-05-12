package com.ptnst.neon.neon.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ContainImageView extends View {
    private Bitmap mImage;
    private int mIsMoveLine;
    private Rect mStickerTextRect;
    private int mStyleAlpha;
    private int mStyleBright;
    private int mStyleColor;
    private int mTextBackGroundCanvasSize;
    private Bitmap mTextBitmap;
    private int mTextDegree;
    private Rect mTextRect;

    public ContainImageView(Context context) {
        super(context);
        init();
    }

    public ContainImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ContainImageView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        this.mStyleColor = 0xFF000000;
        this.mStyleAlpha = 0xFF;
        this.mImage = null;
        this.mTextBitmap = null;
        this.mTextRect = new Rect();
        this.mIsMoveLine = -1;
        this.mTextBackGroundCanvasSize = -1;
        this.mTextDegree = 0;
        this.mStickerTextRect = new Rect();
    }

    public void setImageBitmap(@Nullable Bitmap bitmap) {
        this.mImage = bitmap;
    }


    public void setStyleColor(int styleColor) {
        this.mStyleColor = styleColor;
    }

    public void setText(@Nullable Bitmap bitmap, int canvasSize) {
        this.mTextBitmap = bitmap;
        this.mTextBackGroundCanvasSize = canvasSize;
        this.mTextRect.setEmpty();
    }

    public void setTextArea(int left, int top, int right, int bottom) {
        this.mTextRect.set(left, top, right, bottom);
    }

   

    public void setStickerTextArea(int left, int top, int right, int bottom) {
        this.mStickerTextRect.set(left, top, right, bottom);
    }

    public void setTextDegree(int textDegree) {
        this.mTextDegree = textDegree;
    }

    public int getTextDegree() {
        return this.mTextDegree;
    }

    public void setMoveLine(int isMoveLine) {
        if (this.mIsMoveLine != isMoveLine) {
            this.mIsMoveLine = isMoveLine;
        }
    }

    public void setStyleAlpha(int styleAlpha) {
        this.mStyleAlpha = styleAlpha;
    }

    public void setStyleBright(int styleBright) {
        this.mStyleBright = styleBright;
    }

    public int getStyleColor() {
        return this.mStyleColor;
    }

    public int getStyleAlpha() {
        return this.mStyleAlpha;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewProvider instance;
        if (mTextRect ==  null || mTextRect.isEmpty()) {
            mTextRect = computeTextRect(canvas, mTextBitmap, mTextBackGroundCanvasSize);
        }
        instance = ViewProvider.getInstance();
        instance.draw(canvas, getImage(), mStyleBright, mStyleColor, mStyleAlpha, mTextBitmap, mTextRect, mTextDegree,  mStickerTextRect, mIsMoveLine);
    }

    private static Rect computeTextRect(Canvas canvas, Bitmap bitmap, int size) {
        Rect rect = new Rect();
        if (bitmap == null) {
            rect.set(0, 0, canvas.getWidth() / 3, canvas.getHeight() / 3);
        } else {
            rect.set(0, 0, (int) (((float) canvas.getWidth()) * (((float) bitmap.getWidth()) / size)), (int) (((float) canvas.getHeight()) * (((float) bitmap.getHeight()) / size)));
        }
        Point point = EditConfig.GetTextPosition(rect.width(), rect.height(), canvas.getWidth(), canvas.getHeight());
        rect.offset(point.x, point.y);
        return rect;
    }

}
