package com.feedback.test;

public class App extends com.feedback.handler.App {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getFeedbackLink() {
        return "";
    }

    @Override
    protected String ErrorLink() {
        return "";
    }


}