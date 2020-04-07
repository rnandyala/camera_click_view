package com.example.camera;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

// using intent service for retrieving the image from the storage and post it to UI using broadcastreceiver
public class ReadImage extends IntentService {
    public ReadImage() {
        super("imageRetriever");
    }




    /*
   check whether the external storage is present or not

   if it is mounted get the path of the application directory

   convert the path to uri

    by passing uri and imageDecoder get the required bitmap object

    If i want pass this bitmap using intent then compress the bitmap convert bitmap to bytearray

    then pass bytearray using intent


     */














    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //    Environment.getExternalStorageDirectory();
        // returns path of application directory when the external drive is mounted
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // getExternalFilesDir gives path of application directory
            File mFile = new File(getApplicationContext().getExternalFilesDir(null), "temp_image.jpg");
           //get the imageUri
            final Uri tempImageUri = Uri.fromFile(mFile);

            Bitmap bitmap = null;
            try {
                // ExifInterface used for rotation and saving picture
                ExifInterface exif = new ExifInterface(tempImageUri.getPath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // convert image to bitmap
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getApplicationContext().getContentResolver(), tempImageUri));
                    //bitmap.getByteCount();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // compress image before passing back to activity
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                   // convert bitmap to byteArray
                    byte[] byteArray = stream.toByteArray();

                    Intent in1 = new Intent();
                    in1.putExtra("image", byteArray);
                    in1.setAction("Custom_Intent");
                  // set action here and listen to this action when registering receiver
                    sendBroadcast(in1);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), tempImageUri);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte [] byteArray = stream.toByteArray();
                    Intent in1 = new Intent();
                    in1.putExtra("image", byteArray);
                    in1.setAction("Custom_Intent");
                    // set action here and listen to this action when registering receiver
                    sendBroadcast(in1);

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
