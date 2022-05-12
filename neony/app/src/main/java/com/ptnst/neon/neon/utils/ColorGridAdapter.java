package com.ptnst.neon.neon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ptnst.neon.neon.R;

public class ColorGridAdapter extends RecyclerView.Adapter<ColorGridAdapter.ViewHolder> {
    private int[] mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RoundedImageView mImgView;
        public LinearLayout mLayoutItem;
        public ViewHolder(View v) {
            super(v);
            mImgView = v.findViewById(R.id.img_thumb);
            mLayoutItem = v.findViewById(R.id.layout_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ColorGridAdapter(int[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ColorGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        mContext = parent.getContext();
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mImgView.setBackgroundColor(GlobalConst.BACKGROUND_COLORS[position]);
        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("color", GlobalConst.BACKGROUND_COLORS[position]);
                ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                ((Activity)mContext).finish();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }



}
