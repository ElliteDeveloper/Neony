package com.ptnst.neon.neon.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ptnst.neon.neon.GalleryActivity;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.model.AlbumData;

import java.util.List;


/**
 * Created by Diamond on 6/27/2017.
 */

public class AlbumListAdapter extends BaseAdapter {


    private GalleryActivity context;
    private LayoutInflater inflater;
    private List<AlbumData> mDataList;

    public AlbumListAdapter(GalleryActivity context, List<AlbumData> dataList){
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return this.mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_album, parent, false);

            holder.txtName = convertView.findViewById(R.id.txt_name);
            holder.txtCount = convertView.findViewById(R.id.txt_count);
            holder.imgThumb = convertView.findViewById(R.id.img_thumb);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final AlbumData data = mDataList.get(position);
        holder.txtName.setText(data.albumName);
        holder.txtCount.setText(String.format(context.getString(R.string.num_photos), data.listPhotos.size()));
        Glide.with(context).load(data.image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgThumb);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.closeMenu();
                context.setFolder(data.albumId);
            }
        });


        return convertView;
    }





    /** View holder for efficiency. */
    static class ViewHolder {
        TextView txtName;
        TextView txtCount;
        ImageView imgThumb;
    }
}
