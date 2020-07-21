package com.feedback.test;

import android.content.Context;

public class App extends com.feedback.handler.App {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getFeedbackLink() {
        return "http://192.168.0.48/MOB_OfferAt/websrv/mobileapp.asmx/";
    }

    @Override
    protected boolean EnableErrorHandler() {
        return false;
    }

    @Override
    protected boolean EnableFeedBack() {
        return true;
    }

    @Override
    protected Context context() {
        return this;
    }


    @Override
    protected String getUserId() {
        return "0";
    }


}