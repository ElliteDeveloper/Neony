package com.ptnst.neon.neon.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptnst.neon.neon.CropActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.dialog.SelectRatioDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

public class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.ViewHolder> {
    private List<String> mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImgView;
        public LinearLayout mLayoutItem;
        public ViewHolder(View v) {
            super(v);
            mImgView = v.findViewById(R.id.img_thumb);
            mLayoutItem = v.findViewById(R.id.layout_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GalleryGridAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GalleryGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        mContext = parent.getContext();
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Glide.with(mContext).load(mDataset.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.mImgView);
        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRatioDialog dlg = new SelectRatioDialog(mContext, new SelectRatioDialog.ratioCallBack() {
                    @Override
                    public void onClick(int type) {
                        Intent intent = new Intent(mContext, CropActivity.class);
                        intent.putExtra("ratio", type);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            intent.putExtra("file", Uri.fromFile(new File(mDataset.get(position))).toString());
                            Uri outputImagePathUri = GlobalConst.getImageUriFromPictures(mContext.getContentResolver(), GlobalConst.APP_TEMP_CROP_FILE_NAME);
                            try {
                                OutputStream oStream = mContext.getContentResolver().openOutputStream(outputImagePathUri);
                                oStream.write(10);
                            } catch (Exception e) {
                                Toast.makeText(mContext, "An error occurred while opening image!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            intent.putExtra("file_type", "uri");
                            intent.putExtra("output_file", GlobalConst.getImagePathUriFromPictures(mContext.getContentResolver(), GlobalConst.APP_TEMP_CROP_FILE_NAME).toString());
                        }else{
                            intent.putExtra("file", mDataset.get(position));
                        }
                        ((Activity)mContext).startActivityForResult(intent, CropActivity.REQUEST_CROP_PERMISSIONS);
                    }
                });
                dlg.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
