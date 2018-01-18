package com.sa45team7.lussis.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nhatton on 1/17/18.
 */

public class LUSSISClient {

    private static final String ROOT_URL = "http://10.211.55.5/LUSSIS/api/";

    private static Retrofit getRetrofitInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static LUSSISService getApiService() {
        return getRetrofitInstance().create(LUSSISService.class);
    }
}
