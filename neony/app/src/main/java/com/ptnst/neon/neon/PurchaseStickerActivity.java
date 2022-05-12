package com.ptnst.neon.neon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
//import com.google.android.gms.ads.reward.RewardItem;
//import com.google.android.gms.ads.reward.RewardedVideoAd;
//import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ptnst.neon.neon.model.StickerCateData;
import com.ptnst.neon.neon.utils.GlobalConst;

import java.util.ArrayList;
import java.util.List;

public class PurchaseStickerActivity extends BaseActivity implements View.OnClickListener, PurchasesUpdatedListener {

    ImageView mImgCate;
    TextView mTxtCate;
    TextView mTxtPrice;
    ImageView mImgStickers;
    TextView mTxtBuy;
    ProgressBar mProgressView;
    public boolean isLoading = false;

    int mPos = 0;
    StickerCateData mData;

    boolean mIsShownReward = false;


    private BillingClient mBillingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_sticker);


        mPos = getIntent().getIntExtra("pos", 0);
        mData = (StickerCateData) getIntent().getSerializableExtra("data");
        initView();

        mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            //            @Override
//            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
//                if (billingResponseCode == BillingClient.BillingResponse.OK) {
//                    // The billing client is ready. You can query purchases here.
//                    queryProductDetail();
//                }else{
//                    showErrorDialog();
//                }
//            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                showErrorDialog();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
                    queryProductDetail();
                } else {
                    showErrorDialog();
                }
            }
        });
        loadRewardedAd();
//        FullScreenContentCallback fullScreenContentCallback =
//                new FullScreenContentCallback() {
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        // Code to be invoked when the ad showed full screen content.
//                    }
//
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        mRewardedVideoAd = null;
//                        // Code to be invoked when the ad dismissed full screen content.
//                    }
//                };

//        RewardedAd.load(
//                this,
//                getString(R.string.admob_reward_id),
//                new AdRequest.Builder().build(),
//                new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(RewardedAd ad) {
////                        findViewById(R.id.).setVisibility(View.VISIBLE);
//                        mRewardedVideoAd = ad;
//                        mRewardedVideoAd.setFullScreenContentCallback(fullScreenContentCallback);
//                    }
//                });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
////        mRewardedVideoAd = new RewardedAd(this, getString(R.string.admob_reward_id));
////        mRewardedVideoAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback());
//        RewardedAd.load(this, getString(R.string.admob_reward_id), adRequest,
//                new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        mRewardedVideoAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedVideoAd = rewardedAd;
//                    }
//                });

//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
//        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
//            @Override
//            public void onRewardedVideoAdLoaded() {
//                Log.e("reward", "vidoe ad loaded");
//                findViewById(R.id.progress_view).setVisibility(View.INVISIBLE);
//                findViewById(R.id.layout_try).setVisibility(View.VISIBLE);
//                mRewardedVideoAd.show();
//            }
//
//            @Override
//            public void onRewardedVideoAdOpened() {
//                Log.e("reward", "vidoe ad opened");
//            }
//
//            @Override
//            public void onRewardedVideoStarted() {
//                Log.e("reward", "vidoe ad started");
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//                Log.e("reward", "vidoe ad closed");
//
//            }
//
//            @Override
//            public void onRewarded(RewardItem rewardItem) {
//                Log.e("reward", "vidoe ad rewarded");
//            }
//
//            @Override
//            public void onRewardedVideoAdLeftApplication() {
//                Log.e("reward", "vidoe ad leftappliaction");
//            }
//
//            @Override
//            public void onRewardedVideoAdFailedToLoad(int i) {
//
//                Log.e("reward", "vidoe ad failed to loaded");
//                findViewById(R.id.progress_view).setVisibility(View.INVISIBLE);
//                findViewById(R.id.layout_try).setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onRewardedVideoCompleted() {
//                Log.e("reward", "vidoe ad completed");
//                Intent intent = new Intent();
//                intent.putExtra("pos", mPos);
//                intent.putExtra("try", 1);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

        mProgressView = findViewById(R.id.progress_view);
        mProgressView.setVisibility(View.INVISIBLE);
        mProgressView.setIndeterminate(true);
        mProgressView.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPink), android.graphics.PorterDuff.Mode.MULTIPLY);


    }

    private void queryProductDetail() {
        List<String> skuList = new ArrayList<>();
        skuList.add(mData.sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && skuDetailsList != null) {
                            for (Object skuDetails : skuDetailsList) {
                                mTxtBuy.setVisibility(View.VISIBLE);
                                SkuDetails detail = (SkuDetails) skuDetails;
                                String sku = detail.getSku();
                                String price = detail.getPrice();
                                String currencyCode = detail.getPriceCurrencyCode();
                                if (mData.sku.equals(sku)) {
                                    mTxtPrice.setText(currencyCode + " " + price);
                                }
                            }
                        } else {
                            showErrorDialog();
                        }
                    }

//                    @Override
//                    public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
//                        // Process the result.
//                        if (responseCode == BillingClient.BillingResponse.OK
//                                && skuDetailsList != null) {
//                            for (Object skuDetails : skuDetailsList) {
//                                mTxtBuy.setVisibility(View.VISIBLE);
//                                SkuDetails detail = (SkuDetails)skuDetails;
//                                String sku = detail.getSku();
//                                String price = detail.getPrice();
//                                String currencyCode = detail.getPriceCurrencyCode();
//                                if (mData.sku.equals(sku)) {
//                                    mTxtPrice.setText(currencyCode + " " + price);
//                                }
//                            }
//                        }else{
//                            showErrorDialog();
//                        }
//                    }
                });
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.txt_buy).setOnClickListener(this);
        findViewById(R.id.layout_try).setOnClickListener(this);
        mTxtBuy = findViewById(R.id.txt_buy);
        mImgCate = findViewById(R.id.img_cate);
        mTxtCate = findViewById(R.id.txt_cate);
        mTxtPrice = findViewById(R.id.txt_price);
        mImgStickers = findViewById(R.id.img_stickers);
        mTxtBuy.setVisibility(View.INVISIBLE);

        mTxtCate.setText(mData.name);
//        mTxtPrice.setText(String.format("$%.02f", mData.price));
        GlobalConst.showImageFromAsset(this, GlobalConst.STICKER_ASSET_PATH + mData.path + "/s_icon.png", mImgCate);
        GlobalConst.showImageFromAsset(this, GlobalConst.STICKER_ASSET_PATH + mData.path + "/s_iap_page.png", mImgStickers);
    }

    RewardedAd mRewardedVideoAd;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            onBackPressed();
        } else if (v.getId() == R.id.txt_buy) {
//            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                    .setSku(mData.sku)
//                    .setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
//                    .build();
//
//            int responseCode = mBillingClient.launchBillingFlow(this, flowParams);

            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            List<String> skuList = new ArrayList<>();
            skuList.add(mData.sku);
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                        .setSkuDetails(params.build().)
//                    .build();
//                int responseCode = mBillingClient.launchBillingFlow(this, params).getResponseCode();
            mBillingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0))
                                    .build();
                            int responseCode = mBillingClient.launchBillingFlow(PurchaseStickerActivity.this,
                                    billingFlowParams).getResponseCode();
                            Log.e("error", "response" + responseCode);
                        }
                    });
            /*Intent intent = new Intent();
            intent.putExtra("pos", mPos);
            setResult(RESULT_OK, intent);
            finish();*/


//            hadlePurchased();
        } else if (v.getId() == R.id.layout_try) {
            showRewardedVideo();
//            if (mRewardedVideoAd != null) {
////                AdRequest adRequest = new AdRequest.Builder().build();
////                mRewardedVideoAd.loadAd(adRequest, new RewardedAdLoadCallback());
////                mRewardedVideoAd.setFullScreenContentCallback(new FullScreenContentCallback() {
////                    @Override
////                    public void onAdShowedFullScreenContent() {
////                    }
////
////                    @Override
////                    public void onAdFailedToShowFullScreenContent(AdError adError) {
////                        // Called when ad fails to show.
////                    }
////
////                    @Override
////                    public void onAdDismissedFullScreenContent() {
////                        mRewardedVideoAd = null;
////                    }
////                });
////                mRewardedVideoAd.show(this, new OnUserEarnedRewardListener() {
////                    @Override
////                    public void onUserEarnedReward(@NonNull com.google.android.gms.ads.rewarded.RewardItem rewardItem) {
////                        int rewardAmount = rewardItem.getAmount();
////                        String rewardType = rewardItem.getType();
////                    }
////                });
//                mRewardedVideoAd.show(
//                        this,
//                        new OnUserEarnedRewardListener() {
//                            @Override
//                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                                Toast.makeText(
//                                        PurchaseStickerActivity.this,
//                                        "onRewarded! currency: "
//                                                + rewardItem.getType() + "    amount: "
//                                                + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//            mRewardedVideoAd.loadAd(getString(R.string.admob_reward_id),
//                    new AdRequest.Builder().build());
            findViewById(R.id.progress_view).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_try).setVisibility(View.INVISIBLE);

        }
    }

    private void showErrorDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.error)
                    .setMessage(R.string.billing_error)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    private void hadlePurchased() {
        Intent intent = new Intent();
        intent.putExtra("pos", mPos);
        intent.putExtra("try", 0);
        setResult(RESULT_OK, intent);
        finish();
    }

//    @Override
//    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
//        if (responseCode == BillingClient.BillingResponse.OK
//                && purchases != null) {
//            hadlePurchased();
//        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
//            // Handle an error caused by a user cancelling the purchase flow.
//            Log.e("error", "canceled");
////            Toast.makeText(this, "error code =" + responseCode, Toast.LENGTH_LONG).show();
//            showErrorDialog();
//        } else {
//            // Handle any other error codes.
//            Log.e("error", "other error=" + responseCode);
//            if (responseCode == 7){
//                hadlePurchased();
//            }else{
//                showErrorDialog();
//            }
//        }
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            hadlePurchased();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.e("error", "canceled");
//            Toast.makeText(this, "error code =" + responseCode, Toast.LENGTH_LONG).show();
            showErrorDialog();
        } else {
            // Handle any other error codes.
            Log.e("error", "other error=" + billingResult.getResponseCode());
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                hadlePurchased();
            } else {
                showErrorDialog();
            }
        }
    }

    private void loadRewardedAd() {
        if (mRewardedVideoAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    getString(R.string.admob_reward_id),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            mRewardedVideoAd = null;
                            isLoading = false;

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedVideoAd = rewardedAd;
                            isLoading = false;
                        }
                    });
        }
    }

    private void showRewardedVideo() {

        if (mRewardedVideoAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
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
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mRewardedVideoAd = null;
                        loadRewardedAd();
                    }
                });
        mRewardedVideoAd.show(
                this,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                    }
                });
    }

}

