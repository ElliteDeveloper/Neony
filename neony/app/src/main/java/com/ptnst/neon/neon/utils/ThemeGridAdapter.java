package com.ptnst.neon.neon.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.ThemeActivity;
import com.ptnst.neon.neon.model.ThemeCateData;

import java.util.List;

public class ThemeGridAdapter extends RecyclerView.Adapter<ThemeGridAdapter.ViewHolder> {
    private List<ThemeCateData> mDataset;
    private ThemeActivity mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImgView;
        public RelativeLayout mLayoutItem;
        public TextView mTxtView;
        public ViewHolder(View v) {
            super(v);
            mImgView = v.findViewById(R.id.img_thumb);
            mLayoutItem = v.findViewById(R.id.layout_item);
            mTxtView = v.findViewById(R.id.txt_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ThemeGridAdapter(List<ThemeCateData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ThemeGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        mContext = (ThemeActivity) parent.getContext();
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid_theme, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mImgView.setImageResource(mContext.getResources().getIdentifier("img_" + mDataset.get(position).key, "drawable", mContext.getPackageName()));
        holder.mTxtView.setText(mDataset.get(position).name);
        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ThemeActivity)mContext).searchWords(mDataset.get(position).name, position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
