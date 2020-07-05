package com.feedback.handler;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Abandah on 7/1/2020.
 */
public class App extends Application {

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

    private void takeScreenshot() {
        if (getActiveActivity() == null) {
            return;
        }
        if(!isStoragePermissionGranted()){
            return;
        }
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
         //   outputStream.flush();
          //  outputStream.close();
            Fragment f = getVisibleFragment(activeActivity);
            String g = "";
            if (f != null) {
                g = f.toString();
                String s[] = g.split("\\{");
                g = s[0];
            }
            openScreenshot(imageFile, activeActivity.getLocalClassName(), g);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public Activity getActiveActivity() {
        if (activeActivity.getLocalClassName().equalsIgnoreCase(ViewScreenShot_Activity.class.getName())) {
            return null;
        }
        if (activeActivity.getLocalClassName().equalsIgnoreCase(UCEDefaultActivity.class.getName())) {
            return null;
        }
        if (activeActivity.getLocalClassName().equalsIgnoreCase(PhotoEdit.class.getName())) {
            return null;
        }
        return activeActivity;
    }

    public Fragment getVisibleFragment(Activity activity) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible())
                        return fragment;
                }
            }
        }
        return null;
    }

    private void openScreenshot(File imageFile, String localClassName, String g) {
        //   Intent intent = new Intent();
        //   intent.setAction(Intent.ACTION_VIEW);
        // Uri uri = Uri.fromFile(imageFile);
        // intent.setDataAndType(uri, "image/*");
        //  startActivity(intent);
        Intent intent = new Intent(this, ViewScreenShot_Activity.class);
        intent.putExtra("picture", imageFile);
        intent.putExtra("localClassName", localClassName);
        intent.putExtra("localFragmentClassName", g);
        activeActivity.startActivityForResult(intent,486);
    }

    public boolean isStoragePermissionGranted() {
        if (activeActivity == null)
            return false;
        if (ContextCompat.checkSelfPermission(activeActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activeActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        if (ContextCompat.checkSelfPermission(activeActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activeActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

}