package com.error.handler;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class ViewScreenShot_Activity extends AppCompatActivity {

    ImageView ivDrawImg;
    ScrollViewCustome scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    DrawingView mDrawingView=new DrawingView(this);

        setContentView(R.layout.activity_view_screen_shot);
        //   DrawingView  mDrawingView =findViewById(R.id.pic);
        File file = (File) getIntent().getExtras().get("picture");
        /*if (file.exists()) {
            String fp = file.getAbsolutePath();
            Drawable d = Drawable.createFromPath(file.getAbsolutePath());
            mDrawingView.setBackground(d);
        } else {
            System.out.println("File Not Found");
        }*/


        ivDrawImg = findViewById(R.id.iv_draw);
        scrollView = findViewById(R.id.scrollView);
        Picasso.get().load(file).into(ivDrawImg);
        Button btnSave = (Button) findViewById(R.id.btn_save);
        Button btnResume = (Button) findViewById(R.id.btn_resume);


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                saveImg();

            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


    }


    private void saveImg() {

        Bitmap image = Bitmap.createBitmap(ivDrawImg.getWidth(), ivDrawImg.getHeight(), Bitmap.Config.RGB_565);
        ivDrawImg.draw(new Canvas(image));

        String uri = MediaStore.Images.Media.insertImage(getContentResolver(), image, "title", null);

        Log.e("uri", uri);


        try {
            // Save the image to the SD card.

            File folder = new File(Environment.getExternalStorageDirectory() + "/DrawTextOnImg");

            if (!folder.exists()) {
                folder.mkdir();
                //folder.mkdirs();  //For creating multiple directories
            }

            File file = new File(Environment.getExternalStorageDirectory() + "/DrawTextOnImg/tempImg.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Toast.makeText(this, "Picture saved", Toast.LENGTH_SHORT).show();

            // Android equipment Gallery application will only at boot time scanning system folder
            // The simulation of a media loading broadcast, for the preservation of images can be viewed in Gallery

            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);*/

        } catch (Exception e) {
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount() == 1){
            ivDrawImg.onTouchEvent(event);
            return super.onTouchEvent(event);
        }else if (event.getPointerCount() == 2) {
            scrollView.onTouchEvent(event);
            return super.onTouchEvent(event);
        }
        return false;
        //return super.onTouchEvent(event);
    }
}

