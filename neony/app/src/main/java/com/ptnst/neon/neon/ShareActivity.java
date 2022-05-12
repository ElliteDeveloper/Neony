package com.ptnst.neon.neon;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class ShareActivity extends BaseActivity implements View.OnClickListener{

    String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaScannerConnection.scanFile(ShareActivity.this,
                        new String[] { mFilePath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }
        }).start();


    }
    private void initView(){

        mFilePath = getIntent().getStringExtra("filepath");
        findViewById(R.id.img_back).setOnClickListener(this);


        TextView txtTitle = findViewById(R.id.txt_title_large);
        txtTitle.setText(R.string.share);

        findViewById(R.id.txt_instagram).setOnClickListener(this);
        findViewById(R.id.txt_facebook).setOnClickListener(this);
        findViewById(R.id.txt_more).setOnClickListener(this);
        findViewById(R.id.txt_rating).setOnClickListener(this);

        if (!verificaInstagram("com.instagram.android")) {
            TextView txtView = findViewById(R.id.txt_instagram);
            txtView.setTextColor(getResources().getColor(R.color.colorGray));
            txtView.setEnabled(false);
        }

        if (!verificaInstagram("com.facebook.katana")) {
            TextView txtView = findViewById(R.id.txt_facebook);
            txtView.setTextColor(getResources().getColor(R.color.colorGray));
            txtView.setEnabled(false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.saved)
                .setMessage(R.string.saved_comment)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
        loadBannerAd();
    }

    private void shareWithPackage(String packageName){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(mFilePath));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_STREAM, photoURI);
        share.setPackage(packageName);
        startActivity(Intent.createChooser(share, getString(R.string.share)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();
        }else if (v.getId() == R.id.txt_instagram){
            shareWithPackage("com.instagram.android");

        }else if (v.getId() == R.id.txt_facebook){
            shareWithPackage("com.facebook.katana");
        }else if (v.getId() == R.id.txt_more){
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(mFilePath));
            startActivity(Intent.createChooser(share, getString(R.string.share)));

        }else if (v.getId() == R.id.txt_rating){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        }
    }

    private boolean verificaInstagram(String packageName){
        boolean installed = false;

        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, 0);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    public class MediaScannerWrapper implements
            MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mConnection;
        private String mPath;
        private String mMimeType;

        // filePath - where to scan;
        // mime type of media to scan i.e. "image/jpeg".
        // use "*/*" for any media
        public MediaScannerWrapper(Context ctx, String filePath, String mime){
            mPath = filePath;
            mMimeType = mime;
            mConnection = new MediaScannerConnection(ctx, this);
        }

        // do the scanning
        public void scan() {
            mConnection.connect();
        }

        // start the scan when scanner is ready
        public void onMediaScannerConnected() {
            mConnection.scanFile(mPath, mMimeType);
            Log.w("MediaScannerWrapper", "media file scanned: " + mPath);
        }

        public void onScanCompleted(String path, Uri uri) {
            // when scan is completes, update media file tags
        }
    }
}
