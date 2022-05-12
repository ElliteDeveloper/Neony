package com.ptnst.neon.neon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.NativeExpressAdView;
import com.ptnst.neon.neon.R;

public class NativeAdDialog extends Dialog {



    View.OnClickListener mClickListener;

    public NativeAdDialog(@NonNull Context context, View.OnClickListener clickListener) {
        super(context);
        mClickListener = clickListener;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public NativeAdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    protected NativeAdDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_native_ad);
        findViewById(R.id.btn_yes).setOnClickListener(mClickListener);
        findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*NativeExpressAdView adView = findViewById(R.id.dialog_native_ad);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);*/
        AdRequest adRequest = new AdRequest.Builder()
                .build();//.addTestDevice("55A65AD550C58702609E8BF0A0F1E3FF").build();


        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
    }
}
