package com.ptnst.neon.neon.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ptnst.neon.neon.R;
import com.ptnst.neon.neon.ThemeActivity;
import com.ptnst.neon.neon.dialog.OnWatchAdOKClickedListener;
import com.ptnst.neon.neon.dialog.ThemeWatchAdDialog;
import com.ptnst.neon.neon.model.ThemePhotoData;
import com.ptnst.neon.neon.views.DynamicImageView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ViewHolder> {
    private List<ThemePhotoData> mDataset;
    Context mContext;
    OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 5;
    private boolean loading;
    RewardedAd mRewardedVideoAd;
    public boolean isLoading = false;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public DynamicImageView mImgView;
        public TextView mTxtName;
        public TextView mTxtUnsplash;
        public RelativeLayout mLayoutItem;
        public ViewHolder(View v) {
            super(v);
            mImgView = v.findViewById(R.id.img_thumb);
            mTxtName = v.findViewById(R.id.txt_user);
            mTxtUnsplash = v.findViewById(R.id.txt_unsplash);
            mLayoutItem = v.findViewById(R.id.layout_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ThemeListAdapter(List<ThemePhotoData> myDataset, RecyclerView recyclerView) {
        mDataset = myDataset;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ThemeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        mContext = parent.getContext();
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_theme, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ThemePhotoData data = mDataset.get(position);
        holder.mImgView.setBackgroundColor(0x00000000);
        if (data.getWidth() <= 0 || data.getHeight() <= 0 || data.getWidth() == data.getHeight()) {
            holder.mImgView.setAspectRatio(1.0f);
        } else {
            holder.mImgView.setAspectRatio(((float) data.getWidth()) / ((float) data.getHeight()));
        }

        Glide.with(mContext).load(mDataset.get(position).getUrls().getSmall())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.mImgView);

        holder.mTxtName.setText(Html.fromHtml("<u>" + data.getUser().getName() + "</u>"));
        holder.mTxtUnsplash.setText(Html.fromHtml(mContext.getString(R.string.unsplash)));
        holder.mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                downloadImage(data);
                ThemeWatchAdDialog dlg = new ThemeWatchAdDialog(mContext, new OnWatchAdOKClickedListener() {
                    @Override
                    public void onClickedOK() {
                        loadRewardedAd(data);
                    }
                });
                dlg.show();
            }
        });
        holder.mTxtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://unsplash.com/" + data.getUser().getUsername() +"?utm_source=NEON&utm_medium=referral")));
                } catch (Exception e) {

                }
            }
        });
        holder.mTxtUnsplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://unsplash.com/?utm_source=NEON&utm_medium=referral")));
                } catch (Exception e) {

                }

            }
        });
    }

    public void downloadImage(ThemePhotoData data){
        if (data.getLinks() != null){
            String downloadUrl = data.getUrls().getRegular();
            String triggerUrl = data.getLinks().getDownload_location();
            ((ThemeActivity)mContext).showProgress();
            ApiClient.getApiClient(GlobalConst.UNSPLASH_HOST_NAME).getUnsplashDownloadUrl(triggerUrl, GlobalConst.UNSPLASH_ACCESS_KEY).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        try{
                            JSONObject json = new JSONObject(response.body().string());
                            String url = (String) json.get("url");
                            ApiClient.getApiClient(GlobalConst.UNSPLASH_HOST_NAME).downloadImageFromUnPlash(url).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    ((ThemeActivity)mContext).hideProgress();
                                    if (response.isSuccessful()){
                                        InputStream in = null;
                                        if (response.body() != null) {
                                            in = response.body().byteStream();
                                            ((ThemeActivity)mContext).downloadImage(in);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    ((ThemeActivity)mContext).hideProgress();
                                }
                            });
                            return;
                        }catch (Exception e){

                        }
                    }
                    ((ThemeActivity)mContext).hideProgress();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ((ThemeActivity)mContext).hideProgress();
                }
            });

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private void loadRewardedAd(final ThemePhotoData data) {
        if (mRewardedVideoAd == null) {
            ((ThemeActivity)mContext).showProgress();
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    mContext,
                    mContext.getString(R.string.admob_reward_id),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mRewardedVideoAd = null;
                            isLoading = false;
                            ((ThemeActivity)mContext).hideProgress();
                            downloadImage(data);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedVideoAd = rewardedAd;
                            isLoading = false;
                            ((ThemeActivity)mContext).hideProgress();
                            showRewardedVideo(data);
                        }
                    });
        }
    }

    private void showRewardedVideo(final ThemePhotoData data) {

        if (mRewardedVideoAd == null) {
            return;
        }

        mRewardedVideoAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mRewardedVideoAd = null;
                        downloadImage(data);
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mRewardedVideoAd = null;
                        downloadImage(data);
                    }
                });
        mRewardedVideoAd.show(
                (ThemeActivity)mContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();

                    }
                });
    }
}
