package com.ptnst.neon.neon;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ptnst.neon.neon.dialog.PermissionNotifyDialog;
import com.ptnst.neon.neon.dialog.SelectRatioDialog;
import com.ptnst.neon.neon.utils.GlobalConst;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_COLOR = 3;
    float mColorRatioType = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAdmob();
        initView();
        checkPermission();


    }

    private void initView() {
        findViewById(R.id.btn_theme_image).setOnClickListener(this);
        findViewById(R.id.btn_your_photo).setOnClickListener(this);
        findViewById(R.id.btn_color).setOnClickListener(this);
        findViewById(R.id.img_right).setOnClickListener(this);
        findViewById(R.id.img_today).setOnClickListener(this);
    }

    private void loadAdmob() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewmainact);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                Toast.makeText(MainActivity.this, "loaded", Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
//                Toast.makeText(MainActivity.this, "error"+adError, Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        //.addTestDevice("55A65AD550C58702609E8BF0A0F1E3FF").build();


    }

    static final int REQUEST_PERMISSIONS = 3;

    private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()) {
//                File file = new File(getExternalFilesDir(null));
////                File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//            } else {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        }else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            /*if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {*/
                PermissionNotifyDialog dlg = new PermissionNotifyDialog(this);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_MEDIA_LOCATION},
                                REQUEST_PERMISSIONS);
                    }
                });
                dlg.show();
            }
        }else {
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            /*if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {*/
                PermissionNotifyDialog dlg = new PermissionNotifyDialog(this);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                });
                dlg.show();
            }
        }
//            else {
//                File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_theme_image) {
            Intent intent = new Intent(this, ThemeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_your_photo) {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_color) {
            SelectRatioDialog dlg = new SelectRatioDialog(this, new SelectRatioDialog.ratioCallBack() {
                @Override
                public void onClick(int type) {
                    if (type == 0) {
                        mColorRatioType = 1.0f;
                    } else if (type == 1) {
                        mColorRatioType = 4.0f / 5.0f;
                    } else if (type == 2) {
                        mColorRatioType = 5.0f / 4.0f;
                    }
                    Intent intent = new Intent(MainActivity.this, SelectColorActivity.class);
                    startActivityForResult(intent, REQUEST_COLOR);
                }
            }, true);
            dlg.show();
        } else if (v.getId() == R.id.img_right) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.img_today) {
            Uri uri = Uri.parse("http://instagram.com/_u/neony.app");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/neony.app")));
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COLOR) {
            if (resultCode == RESULT_OK) {
                int colorBack = data.getIntExtra("color", 0xFFFFFFFF);
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("ratio", mColorRatioType);
                intent.putExtra("color", colorBack);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                    }
                }
            }
        }
    }
}
