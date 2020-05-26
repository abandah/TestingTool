package com.error.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstanceWithLink {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(String BASE_URL) {
        if (retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();


        }
        return retrofit;
    }
}
