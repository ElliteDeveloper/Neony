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
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.ptnst.neon.neon.model.FontData;
import com.ptnst.neon.neon.utils.GlobalConst;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PurchaseFontActivity extends BaseActivity implements View.OnClickListener, PurchasesUpdatedListener {


    ImageView mImgCate;
    TextView mTxtPrice;
    ImageView mImgFontPreview;
    TextView mTxtBuy;

    int mCateNo = 0;
    int mFontNo = 0;
    FontData mData;

    private BillingClient mBillingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_font);


        mCateNo = getIntent().getIntExtra("cateNo", 0);
        mFontNo = getIntent().getIntExtra("fontNo", 0);
        mData = (FontData) getIntent().getSerializableExtra("data");
        initView();

        mBillingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this).build();
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

    }

    private void queryProductDetail() {
        List skuList = new ArrayList<>();
        skuList.add(mData.sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//        mBillingClient.querySkuDetailsAsync(params.build(),
//                new SkuDetailsResponseListener() {
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
//                });
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
        mTxtBuy = findViewById(R.id.txt_buy);
        mImgCate = findViewById(R.id.img_cate);
        mTxtPrice = findViewById(R.id.txt_price);
        mImgFontPreview = findViewById(R.id.img_stickers);
        mTxtBuy.setVisibility(View.INVISIBLE);

        GlobalConst.showImageFromAsset(this, GlobalConst.FONT_ASSET_PATH + mData.lang + "/" + mData.path + "/" + mData.icon, mImgCate);
        GlobalConst.showImageFromAsset(this, GlobalConst.FONT_ASSET_PATH + mData.lang + "/" + mData.path + "/" + mData.preview, mImgFontPreview);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            onBackPressed();
        } else if (v.getId() == R.id.txt_buy) {
//            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                    .setSku(mData.sku)
//                    .setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
//                    .build();
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
                            int responseCode = mBillingClient.launchBillingFlow(PurchaseFontActivity.this,
                                    billingFlowParams).getResponseCode();
                        }
                    });
            /*Intent intent = new Intent();
            intent.putExtra("pos", mPos);
            setResult(RESULT_OK, intent);
            finish();*/

//            hadlePurchased();
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
        intent.putExtra("cateNo", mCateNo);
        intent.putExtra("fontNo", mFontNo);
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
//            showErrorDialog();
//        } else {
//            // Handle any other error codes.
//            Log.e("error", "other error");
//            if (responseCode == 7){
//                hadlePurchased();
//            }else{
//                showErrorDialog();
//            }
//        }
//    }

//    @Override
//    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
//        if (responseCode == BillingClient.BillingResponseCode.OK
//                && purchases != null) {
//            hadlePurchased();
//        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//            // Handle an error caused by a user cancelling the purchase flow.
//            Log.e("error", "canceled");
//            showErrorDialog();
//        } else {
//            // Handle any other error codes.
//            Log.e("error", "other error");
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
            showErrorDialog();
        } else {
            // Handle any other error codes.
            Log.e("error", "other error");
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                hadlePurchased();
            } else {
                showErrorDialog();
            }
        }
    }
}
