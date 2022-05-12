package com.ptnst.neon.neon.data;

import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.widget.TextView;

public class TextAttr {
    public static final int ALIGN_FONT_CENTER = 1;
    public static final int ALIGN_FONT_LEFT = 0;
    public static final int ALIGN_FONT_RIGHT = 2;
    protected int align = ALIGN_FONT_CENTER;
    protected int alpha = 0xFF;
    protected int color = 0xFFFFFFFF;
    protected int degree = 0;
    protected Typeface font = null;
    protected float letterspacing = 0.0f;
    protected float linespacing = 1.0f;
    protected int shadowcolor = 0xFF000000;
    protected float shadowradius = 0.0f;
    protected float shadowx = 0.0f;
    protected float shadowy = 0.0f;
    protected int size = 30;
    protected int x = 0;
    protected int y = 0;

    public void applyTextView(TextView textView) {
        applyTextView(textView, -1, true);
    }

    public void applyTextView(TextView textView, boolean z) {
        applyTextView(textView, -1, z);
    }

    public void applyTextView(TextView textView, int size, boolean z) {
        textView.setTypeface(font);
        if (size > 0) {
            textView.setTextSize(2, (float) size);
        } else {
            textView.setTextSize(2, (float) getSize());
        }
        if (z) {
            textView.setTranslationX((float) getX());
            textView.setTranslationY((float) getY());
        }
        textView.setTextColor((getAlpha() << 24) | (getColor() & 0xFFFFFF));
//        textView.setShadowLayer(getShadowradius(), getShadowx(), getShadowy(), getShadowcolor());
        textView.setLineSpacing(0, getLinespacing());
        textView.setRotation((float) getDegree());
        if (VERSION.SDK_INT >= 21) {
            textView.setLetterSpacing(getLetterspacing());
        }
        int align = getAlign();
        if (align == ALIGN_FONT_LEFT) {
            textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        } else if (align == ALIGN_FONT_CENTER) {
            textView.setGravity(Gravity.CENTER);
        } else if (align == ALIGN_FONT_RIGHT) {
            textView.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        }


    }



    public Typeface getFont() {
        return this.font;
    }

    public void setFont(Typeface font) {
        this.font = font;
    }

    public int getAlign() {
        return this.align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDegree() {
        return this.degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public float getLinespacing() {
        return this.linespacing;
    }

    public void setLinespacing(float linespacing) {
        this.linespacing = linespacing;
    }

    public float getLetterspacing() {
        return this.letterspacing;
    }

    public void setLetterspacing(float letterspacing) {
        this.letterspacing = letterspacing;
    }

    public int getShadowcolor() {
        return this.shadowcolor;
    }

    public void setShadowcolor(int shadowcolor) {
        this.shadowcolor = shadowcolor;
    }

    public float getShadowradius() {
        return this.shadowradius;
    }

    public void setShadowradius(float shadowradius) {
        this.shadowradius = shadowradius;
    }

    public float getShadowx() {
        return this.shadowx;
    }

    public void setShadowx(float shadowx) {
        this.shadowx = shadowx;
    }

    public float getShadowy() {
        return this.shadowy;
    }

    public void setShadowy(float shadowy) {
        this.shadowy = shadowy;
    }
}
