package com.example.camera;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TODO {
    private File mVideoFolder;
    private String mVideoFileName;

    /*
   ContentResolver resolver = context.getContentResolver();
   ContentValues contentValues = new ContentValues();
contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

   Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
   As per the suggestion of @SamChen the code should look like this for text files:

   Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);*/
    private void createVideoFolder() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            /*
            ContentResolver resolver = CameraActivity.this.getContentResolver();

            ContentValues mContentValues = new ContentValues();

            mContentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "video");
            mContentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            mContentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);

            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mContentValues);

             */


            //   File movieFile = getExternalMediaDirs();


        } else {
            File movieFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

            mVideoFolder = new File(movieFile, "camera2video");

            if (!mVideoFolder.exists()) {
                mVideoFolder.mkdirs();

            }

        }

    }

    private File createVideoFileName() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prepend = "VIDEO_" + timestamp + "_";

        File videoFile = File.createTempFile(prepend, ".mp4", mVideoFolder);

        mVideoFileName = videoFile.getAbsolutePath();

        return videoFile;
    }

}
