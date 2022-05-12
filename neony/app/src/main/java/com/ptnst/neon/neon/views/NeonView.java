package com.ptnst.neon.neon.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import com.ptnst.neon.neon.R;

public class NeonView extends View {
    int errorResId = R.string.app_name;

    private static final boolean DBG = false;
    private static final int ERR_MSG_TEXTSIZE = 20;
    static final int FILL = 2;
    static final int NONE = 0;
    static final int OUTLINE = 1;
    private static final int PAINT_FLAGS = 65;
    private static final String TAG = "NeonView";
    Bitmap brightBmp;
    TextPaint brightPaint;
    float brightness;
    Matrix dMatrix;
    Rect dRect;
    RectF dRectf;
    float defTextCharSize;
    Path defTextPath;
    RectF defTextRectf;
    float defTextSpacing;
    int drawMode;
    BlurMaskFilter glowFilter;
    TextPaint glowPaint;
    float glowSpread;
    boolean hardwareAccelerated;
    View hostView;
    String imageTooLargeMsg;
    String imageUnaccelerableMsg;
    LayoutParams layoutParams;
    TextPaint mainPaint;
    int maxBmpHeight;
    int maxBmpWidth;
    Bitmap neonBmp;
    TextPaint neonPaint;
    float outWidth;
    Bitmap overlayBmp;
    TextPaint overlayPaint;
    Rect overlayRect;
    Rect paintRect;
    int screenHeight;
    int screenWidth;
    float textCharSize;
    float textHeight;
    Path textPath;
    RectF textRectf;
    float textSpacing;
    float textWidth;
    boolean verticalAligned;

    public NeonView(Context context) {
        super(context);
        initView();
    }

    public NeonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NeonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenDensity = getResources().getDisplayMetrics().density;
        this.imageTooLargeMsg = getContext().getString(errorResId);
        this.imageUnaccelerableMsg = getContext().getString(errorResId);
        this.glowPaint = new TextPaint();
        this.glowPaint.setFlags(PAINT_FLAGS);
        this.glowPaint.setStrokeJoin(Join.ROUND);
        this.mainPaint = new TextPaint();
        this.mainPaint.setFlags(PAINT_FLAGS);
        this.mainPaint.setStrokeJoin(Join.ROUND);
        this.brightPaint = new TextPaint();
        this.brightPaint.setFlags(PAINT_FLAGS);
        this.brightPaint.setStrokeJoin(Join.ROUND);
        this.overlayPaint = new TextPaint();
        this.neonPaint = new TextPaint();
        this.neonPaint.setColor(-1);
        this.neonPaint.setTextSize(20.0f * screenDensity);
        this.neonPaint.setTextAlign(Align.CENTER);
        this.paintRect = new Rect();
        this.overlayRect = new Rect();
        this.defTextPath = new Path();
        this.textPath = new Path();
        this.defTextRectf = new RectF();
        this.textRectf = new RectF();
        this.dMatrix = new Matrix();
        this.dRect = new Rect();
        this.dRectf = new RectF();
    }

    public void setNeonText(String text, Typeface ttf) {
        if (text == null || ttf == null || text.trim().isEmpty()) {
            this.defTextPath.reset();
            this.defTextRectf.setEmpty();
            invalidate();
            return;
        }
        this.glowPaint.setTypeface(ttf);
        this.mainPaint.setTypeface(ttf);
        this.brightPaint.setTypeface(ttf);
        String[] textLines = text.split("\n");
        TextPaint tPaint = new TextPaint();
        tPaint.setFlags(PAINT_FLAGS);
        tPaint.setTypeface(ttf);
        tPaint.setTextAlign(Align.CENTER);


        tPaint.setTextSize((float) (Math.min(this.screenWidth, this.screenHeight) / textLines.length));
        this.defTextPath.reset();
        this.defTextRectf.setEmpty();
        this.defTextSpacing = tPaint.getFontSpacing();
        Path tPath = new Path();
        RectF tRectf = new RectF();
        float linesWidth = 0.0f;
        float linesHeight = 0.0f;
        float baseline = 0.0f;
        float lineCount = 0.0f;
        float charCount = 0.0f;
        for (String trim : textLines) {
            String line = trim.trim();
            tPaint.getTextPath(line, 0, line.length(), 0.0f, baseline, tPath);
            tPath.computeBounds(tRectf, true);
            if (line.isEmpty() || tRectf.isEmpty()) {
                tRectf.set(0.0f, tPaint.ascent() + baseline, 1.0f, tPaint.descent() + baseline);
            } else {
                linesWidth += tRectf.width();
                linesHeight += tRectf.height();
                if (tRectf.height() > 0.0f) {
                    lineCount += 1.0f;
                }
                if (tRectf.width() > 0.0f) {
                    charCount += (float) line.length();
                }
                this.defTextPath.addPath(tPath);
            }
            this.defTextRectf.union(tRectf);
            baseline += this.defTextSpacing;
        }
        this.defTextCharSize = Math.min(charCount > 0.0f ? linesWidth / charCount : 0.0f, lineCount > 0.0f ? linesHeight / lineCount : 0.0f);
        invalidate();
    }

    public void setTextHeight(float height) {
        float r;
        float defWidth = this.defTextRectf.width();
        float defHeight = this.defTextRectf.height();
        if (defHeight > 0.0f) {
            r = height / defHeight;
        } else {
            r = 0.0f;
        }
        this.textHeight = height;
        this.textWidth = defWidth * r;
        this.textCharSize = this.defTextCharSize * r;
        this.textSpacing = this.defTextSpacing * r;
        this.textRectf.set(0.0f, 0.0f, this.textWidth, this.textHeight);
        updatePaintRect();
        invalidate();
    }

    private void updatePaintRect() {
        this.outWidth = Math.max(this.mainPaint.getStrokeWidth(), this.glowSpread);
        this.paintRect.set(0, 0, (int) Math.ceil((double) (this.textWidth + this.outWidth)), (int) Math.ceil((double) (this.textHeight + this.outWidth)));
    }

    public void setPaintLayout() {
        this.layoutParams = new LayoutParams(Math.min(this.paintRect.width(), this.screenWidth), Math.min(this.paintRect.height(), this.screenHeight));
        setLayoutParams(this.layoutParams);
        requestLayout();
        invalidate();
    }

    Rect getPaintRect() {
        return new Rect(this.paintRect);
    }

    public void setDrawMode(int mode) {
        this.drawMode = mode;
        switch (mode) {
            case 1:
                this.mainPaint.setStyle(Style.STROKE);
                this.glowPaint.setStyle(Style.STROKE);
                this.brightPaint.setStyle(Style.STROKE);
                break;
            case 2:
                this.mainPaint.setStyle(Style.FILL_AND_STROKE);
                this.glowPaint.setStyle(Style.FILL_AND_STROKE);
                this.brightPaint.setStyle(Style.FILL_AND_STROKE);
                break;
        }
        invalidate();
    }

    public void setMainColor(int color) {
        this.mainPaint.setColor(color);
        this.glowPaint.setColor(color);
        setBrightness(this.brightness);
        invalidate();
    }

    public void setOutlineWidth(float widthRatio) {
        float width = Math.max((this.textCharSize * widthRatio) / 2.0f, 1.0f);
        this.mainPaint.setStrokeWidth(width);
        if (this.drawMode == 2) {
            this.brightPaint.setStrokeWidth(0.1f);
        } else if (this.drawMode == 1) {
            this.brightPaint.setStrokeWidth(Math.max(0.6f * width, 1.0f));
        }
        updatePaintRect();
    }

    public void setGlowSpread(float spreadRatio) {
        if (spreadRatio == 0.0f) {
            this.glowPaint.setMaskFilter(null);
            this.glowPaint.setStrokeWidth(1.0f);
            this.glowPaint.setAlpha(0);
            this.glowSpread = 0.0f;
        } else {
            float outlineWidth = this.mainPaint.getStrokeWidth();
            float maxWidth = this.textCharSize + outlineWidth;
            float spread = Math.max(((spreadRatio * maxWidth) / 2.0f) + outlineWidth, 1.0f);
            float blur = Math.max((spreadRatio * maxWidth) / 2.0f, 1.0f);
            this.glowFilter = new BlurMaskFilter(blur, Blur.NORMAL);
            this.glowPaint.setMaskFilter(this.glowFilter);
            this.glowPaint.setStrokeWidth(spread);
            this.glowSpread = (blur * 2.0f) + spread;
        }
        setBrightness(this.brightness);
        updatePaintRect();
    }

    public void setBrightness(float bright) {
        this.brightness = bright;
        if (bright <= 0.0f) {
            this.brightPaint.setMaskFilter(null);
            this.brightPaint.setColor(this.mainPaint.getColor());
            this.glowPaint.setAlpha(128);
        } else {
            float[] hsv = new float[3];
            Color.colorToHSV(this.mainPaint.getColor(), hsv);
            hsv[1] = hsv[1] * (1.0f - bright);
            this.brightPaint.setColor(Color.HSVToColor(hsv));
            if (this.glowPaint.getMaskFilter() != null) {
                this.glowPaint.setAlpha(((int) ((128.0f * bright) * 0.8f)) + 128);
            }
        }
        invalidate();
    }



    public int createBitmap() {
        int[] errorId = new int[1];
        this.neonBmp = drawBitmap(errorId, this.glowPaint, this.brightPaint);
        return errorId[0];
    }

    public void clearBitmap() {
        if (this.neonBmp != null) {
            this.neonBmp.recycle();
            this.neonBmp = null;
        }
    }

    public Bitmap generateBitmap(int[] errorId) {
        if (this.neonBmp == null) {
            return drawBitmap(errorId, this.glowPaint, this.brightPaint);
        }
        Bitmap bmp = this.neonBmp;
        this.neonBmp = null;
        return bmp;
    }

    private Bitmap createBrightBitmap(int[] errorId) {
        Paint bglowPaint = new Paint(this.glowPaint);
        Paint bbrightPaint = new Paint(this.brightPaint);
        float[] hsv = new float[3];
        Color.colorToHSV(bbrightPaint.getColor(), hsv);
        hsv[1] = 0.0f;
        bbrightPaint.setColor(Color.HSVToColor(hsv));
        if (bglowPaint.getMaskFilter() != null) {
            bglowPaint.setAlpha(230);
        }
        return drawBitmap(errorId, bglowPaint, bbrightPaint);
    }

    private Bitmap drawBitmap(int[] errorId, Paint bmpglowPaint, Paint bmpbrightPaint) {
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(this.paintRect.width(), this.paintRect.height(), Config.ARGB_8888);
            if (bmp == null) {
                errorId[0] = errorResId;
            } else {
                Canvas canvas = new Canvas(bmp);
                if (!this.defTextPath.isEmpty()) {
                    float left = (((float) canvas.getWidth()) - this.textWidth) / 2.0f;
                    float top = (((float) canvas.getHeight()) - this.textHeight) / 2.0f;
                    Path txtPath = new Path();
                    this.dRectf.set(this.textRectf);
                    this.dRectf.offset(left, top);
                    this.dMatrix.setRectToRect(this.defTextRectf, this.dRectf, ScaleToFit.FILL);
                    this.defTextPath.transform(this.dMatrix, txtPath);
                    txtPath.close();
                    canvas.drawPath(txtPath, bmpglowPaint);
                    canvas.drawPath(txtPath, this.mainPaint);
//                    canvas.drawPath(txtPath, bmpbrightPaint);
                }
            }
        } catch (IllegalArgumentException e) {
            errorId[0] = errorResId;
        } catch (OutOfMemoryError e2) {
            errorId[0] = errorResId;
        }
        if (errorId[0] != 0) {
            return null;
        }
        return bmp;
    }

    void setHostView(View v) {
        this.hostView = v;
    }

    void alignVertical(boolean enable) {
        if (this.hostView != null) {
            this.verticalAligned = enable;
            this.hostView.scrollTo(0, enable ? (getHeight() - this.hostView.getHeight()) / 2 : 0);
        }
    }

    boolean isVerticalAligned() {
        return this.verticalAligned;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        alignVertical(this.verticalAligned);
    }

    protected void onDraw(Canvas canvas) {
        if (this.neonBmp != null) {
            int bmpWidth = this.neonBmp.getWidth();
            int bmpHeight = this.neonBmp.getHeight();
            this.hardwareAccelerated = canvas.isHardwareAccelerated();
            this.maxBmpWidth = canvas.getMaximumBitmapWidth();
            this.maxBmpHeight = canvas.getMaximumBitmapHeight();
            if (bmpWidth > this.maxBmpWidth || bmpHeight > this.maxBmpHeight) {

                canvas.drawText(this.hardwareAccelerated ? this.imageUnaccelerableMsg : this.imageTooLargeMsg, (float) (this.screenWidth / 2), (float) (getHeight() / 2), this.neonPaint);
                return;
            }
            int left = (getWidth() - bmpWidth) / 2;
            int top = (getHeight() - bmpHeight) / 2;
            canvas.drawBitmap(this.neonBmp, (float) left, (float) top, null);
            if (this.overlayBmp != null) {
                this.dRect.set(this.overlayRect);
                this.dRect.offset(left, top);
                canvas.clipRect(this.dRect);
                canvas.drawBitmap(this.overlayBmp, null, this.dRect, this.overlayPaint);
            }
        } else if (!this.defTextPath.isEmpty()) {
            float left2 = (((float) getWidth()) - this.textWidth) / 2.0f;
            float top2 = (((float) getHeight()) - this.textHeight) / 2.0f;
            this.dRectf.set(this.textRectf);
            this.dRectf.offset(left2, top2);
            this.dMatrix.setRectToRect(this.defTextRectf, this.dRectf, ScaleToFit.FILL);
            this.defTextPath.transform(this.dMatrix, this.textPath);
            this.textPath.close();
            canvas.drawPath(this.textPath, this.glowPaint);
            canvas.drawPath(this.textPath, this.mainPaint);
            canvas.drawPath(this.textPath, this.brightPaint);
        }
    }

}
