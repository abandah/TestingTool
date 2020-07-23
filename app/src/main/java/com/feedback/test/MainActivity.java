package com.feedback.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_close_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i [] = new int[2];
                int y = i[3];
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                start();
            }
        },5000);//time in milisecond

    }

    private void start() {
        Intent i = new Intent(this,MainActivity2.class);
        startActivity(i);
    }
}
