package com.ptnst.neon.neon.utils;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ExifInterface;

import com.yalantis.ucrop.util.EglUtils;

public class BitmapUtils {
    public static int GetExifOrientation(String str) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(str);
        } catch (Exception e) {
            exifInterface = null;
        }
        if (exifInterface != null) {
            int exif = exifInterface.getAttributeInt("Orientation", -1);
            if (exif !=  -1) {
                if (exif == ExifInterface.ORIENTATION_ROTATE_180) {
                    return 180;
                }
                if (exif == ExifInterface.ORIENTATION_ROTATE_90) {
                    return 90;
                }
                if (exif == ExifInterface.ORIENTATION_ROTATE_270) {
                    return 270;
                }
            }
        }
        return 0;
    }

    public static int CalculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int hei = options.outHeight;
        int wid = options.outWidth;
        int ratio = 1;
        if (hei > height && wid > width) {
            while (hei / ratio > height && wid / ratio > width) {
                ratio *= 2;
            }
        }

        return ratio;
    }



    public static int CalculateMaxBitmapSize(int wid, int hei) {
        int dis = (int) Math.sqrt(Math.pow((double) wid, 2.0d) + Math.pow((double) hei, 2.0d));

        Canvas canvas = new Canvas();
        int min = Math.min(canvas.getMaximumBitmapWidth(), canvas.getMaximumBitmapHeight());

        if (min > 0) {
            dis = Math.min(dis, min);
        }
        min = EglUtils.getMaxTextureSize();

        if (min > 0) {
            dis = Math.min(dis, min);
        }

        return dis;
    }


}
