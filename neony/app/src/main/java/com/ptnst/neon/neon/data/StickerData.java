package com.ptnst.neon.neon.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ptnst.neon.neon.views.OutGlowImageView;

public class StickerData extends TextData {
    private float scale = 1.0f;
    private int visible = 1;
    private int blur = 0;
    private int originalWidth = 100;
    private int originalHeight = 100;

    public int firstViewWidth;
    public int firstViewHeight;
    public Bitmap mBitmap;
    int inner = 1;

    public void StyleData(){

    }

    public void setInner(int blur){
        this.inner = blur;
    }

    public int getInner(){
        return this.inner;
    }

    public int getBlur(){
        return this.blur;
    }

    public void setBlur(int blur){
        this.blur = blur;
    }

    public int getVisible() {
        return this.visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setOriginalSize(int width, int height){
        originalHeight = height;
        originalWidth = width;
    }

    public int getOrigianlWidth(){
        return originalWidth;
    }
    public int getOriginalHeight(){
        return originalHeight;
    }

    public void applySticker(OutGlowImageView imgView){
//        imgView.setScaleX(scale );
//        imgView.setScaleY(scale );
        imgView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imgView.setRotation(degree);
        imgView.setBlurColor(color);
        imgView.setShadow(shadow);
        imgView.setImageBlur(blur);
        imgView.setImageInner(inner);
        imgView.invalidate();

    }
}
