package com.ptnst.neon.neon.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;

import com.ptnst.neon.neon.views.BlurTextView;

public class TextData extends TextAttr {
    protected String text = "";
    int blurColor = 0xFFFF2366; //Default neon color
    int blur = 10;
    int shadow = 50;
    int inner = 1;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBlurColor(int color){
        this.blurColor = color;
    }

    public void setBlur(int blur){
        this.blur = blur;
    }

    public void setInner(int blur){
        this.inner = blur;
    }

    public int getBlurColor(){
        return this.blurColor;
    }

    public int getBlur(){
        return this.blur;
    }

    public int getInner(){
        return this.inner;
    }

    public void setShadow(int shadow){
        this.shadow = shadow;
    }

    public int getShadow(){
        return this.shadow;
    }


    public void applyTextView(EditText editText) {
        applyTextView(editText, -1, true);
        if (((BlurTextView)editText).mBitmapBlur != null){
            ((BlurTextView)editText).mBitmapBlur.recycle();
        }

        editText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        ((BlurTextView)editText).mBitmapBlur = null;
        ((BlurTextView)editText).setShadow(shadow);
        ((BlurTextView)editText).setBlurColor(blurColor);
        ((BlurTextView)editText).setImageBlur(blur);
        ((BlurTextView)editText).setImageInner(inner);

        /*
        editText.setLayerType(editText.LAYER_TYPE_SOFTWARE, null);

        ((BlurTextView)editText).freeze();

        editText.setDrawingCacheEnabled(true);
//        editText.invalidate();
        editText.buildDrawingCache();
        try {
            Bitmap bitmap = Bitmap.createBitmap(editText.getDrawingCache());
            ((BlurTextView) editText).setImageBlur(bitmap);
            editText.invalidate();
        }catch (Exception e){

        }
        ((BlurTextView)editText).unfreeze();
        */
        editText.invalidate();
    }
}
