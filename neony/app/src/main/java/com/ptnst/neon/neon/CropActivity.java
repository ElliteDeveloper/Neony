package com.ptnst.neon.neon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptnst.neon.neon.utils.GlobalConst;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

import java.io.File;
import java.util.Locale;

public class CropActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_CROP_PERMISSIONS = 5;
    UCropView mCropView;
    GestureCropImageView mGestureCropImageView;
    OverlayView mOverlayView;
    HorizontalProgressWheelView mRotateWheel;
    TextView mTxtAngle;
    ImageView mImgRatio;
    int mRatio;
    String mFilePath;
    String mStrFileType = "";
    float mOriginalRatio;
    boolean mIsChangedRatio = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        initView();

    }
    private void initView(){

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.txt_crop).setOnClickListener(this);
        findViewById(R.id.txt_reset).setOnClickListener(this);
        findViewById(R.id.img_rotate).setOnClickListener(this);
        findViewById(R.id.img_ratio).setOnClickListener(this);

        mCropView = findViewById(R.id.ucrop);
        mRotateWheel = findViewById(R.id.wheel_rotate);
        mTxtAngle = findViewById(R.id.txt_angle);
        mImgRatio = findViewById(R.id.img_ratio);
        mGestureCropImageView = mCropView.getCropImageView();
        mOverlayView = mCropView.getOverlayView();


        mRatio = getIntent().getIntExtra("ratio", 0);
        mFilePath = getIntent().getStringExtra("file");
        mStrFileType = getIntent().getStringExtra("file_type");
        if (mStrFileType == null) mStrFileType = "file";

        // Crop image view options

        mGestureCropImageView.setMaxBitmapSize(CropImageView.DEFAULT_MAX_BITMAP_SIZE);
        mGestureCropImageView.setMaxScaleMultiplier(CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER);
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);
        mGestureCropImageView.setScaleEnabled(true);
        mGestureCropImageView.setRotateEnabled(false);

        Uri iUri, oUri;
        if (mStrFileType.equals("file")){
            iUri = Uri.fromFile(new File(mFilePath));
            File file = new File(GlobalConst.APP_PHOTO_TEMP_PATH, GlobalConst.APP_TEMP_CROP_FILE_NAME);
            oUri = Uri.fromFile(file);
        } else {
            iUri = Uri.parse(mFilePath);
            oUri = Uri.parse(getIntent().getStringExtra("output_file"));
        }

        try {
            mGestureCropImageView.setImageUri(iUri, oUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Overlay view options


        mOverlayView.setDimmedColor(getResources().getColor(R.color.ucrop_color_default_dimmed));
        mOverlayView.setCircleDimmedLayer(OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER);

        mOverlayView.setShowCropFrame( OverlayView.DEFAULT_SHOW_CROP_FRAME);
        mOverlayView.setCropFrameColor(getResources().getColor(R.color.ucrop_color_default_crop_frame));
        mOverlayView.setCropFrameStrokeWidth(getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width));

        mOverlayView.setShowCropGrid(OverlayView.DEFAULT_SHOW_CROP_GRID);
        mOverlayView.setCropGridRowCount(2);
        mOverlayView.setCropGridColumnCount(2);
        mOverlayView.setCropGridColor(getResources().getColor(R.color.ucrop_color_default_crop_grid));
        mOverlayView.setCropGridStrokeWidth(getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width));

        if (mRatio == 0){
            mOriginalRatio = 1.0f;
        }else if (mRatio == 1){
            mOriginalRatio = 4.0f / 5.0f;
        }else{
            mOriginalRatio = CropImageView.SOURCE_IMAGE_ASPECT_RATIO;
            mOverlayView.setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE);
        }
        mGestureCropImageView.setTargetAspectRatio(mOriginalRatio);
        mGestureCropImageView.setTransformImageListener( new TransformImageView.TransformImageListener() {
            @Override
            public void onRotate(float currentAngle) {
                setAngle(currentAngle);
            }

            @Override
            public void onScale(float currentScale) {

            }

            @Override
            public void onLoadComplete() {
                mCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());

            }

            @Override
            public void onLoadFailure(@NonNull Exception e) {
                finish();
            }

        });
        setAngle(0.0f);
        final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
        mRotateWheel.setMiddleLineColor(getResources().getColor(R.color.colorWhite));
        mRotateWheel.setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScroll(float delta, float totalDistance) {
                float angle = -delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT;
                mGestureCropImageView.postRotate(angle);

            }

            @Override
            public void onScrollEnd() {
                mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                mGestureCropImageView.cancelAllAnimations();
            }
        });
    }

    private Uri getImageUri(File file){
        Uri imgUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider", file);
        mFilePath = file.getAbsolutePath();
        return imgUri;
    }

    private void reset() {
        mGestureCropImageView.postRotate(-mGestureCropImageView.getCurrentAngle());
        mIsChangedRatio = false;
        mGestureCropImageView.setTargetAspectRatio(mOriginalRatio);
        mImgRatio.setImageResource(R.drawable.ic_ratio_square);
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setAngle(float angle){
        mTxtAngle.setText(String.format(Locale.getDefault(), "%.1f°", angle));
    }

    private void rotateByAngle(int angle) {
        mGestureCropImageView.postRotate(angle);
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGestureCropImageView != null) {
            mGestureCropImageView.cancelAllAnimations();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back){
            onBackPressed();
        }else if (v.getId() == R.id.txt_crop){
            mGestureCropImageView.cropAndSaveImage(Bitmap.CompressFormat.JPEG, 90, new BitmapCropCallback() {
                @Override
                public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                    Intent intent =  new Intent();
                    intent.putExtra("ratio", mGestureCropImageView.getTargetAspectRatio());
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        intent.putExtra("file", mGestureCropImageView.getImageOutputPath());
//                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onCropFailure(@NonNull Throwable t) {
                    finish();
                }
            });
        }else if (v.getId() == R.id.txt_reset){
            reset();
        }else if (v.getId() == R.id.img_rotate){
            rotateByAngle(90);
        }else if (v.getId() == R.id.img_ratio){
            if (mIsChangedRatio){
                mIsChangedRatio = false;
                mGestureCropImageView.setTargetAspectRatio(mOriginalRatio);
                mImgRatio.setImageResource(R.drawable.ic_ratio_square);
            }else{
                mIsChangedRatio = true;
                mGestureCropImageView.setTargetAspectRatio(1.0f);
                mImgRatio.setImageResource(R.drawable.ic_ratio_origin);
            }
        }
    }



}
