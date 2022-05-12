package com.ptnst.neon.neon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ptnst.neon.neon.R;

public class SelectRatioDialog extends Dialog {

    ratioCallBack mCallBack;
    boolean mIsHiddenFree;

    public interface ratioCallBack{
        public void onClick(int type);
    }

    public SelectRatioDialog(@NonNull Context context, ratioCallBack callBack) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mCallBack = callBack;
        mIsHiddenFree = false;
    }

    public SelectRatioDialog(@NonNull Context context, ratioCallBack callBack, boolean isHiddenFree) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mCallBack = callBack;
        mIsHiddenFree = true;
    }

    public SelectRatioDialog(@NonNull Context context) {
        super(context);
    }

    public SelectRatioDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectRatioDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_select_ratio);
        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.layout_square).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(0);
                dismiss();
            }
        });
        findViewById(R.id.layout_for_instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(1);
                dismiss();
            }
        });
        findViewById(R.id.layout_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClick(2);
                dismiss();
            }
        });
        if (mIsHiddenFree){
            TextView txtCustomLabel = findViewById(R.id.txt_custom_label);
            TextView txtCustomComment = findViewById(R.id.txt_custom_comment);
            txtCustomLabel.setText(R.string.five_four);
            txtCustomComment.setText(R.string.for_five_four);
        }
    }
}
