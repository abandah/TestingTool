package com.feedback.test;

import android.os.Bundle;
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


    }
}
