package com.example.camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class TakePictureOrVideo extends AppCompatActivity {

    Group mVideoGroup;
    Group mPictureGroup;
    ImageButton mRecordVideo;
    ImageButton mTakePhoto;
    Button mCancel;
    Button mSave;
    File mFile;
    public static final int REQUEST_CODE_CAMERA = 999;

    public static final int REQUEST_CODE_VIDEO = 888;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CAMERA == requestCode && resultCode == RESULT_OK) {

            String fileName = data.getStringExtra("fileName");

            fileName.isEmpty();

            mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), fileName);

            //get the imageUri
            // final Uri tempImageUri = Uri.fromFile(mFile);

            //String mAuthority =    tempImageUri.getAuthority();

            //mAuthority.isEmpty();

            final int THUMBSIZE = 400;

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                    THUMBSIZE, THUMBSIZE);

            mTakePhoto.setImageBitmap(ThumbImage);

        }

        if(REQUEST_CODE_VIDEO  == requestCode && resultCode == RESULT_OK){
            String fileName = data.getStringExtra("fileName");

            fileName.isEmpty();

            mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), fileName);

            //get the imageUri
            // final Uri tempImageUri = Uri.fromFile(mFile);

            //String mAuthority =    tempImageUri.getAuthority();

            //mAuthority.isEmpty();

            final int THUMBSIZE = 400;

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                    THUMBSIZE, THUMBSIZE);

            mTakePhoto.setImageBitmap(ThumbImage);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture_or_video);
        mVideoGroup = findViewById(R.id.group_video);
        mPictureGroup = findViewById(R.id.group_picture);
        mRecordVideo = findViewById(R.id.record_video);
        mTakePhoto = findViewById(R.id.open_camera);
        mSave = findViewById(R.id.save);
        mCancel = findViewById(R.id.cancel_button);

        initBundle();

        mTakePhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open CameraActivity

                        Intent mTakePicture = new Intent(TakePictureOrVideo.this, CameraActivity.class);

                        startActivityForResult(mTakePicture, REQUEST_CODE_CAMERA);
                    }
                }

        );


        mRecordVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mTakePicture = new Intent(TakePictureOrVideo.this, CameraActivity.class);

                        startActivityForResult(mTakePicture, REQUEST_CODE_VIDEO);
                    }
                });

        mCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // delete the image from the storage


                        // Unable to delete the files in package folder

                        if (mFile != null) {
                            boolean isFilePresent = mFile.delete();
                            if (isFilePresent) {
                                Toast.makeText(TakePictureOrVideo.this, "unable_to_delete_the_file", Toast.LENGTH_LONG);
                            }
                        }
                    }
                }
        );

        mSave.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pass bitmap or thumbnail back to formaction


                      /*
                      takes request code and an intent
                        setResult();
                       */

                    }
                }
        );

    }

    private void initBundle() {
        int camera = getIntent().getIntExtra("camera", 0);
        int video = getIntent().getIntExtra("video", 0);

        if (camera == 1) {
            mVideoGroup.setVisibility(View.GONE);
            mPictureGroup.setVisibility(View.VISIBLE);
        }
        if (video == 1) {
            mPictureGroup.setVisibility(View.GONE);
            mVideoGroup.setVisibility(View.VISIBLE);
        }
    }


}

