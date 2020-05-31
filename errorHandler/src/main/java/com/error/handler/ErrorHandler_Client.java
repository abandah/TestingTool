package com.error.handler;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ErrorHandler_Client {

    @FormUrlEncoded
    @POST("SendError")
    Call<JsonElement> SendError(@Field("Error_Product") String Error_Product,
                                @Field("Error_Customer") String Error_Customer,
                                @Field("Error_Page") String Error_Page,
                                @Field("Error_Message") String Error_Message,
                                @Field("Error_Details") String Error_Details,
                                @Field("Error_note") String Error_note);
}
