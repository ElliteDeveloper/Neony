package com.ptnst.neon.neon.views;

import android.graphics.Point;

public class EditConfig {
    public static int GetTextPaddingSize(int size) {
        return size / 24;
    }


    public static Point GetTextPosition(int wid, int hei, int canWid, int canHei) {
        Point point = new Point();

        int x = (canWid / 2) - (wid / 2);
        int y = (canHei / 2) - (hei / 2);

        point.set(x, y);
        return point;
    }

    public static int GetLeftStylePadding(int width1, int width2) {
        int GetTextPaddingSize = GetTextPaddingSize(width2);
        return (GetTextPaddingSize * 2) + width1 < width2 ? (width2 - width1) / 2 : GetTextPaddingSize;
    }

    public static int GetTopStylePadding(int height1, int height2) {
        int GetTextPaddingSize = GetTextPaddingSize(height2);
        return (GetTextPaddingSize * 2) + height1 < height2 ? (height2 - height1) / 2 : GetTextPaddingSize;
    }


}
