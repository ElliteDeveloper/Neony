package com.ptnst.neon.neon.data;

public class StyleData {
    int alpha = 0xFF;
    int color = 0x00000000;
    int bright = 50;



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



    public int getBright() {
        return this.bright;
    }

    public void setBright(int bright) {
        this.bright = bright;
    }
}