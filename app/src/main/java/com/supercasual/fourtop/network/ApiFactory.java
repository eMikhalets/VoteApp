package com.supercasual.fourtop.network;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final String BASE_URL = "http://ntech.team:8082";
    private static ApiFactory apiFactory;
    private static Retrofit retrofit;

    private ApiFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiFactory getApiFactory() {
        if (apiFactory == null) {
            apiFactory = new ApiFactory();
        }
        return apiFactory;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
