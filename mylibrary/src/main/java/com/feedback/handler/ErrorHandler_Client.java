package com.feedback.handler;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ErrorHandler_Client {

    @FormUrlEncoded
    @POST("SendFeedback")
    Call<JsonElement> SendError(@Field("Error_Product") String Error_Product,
                                @Field("Error_Customer") String Error_Customer,
                                @Field("Error_Page") String Error_Page,
                                @Field("Error_Message") String Error_Message,
                                @Field("Error_Details") String Error_Details,
                                @Field("Error_note") String Error_note);


    @Multipart
    @POST("SendFeedback")
    Call<JsonElement> SendFeedback(@Part MultipartBody.Part file,
                                   @Part("localClassName") String localClassName,
                                   @Part("localFragmentClassName") String localFragmentClassName,
                                   @Part("notetext") String notetext,
                                   @Part("error_Product") String error_Product,
                                   @Part("error_Customer") String error_Customer,
                                   @Part("userId") String userId);
}
