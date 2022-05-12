package com.ptnst.neon.neon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptnst.neon.neon.FindTextActivity;
import com.ptnst.neon.neon.R;


/**
 * Created by Diamond on 6/27/2017.
 */

public class FindTextListAdapter extends BaseAdapter {


    private FindTextActivity context;
    private LayoutInflater inflater;
    private String[] mDataList;
    private int mLevel;

    public FindTextListAdapter(FindTextActivity context, String[] dataList, int level){
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataList = dataList;
        mLevel = level;
    }

    @Override
    public int getCount() {
        return this.mDataList.length;
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
            convertView = inflater.inflate(R.layout.row_find_text, parent, false);

            holder.txtName = convertView.findViewById(R.id.txt_name);
            holder.imgIcon = convertView.findViewById(R.id.img_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final String data = mDataList[position];
        holder.txtName.setText(data);
        if (mLevel == -1){
            holder.imgIcon.setImageResource(R.drawable.ic_right_arrow);
            convertView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    context.setList(position, data);
                }
            });
        }else{
            holder.imgIcon.setImageResource(R.drawable.ic_copy);
            holder.imgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(context.getResources().getString(R.string.copied), data);
                    clipboard.setPrimaryClip(clip);
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context);//, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder .setTitle(R.string.copied)
                            .setMessage(R.string.copied_clipboard)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    context.finish();
                                }
                            })
                            .show();
                }
            });
        }

        return convertView;
    }





    /** View holder for efficiency. */
    static class ViewHolder {
        TextView txtName;
        ImageView imgIcon;
    }
}
