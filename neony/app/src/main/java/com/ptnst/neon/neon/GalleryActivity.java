package com.ptnst.neon.neon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptnst.neon.neon.model.AlbumData;
import com.ptnst.neon.neon.utils.AlbumListAdapter;
import com.ptnst.neon.neon.utils.GalleryGridAdapter;
import com.ptnst.neon.neon.utils.GlobalConst;
import com.ptnst.neon.neon.utils.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends BaseActivity implements View.OnClickListener{
    TextView mTxtTitleLarge;
    TextView mTxtTitleSmall;
    DrawerLayout mDrawerLayout;
    static final int REQUEST_PERMISSIONS = 3;


    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    GridLayoutManager mGridManager;
    String mAlbumId = "-----------";
    Map<String, AlbumData> mMapAlbum = new HashMap<>();
    List<String> mListAllPhotos = new ArrayList<>();

    ListView mListAlbum;
    List<AlbumData> mListAlbumData;
    AlbumListAdapter mAlbumAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView();
        checkPermission();

    }
    private void initView(){
        mTxtTitleLarge = findViewById(R.id.txt_title_large);
        mTxtTitleSmall = findViewById(R.id.txt_title_small);
        mListAlbum = findViewById(R.id.list_view);
        mListAlbumData = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mGridManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mGridManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gallery_spacing);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(spacingInPixels, 4);
        mRecyclerView.addItemDecoration(itemDecoration);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(0x20ffffff);
        findViewById(R.id.layout_title).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);


    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) &&
                        (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_MEDIA_LOCATION))) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                }
            }else {
                loadAlbum();
            }
        }else{
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                }
            }else {
                loadAlbum();
            }
        }
    }

    @SuppressLint("Range")
    private void loadAlbum(){

        findViewById(R.id.layout_menu1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
                setFolder("");
            }
        });
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        ContentResolver cr = getContentResolver();
        Cursor cur = managedQuery(images,
                projection, // Which columns to return
//                "1=1) GROUP BY " + MediaStore.Images.Media.BUCKET_ID,       // Which rows to return (all rows)
                null,
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"        // Ordering
        );

//        Cursor cur = managedQuery(images,
//                projection, // Which columns to return
//                null,
//                null,       // Selection arguments (none)
//                MediaStore.Images.Media.DATE_MODIFIED + " DESC"        // Ordering
//        );


        if (cur.moveToFirst()) {

            do {
                // Get the field values

                // Do something with the values.
                if (cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID) > 0 &&
                        cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME) > 0 &&
                        cur.getColumnIndex(MediaStore.Images.Media.DATA) > 0) {
                    AlbumData data = new AlbumData();
                    data.albumId = cur.getString(cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_ID));
                    data.albumName = cur.getString(cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    data.image = cur.getString(cur.getColumnIndex(
                            MediaStore.Images.Media.DATA));
                    mMapAlbum.put(data.albumId, data);
                    mListAlbumData.add(data);
                }
            } while (cur.moveToNext());

        }
        mAlbumAdapter = new AlbumListAdapter(this, mListAlbumData);
        mListAlbum.setAdapter(mAlbumAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = new String[] {
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DATA
                };
                Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                // Make the query.
                Cursor cur = managedQuery(images,
                        projection, // Which columns to return
                        null,       // Which rows to return (all rows)
                        null,       // Selection arguments (none)
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC"        // Ordering
                );
                if (cur.moveToFirst()) {

                    do {
                        // Get the field values

                        // Do something with the values.

                        String albumId = cur.getString(cur.getColumnIndex(
                                MediaStore.Images.Media.BUCKET_ID));
                        String image = cur.getString(cur.getColumnIndex(
                                MediaStore.Images.Media.DATA));
                        AlbumData data = mMapAlbum.get(albumId);
                        if (data != null) {
                            data.listPhotos.add(image);
                        }
                        mListAllPhotos.add(image);
                    } while (cur.moveToNext());

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtMenu1 = findViewById(R.id.txt_menu1);
                        ImageView imgMenu1 = findViewById(R.id.img_menu1);
                        txtMenu1.setText(String.format(getString(R.string.num_photos), mListAllPhotos.size()));
                        if (mListAllPhotos.size() > 0){
                            Glide.with(GalleryActivity.this).load(mListAllPhotos.get(0))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(imgMenu1);
                        }
                        mAlbumAdapter = new AlbumListAdapter(GalleryActivity.this, mListAlbumData);
                        mListAlbum.setAdapter(mAlbumAdapter);
                        setFolder("");
                    }
                });
            }
        }).start();

    }


    public void setFolder(String albumId){
        if (mAlbumId.equals(albumId)){
            return;
        }

        mAlbumId = albumId;
        if (mAlbumId.equals("")){
            mTxtTitleLarge.setText(R.string.all_photos);
            mAdapter = new GalleryGridAdapter(mListAllPhotos);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            AlbumData data = mMapAlbum.get(albumId);
            mTxtTitleLarge.setText(data.albumName);
            mAdapter = new GalleryGridAdapter(data.listPhotos);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void closeMenu(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_title){
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }else{
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        }else if (v.getId() == R.id.img_back){
            onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        loadAlbum();
                    }
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropActivity.REQUEST_CROP_PERMISSIONS) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("ratio", data.getFloatExtra("ratio", 1.0f));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent.putExtra("filepath", data.getStringExtra("file"));
                }else{
                    intent.putExtra("filepath", GlobalConst.APP_PHOTO_TEMP_PATH + "/" + GlobalConst.APP_TEMP_CROP_FILE_NAME);
                }
                startActivity(intent);
                finish();
            }
        }
    }
}

