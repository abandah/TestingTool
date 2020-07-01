package com.error.handler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoEdit extends AppCompatActivity {

    ImageView ivDrawImg;
    Button save, cancel;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        file = (File) getIntent().getExtras().get("picture");
        ivDrawImg = findViewById(R.id.iv_draw);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        Picasso.get().load(file).into(ivDrawImg);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImg();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
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
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", file);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            // FileOutputStream stream = new FileOutputStream(file);
            //  image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // Toast.makeText(this, "Picture saved", Toast.LENGTH_SHORT).show();

            // Android equipment Gallery application will only at boot time scanning system folder
            // The simulation of a media loading broadcast, for the preservation of images can be viewed in Gallery

            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);*/

        } catch (Exception e) {
            //  Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
