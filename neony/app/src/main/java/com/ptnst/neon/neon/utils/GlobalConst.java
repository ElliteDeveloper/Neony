package com.ptnst.neon.neon.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GlobalConst {
    public static final String APP_PHOTO_TEMP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.neony";
    public static final String APP_TEMP_FILE_NAME = "temp.jpg";
    public static final String APP_TEMP_CROP_FILE_NAME = "neony_crop.jpg";


    // public final static String SKU_FONT = "font";

    // If you changed the fonts, change this part "name".
    public static final String PREF_FONT_SAVE_DATA_NAME = "purchased_font_1";

    public static final String PREF_STICKER_SAVE_DATA_NAME = "purchased_sticker";


    public static final String UNSPLASH_ACCESS_KEY = "0ff853db3a903689b7dfc4cab67db7101c12160c06ab58723e2c4d40e31d447b";
    public static final String UNSPLASH_SECRET_KEY = "eafd30f6fb97bbf11529fa85559bcde4e41cd1d63d0c92acb66c2bd8eee63263";
    public static final String UNSPLASH_HOST_NAME =  "https://api.unsplash.com";
    public static final int UNPLASH_PAGE_COUNT = 20;

    public static final int[] mTryStickerPosition = new int[1000];

    public static void initStickerFree(){
        for (int i = 0; i<mTryStickerPosition.length; i++){
            mTryStickerPosition[i] = 0;
        }
    }

    public static int[] BACKGROUND_COLORS = new int[]{
            0xFFFFA4B3, 0xFFEB7395, 0xFFFF4278, 0xFFF80060, 0xFFE9066F,
            0xFFFF4545, 0xFFFF2B0F, 0xFFFC0000, 0xFFDC160F, 0xFFC3000F,
            0xFFFCE07F, 0xFFFFEA00, 0xFFE3D512, 0xFFFFBA00, 0xFFFC7637,
            0xFF75FF7F, 0xFF00FF02, 0xFF00EC40, 0xFF00D751, 0xFF01C207,
            0xFF51FFD0, 0xFF4DF7D5, 0xFF02F2C2, 0xFF25DEB0, 0xFF0DD1BC,
            0xFF9AFDFF, 0xFF2FFAFF, 0xFF01F3FF, 0xFF04E6FF, 0xFF00B3FF,
            0xFF147DFF, 0xFF3862FF, 0xFF1F4EFF, 0xFF043EFC, 0xFF0001FE,
            0xFFE681FF, 0xFFFF48FF, 0xFFE42896, 0xFFC728E4, 0xFFA200FF,
            0xFF483B48, 0xFF6C7590, 0xFF5187B3, 0xFF48C8EE, 0xFF9DD6DF,
            0xFF1F4255, 0xFF56999F, 0xFF21AA98, 0xFF6ACACD, 0xFF92D1C9,
            0xFF503E37, 0xFFAA7D71, 0xFFBA9D80, 0xFFBDADA0, 0xFFCFC5BC,
            0xFFF9A130, 0xFFFEC955, 0xFFF9DA84, 0xFF778586, 0xFF8CAFC3,
            0xFFA5C2C4, 0xFFF79D8C, 0xFFFF2365, 0xFF790579, 0xFF0F2EF4,
            0xFF19EAA7, 0xFFD4F712, 0xFFF4F61C, 0xFFFFFFFF, 0xFF020202,
            0xFFFFD9E7, 0xFFE582A1, 0xFFE54274, 0xFF922C4C, 0xFF23000B,
            0xFFFFD9F0, 0xFFE582BD, 0xFFE542A4, 0xFF922C69, 0xFF230015,
            0xFFFFD9FB, 0xFFE582DB, 0xFFE542D5, 0xFF922C88, 0xFF230020,
            0xFFF7D9FF, 0xFFD182E5, 0xFFC442E5, 0xFF7E2C92, 0xFF1C0023,
            0xFFECD9FF, 0xFFB482E5, 0xFF9442E5, 0xFF5F2C92, 0xFF120023,
            0xFFE1D9FF, 0xFF9682E5, 0xFF6342E5, 0xFF402C92, 0xFF070023,
            0xFFD9DDFF, 0xFF828CE5, 0xFF4252E5, 0xFF2C3692, 0xFF000323,
            0xFFD9E8FF, 0xFF82AAE5, 0xFF4283E5, 0xFF2C5592, 0xFF000F26,
            0xFFD9F4FF, 0xFF82C7E5, 0xFF42B4E5, 0xFF2C7392, 0xFF001923,
            0xFFD9FFFF, 0xFF82E5E5, 0xFF42E6E6, 0xFF2C9292, 0xFF002323,
            0xFFD9FFF4, 0xFF82E5C7, 0xFF42E5B4, 0xFF2C9273, 0xFF002319,
            0xFFD9FFE8, 0xFF82E5AA, 0xFF42E583, 0xFF2C9255, 0xFF00230E,
            0xFFD9FFDD, 0xFF82E58C, 0xFF42E552, 0xFF2C9236, 0xFF002303,
            0xFFE1FFD9, 0xFF96E582, 0xFF63E542, 0xFF40922C, 0xFF072300,
            0xFFECFFD9, 0xFFB4E582, 0xFF94E542, 0xFF5F922C, 0xFF122300,
            0xFFF7FFD9, 0xFFD1E582, 0xFFC4E542, 0xFF7E922C, 0xFF1C2300,
            0xFFFFFBD9, 0xFFE5DB82, 0xFFE5D542, 0xFF92882C, 0xFF232000,
            0xFFFFF0D9, 0xFFE5BD82, 0xFFE5A442, 0xFF92692C, 0xFF231500,
            0xFFFFE4D9, 0xFFE5A082, 0xFFE57342, 0xFF924B2C, 0xFF924B2C,
            0xFFFFD9D9, 0xFFE58282, 0xFFE54242, 0xFF922C2C, 0xFF230000,
            0xFFE4E4E4, 0xFFA0A0A0, 0xFF737373, 0xFF4B4B4B, 0xFF0B0B0B
    };

    public static final int REQUEST_COLOR = 100;
    public static final int REQUEST_NEON_COLOR = 200;
    public static final int REQUEST_STICKER_NEON_COLOR = 300;
    public static final int REQUEST_BUY_STICKER = 400;
    public static final int REQUEST_BUY_FONT = 500;


    public static final String STICKER_ASSET_FOLDER = "stickers";
    public static final String STICKER_ASSET_PATH = "file:///android_asset/" + STICKER_ASSET_FOLDER +  "/";

    public static final String FONT_ASSET_FOLDER = "fonts";
    public static final String FONT_ASSET_PATH = "file:///android_asset/" + FONT_ASSET_FOLDER +  "/";

    public static void showImageFromAsset(Context context, String path, ImageView imageView){
        Glide.with(context)
                .load(Uri.parse(path))
                .into(imageView);
    }

    public static String getPurchasedStickers(Context context){
        SharedPreferences pref = context.getSharedPreferences("neon", Context.MODE_PRIVATE);
        return pref.getString(PREF_STICKER_SAVE_DATA_NAME, "");
    }

    public static void setPurcahsedSticker(Context context, String value){
        SharedPreferences pref = context.getSharedPreferences("neon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_STICKER_SAVE_DATA_NAME, value);
        editor.commit();
    }

    public static String getPurchasedFonts(Context context){
        SharedPreferences pref = context.getSharedPreferences("neon", Context.MODE_PRIVATE);
        return pref.getString(PREF_FONT_SAVE_DATA_NAME, "");
    }

    public static void setPurcahsedFont(Context context, String value){
        SharedPreferences pref = context.getSharedPreferences("neon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_FONT_SAVE_DATA_NAME, value);
        editor.commit();
    }

    public static Uri getImageUri(ContentResolver cr, ContentValues cv){
        Uri uri = null;
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(contentUri,
                    null,
                    MediaStore.MediaColumns.DISPLAY_NAME + "='"+cv.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)+"'",
                    null, null);
            if (cursor == null || cursor.getCount() == 0){
                if (cursor != null) cursor.close();
                uri = cr.insert(contentUri, cv);
            }else{
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getImageUriFromPictures(ContentResolver cr, String strDisplayName){
        Uri uri = null;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, strDisplayName);
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(contentUri,
                    null,
                    MediaStore.MediaColumns.DISPLAY_NAME + "='"+cv.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)+"'",
                    null, null);
            if (cursor == null || cursor.getCount() == 0){
                cursor.close();
                uri = cr.insert(contentUri, cv);
            }else{
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getImagePathUriFromPictures(ContentResolver cr, String strDisplayName){
        Uri uri = null;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, strDisplayName);
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(contentUri,
                    null,
                    MediaStore.MediaColumns.DISPLAY_NAME + "='"+cv.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)+"'",
                    null, null);
            if (cursor == null || cursor.getCount() == 0){
                cursor.close();
                uri = cr.insert(contentUri, cv);
            }else{
                cursor.moveToFirst();
                uri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getImageUriFromDCIM(ContentResolver cr, String strDisplayName){
        Uri uri = null;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, strDisplayName);
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/neony");
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(contentUri,
                    null,
                    MediaStore.MediaColumns.DISPLAY_NAME + "='"+cv.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)+"'",
                    null, null);
            if (cursor == null || cursor.getCount() == 0){
                cursor.close();
                uri = cr.insert(contentUri, cv);
            }else{
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uri;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getImagePathUriFromDCIM(ContentResolver cr, String strDisplayName){
        Uri uri = null;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, strDisplayName);
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/neony");
        try {
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = cr.query(contentUri,
                    null,
                    MediaStore.MediaColumns.DISPLAY_NAME + "='"+cv.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)+"'",
                    null, null);
            if (cursor == null || cursor.getCount() == 0){
                cursor.close();
                uri = cr.insert(contentUri, cv);
            }else{
                cursor.moveToFirst();
                uri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                cursor.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return uri;
    }
}