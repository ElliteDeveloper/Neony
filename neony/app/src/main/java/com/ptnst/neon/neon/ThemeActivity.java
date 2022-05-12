package com.ptnst.neon.neon;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.ptnst.neon.neon.dialog.SelectRatioDialog;
import com.ptnst.neon.neon.model.ThemeCateData;
import com.ptnst.neon.neon.model.ThemePhotoData;
import com.ptnst.neon.neon.model.UnsplashResultData;
import com.ptnst.neon.neon.utils.ApiClient;
import com.ptnst.neon.neon.utils.GlobalConst;
import com.ptnst.neon.neon.utils.ItemThemeOffsetDecoration;
import com.ptnst.neon.neon.utils.ThemeGridAdapter;
import com.ptnst.neon.neon.utils.ThemeListAdapter;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeActivity extends BaseActivity implements View.OnClickListener {

    EditText mEditKeyword;
    ImageView mImgSearch;
    ImageView mImgClose;
    RecyclerView mRecyclerView;
    ProgressBar mProgressView;

    RecyclerView.Adapter mAdapter;
    ThemeListAdapter mListAdapter;
    GridLayoutManager mGridManager;
    List<ThemeCateData> mListCate;
    List<ThemePhotoData> mListPhoto = new ArrayList<>();
    static final int REQUEST_PERMISSIONS = 3;

    int mPageNo = 1;
    int mPos = 0;
    boolean mPageLimit = false;
    String mKeyword = "";
    int mScreenNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);


        initView();
        loadData();
        checkPermission();
    }

    private void initView() {

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_search).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mImgSearch = findViewById(R.id.img_search);
        mImgClose = findViewById(R.id.img_close);
        mProgressView = findViewById(R.id.progress_view);
        mProgressView.setVisibility(View.INVISIBLE);
        mProgressView.setIndeterminate(true);
        mProgressView.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);
        mEditKeyword = findViewById(R.id.edit_keyword);

        mEditKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = mEditKeyword.getText().toString();
                    if (keyword.equals("")) {
                        return false;
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditKeyword.getWindowToken(), 0);
                    searchWords(keyword, 3);
                    return true;
                }
                return false;
            }
        });

    }

    private void loadData() {
        mListCate = new ArrayList<>();
        String[] arrCateNames = getResources().getStringArray(R.array.pexel_name);
        String[] arrCateKeys = getResources().getStringArray(R.array.pexel_key);
        for (int i = 0; i < arrCateNames.length; i++) {
            ThemeCateData data = new ThemeCateData();
            data.name = arrCateNames[i];
            data.key = arrCateKeys[i];
            mListCate.add(data);
        }
        showCategory();
    }

    private void showCategory() {
        mScreenNo = 0;
        mRecyclerView.setHasFixedSize(true);
        mGridManager = new GridLayoutManager(this, 6);
        mRecyclerView.setLayoutManager(mGridManager);

        while (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecorationAt(0);
        }

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.theme_spacing);
        ItemThemeOffsetDecoration itemDecoration = new ItemThemeOffsetDecoration(spacingInPixels, 3);
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new ThemeGridAdapter(mListCate);
        mRecyclerView.setAdapter(mAdapter);
        mGridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 1) {
                    return 3;
                } else {
                    return 2;
                }
            }
        });

    }

    public void searchWords(String keyword, int position) {
        mScreenNo = 1;
        mEditKeyword.setText(keyword);
        mImgSearch.setVisibility(View.GONE);
        mImgClose.setVisibility(View.VISIBLE);
        mKeyword = keyword;
        mPageNo = 1;
        mPageLimit = false;
        mPos = position;

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        while (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecorationAt(0);
        }
        mListPhoto.clear();
        mListAdapter = new ThemeListAdapter(mListPhoto, mRecyclerView);
        mListAdapter.setOnLoadMoreListener(new ThemeListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mPageLimit) {
                    mPageNo++;
                    searchTextByPage();
                }
            }
        });
        mRecyclerView.setAdapter(mListAdapter);

        if (position < 100) {
            searchTextByPage();
        } else {
            ApiClient.getApiClient("https://openapi.naver.com").translateNaver("ko", "en", mKeyword).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody body = response.body();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(body.string());
                            JSONObject jsonMsg = (JSONObject) json.get("message");
                            JSONObject jsonResult = (JSONObject) jsonMsg.get("result");
                            String translatedText = (String) jsonResult.get("translatedText");
                            mKeyword = translatedText;
                            searchTextByPage();
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("error", "translate error");
                }
            });
        }
    }

    private void resetSearch() {
        mEditKeyword.setText("");
        mImgSearch.setVisibility(View.VISIBLE);
        mImgClose.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditKeyword.getWindowToken(), 0);
        showCategory();

    }

    public void searchTextByPage() {
//        https://api.unsplash.com/search/photos?client_id=0ff853db3a903689b7dfc4cab67db7101c12160c06ab58723e2c4d40e31d447b&page=1&query=office&per_page=10
        if (mPos == 0 || mPos == 1) {
            String orderBy = "";
            if (mPos == 0) {
                orderBy = "popular";
            } else {
                orderBy = "latest";
            }
            ApiClient.getApiClient(GlobalConst.UNSPLASH_HOST_NAME).searchPhotosWithOrderBy(GlobalConst.UNSPLASH_ACCESS_KEY, orderBy, mPageNo, GlobalConst.UNPLASH_PAGE_COUNT).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            Type typeOfListOfThemePhoto = new TypeToken<List<ThemePhotoData>>() {
                            }.getType();

                            List<ThemePhotoData> search = JSON.parseObject(result, typeOfListOfThemePhoto);
                            for (int i = 0; i < search.size(); i++) {
                                mListPhoto.add(search.get(i));
                            }
                            mListAdapter.notifyDataSetChanged();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    mListAdapter.setLoaded();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mListAdapter.setLoaded();
                }
            });
        } else {
            String keyword = mKeyword;
            if (mPos < 100) {
                for (int i = 2; i < mListCate.size(); i++) {
                    if (mListCate.get(i).name.equals(mKeyword)) {
                        keyword = mListCate.get(i).key;
                        break;
                    }
                }
            }
            ApiClient.getApiClient(GlobalConst.UNSPLASH_HOST_NAME).searchPhotosWithText(GlobalConst.UNSPLASH_ACCESS_KEY, keyword, mPageNo, GlobalConst.UNPLASH_PAGE_COUNT).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            UnsplashResultData search = JSON.parseObject(result, UnsplashResultData.class);
                            for (int i = 0; i < search.results.size(); i++) {
                                mListPhoto.add(search.results.get(i));
                            }
                            mListAdapter.notifyDataSetChanged();

                            if (search.total == mListPhoto.size()) {
                                mPageLimit = true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    mListAdapter.setLoaded();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mListAdapter.setLoaded();
                }
            });
        }
    }

    public void showProgress() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressView.setVisibility(View.INVISIBLE);
    }

    public void downloadImage(InputStream in) {
        if (in == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImage(in, "neony_download.jpg");
        }else {
            boolean bFlag = false;
            File folder = new File(GlobalConst.APP_PHOTO_TEMP_PATH);
            if (!folder.exists()) {
                bFlag = folder.mkdir();
            }
            final File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH, GlobalConst.APP_TEMP_FILE_NAME);
            FileOutputStream fos = null;
            long count = 0L;

            try {
                if (!file.exists()) {
                    bFlag = file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 10];
                for (int n; -1 != (n = in.read(buffer)); count += (long) n) {
                    fos.write(buffer, 0, n);
                }
                SelectRatioDialog dlg = new SelectRatioDialog(this, new SelectRatioDialog.ratioCallBack() {
                    @Override
                    public void onClick(int type) {
                        Intent intent = new Intent(ThemeActivity.this, CropActivity.class);
                        intent.putExtra("file", file.getAbsolutePath());
                        intent.putExtra("ratio", type);
                        startActivityForResult(intent, CropActivity.REQUEST_CROP_PERMISSIONS);
                    }
                });
                dlg.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                in.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void saveImage(@NonNull InputStream in, @NonNull final String displayName) {
        ContentResolver resolver = getContentResolver();
        Uri uri = GlobalConst.getImageUriFromPictures(resolver, displayName);
        long count = 0;
        try {
            if (uri == null) {
                Toast.makeText(this, "Failed to create new MediaStore record.", Toast.LENGTH_LONG).show();
                return;
            }
            try (final OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null) {
                    Toast.makeText(this, "Failed to open output stream.", Toast.LENGTH_LONG).show();
                    return;
                }
                Uri outputImagePathUri = GlobalConst.getImageUriFromPictures(resolver, GlobalConst.APP_TEMP_CROP_FILE_NAME);
                OutputStream oStream = resolver.openOutputStream(outputImagePathUri);
                oStream.write(10);
                byte[] buffer = new byte[1024 * 10];
                for (int n; -1 != (n = in.read(buffer)); count += (long) n) {
                    stream.write(buffer, 0, n);
                }
                final Uri oUri = uri;
                final Uri outputImagePathUri1 = GlobalConst.getImagePathUriFromPictures(resolver, GlobalConst.APP_TEMP_CROP_FILE_NAME);
                SelectRatioDialog dlg = new SelectRatioDialog(this, new SelectRatioDialog.ratioCallBack() {
                    @Override
                    public void onClick(int type) {
                        Intent intent = new Intent(ThemeActivity.this, CropActivity.class);
                        intent.putExtra("file_type", "uri");
                        intent.putExtra("file", oUri.toString());
                        intent.putExtra("output_file", outputImagePathUri1.toString());
                        intent.putExtra("ratio", type);
                        startActivityForResult(intent, CropActivity.REQUEST_CROP_PERMISSIONS);
                    }
                });
                dlg.show();
            }
        } catch (IOException e) {
            if (uri != null) {
                resolver.delete(uri, null, null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mScreenNo == 1) {
            resetSearch();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            onBackPressed();
        } else if (v.getId() == R.id.img_search) {
            String keyword = mEditKeyword.getText().toString();
            if (keyword.trim().equals("")) {
                return;
            }
            searchWords(keyword, 100);
        } else if (v.getId() == R.id.img_close) {
            resetSearch();
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
                } else {
                    intent.putExtra("filepath", GlobalConst.APP_PHOTO_TEMP_PATH + "/" + GlobalConst.APP_TEMP_CROP_FILE_NAME);
                }
                startActivity(intent);
                finish();
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_MEDIA_LOCATION},
                        REQUEST_PERMISSIONS);

            }
        } else {
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
            }
        }
    }

}
