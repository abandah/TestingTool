package com.error.test;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.error.handler.TestToolHandler;
import com.error.handler.UCEHandler;
import com.error.handler.ViewScreenShot_Activity;
import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Abandah on 7/1/2020.
 */
public class app extends Application {
    private Activity activeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        new UCEHandler.Builder(this)
                .setTrackActivitiesEnabled(false)
                .setLink("WebService Link here")
                .build();
        setupActivityListener();

        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {
                takeScreenshot();

            }
        });
     //   new TestToolHandler.Builder(this).build();
               // .setTrackActivitiesEnabled(false)
               // .setLink("WebService Link here")
               // .build();
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }
            @Override
            public void onActivityPaused(Activity activity) {
                activeActivity = null;
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
    public Activity getActiveActivity(){
        return activeActivity;
    }


        private void takeScreenshot() {
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                // image naming and path  to include sd card  appending name you choose for file
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

                // create bitmap screen capture

                View v1 = getActiveActivity().getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();

                openScreenshot(imageFile);
            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }
        }

    private void openScreenshot(File imageFile) {
        //   Intent intent = new Intent();
        //   intent.setAction(Intent.ACTION_VIEW);
        // Uri uri = Uri.fromFile(imageFile);
        // intent.setDataAndType(uri, "image/*");
        //  startActivity(intent);
        Intent intent = new Intent(this, ViewScreenShot_Activity.class);
        intent.putExtra("picture",imageFile);
        startActivity(intent);
    }

}