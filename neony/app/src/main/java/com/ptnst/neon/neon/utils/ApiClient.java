package com.ptnst.neon.neon.utils;


import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by daydreamer on 4/12/2015.
 */
public class ApiClient {



    public static ApiInterface getApiClient(String apiPath) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiPath)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface github = retrofit.create(ApiInterface.class);
        return github;
    }


    public static ApiInterface getDownloadApiClient(DownloadProgressInterceptor.DownloadProgressListener listener) {
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drive.google.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface github = retrofit.create(ApiInterface.class);
        return github;
    }


    public interface ApiInterface {

        //------------Download----------------------
        @GET
        Call<ResponseBody> downloadFont(@Url String url);

        //------------Download----------------------
        @GET
        Call<ResponseBody> downloadImageFromUnPlash(@Url String url);


        //---------------------get download url for unsplash-----------
        @GET
        Call<ResponseBody> getUnsplashDownloadUrl(@Url String url, @Query("client_id") String client_id);

        //--------search with photos-------------
        @GET("/search/photos")
        Call<ResponseBody> searchPhotosWithText(@Query("client_id") String client_id, @Query("query") String query, @Query("page") int page, @Query("per_page") int per_page);

        //--------search with photos-------------
        @GET("/photos")
        Call<ResponseBody> searchPhotosWithOrderBy(@Query("client_id") String client_id, @Query("order_by") String order_by, @Query("page") int page, @Query("per_page") int per_page);

        //--------translate naver-------------
        @Headers({"X-Naver-Client-Id: kGzWHVt_2Bi7hS3hl2Kq", "X-Naver-Client-Secret: Vp0u55hEOU", "Content-Type: application/x-www-form-urlencoded"})
        @FormUrlEncoded
        @POST("/v1/papago/n2mt")
        Call<ResponseBody> translateNaver(@Field("source") String source, @Field("target") String target, @Field("text") String keyword);

    }
}

