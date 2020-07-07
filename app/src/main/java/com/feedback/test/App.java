package com.feedback.test;

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
    protected String ErrorLink() {
        return "";
    }


}