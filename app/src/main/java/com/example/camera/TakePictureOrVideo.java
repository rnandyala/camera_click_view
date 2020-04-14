package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.io.File;

public class TakePictureOrVideo extends AppCompatActivity {


    int mImageTagID;
    Group mFirstVisit;
    Group mUpdateVisit;
    Group mVideoGroup;
    Group mPictureGroup;
    ImageButton mRecordVideo;
    ImageButton mTakePhoto;
    Button mCancel;
    Button mSave;
    File mFile;
    Bitmap mThumbImage;
    String fileName;


    ImageView mFarmVideo;


    Button mCancelUpdate;
    Button mDeleteUpdate;
    Button mUpdate;

    boolean isTakePhoto = false;
    boolean isFromImage = false;


    boolean isTakeVideo = false;
    boolean isFromVideo = false;

    public static final int REQUEST_CODE_CAMERA = 999;
    public static final int REQUEST_CODE_VIDEO = 888;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CAMERA == requestCode && resultCode == RESULT_OK) {

            fileName = data.getStringExtra("fileName");

            fileName.isEmpty();

            mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), fileName);
            final int THUMBSIZE = 400;

            mThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                    THUMBSIZE, THUMBSIZE);


            mTakePhoto.setImageBitmap(mThumbImage);

        }

        if (REQUEST_CODE_VIDEO == requestCode && resultCode == RESULT_OK) {
            fileName = data.getStringExtra("fileName");

            fileName.isEmpty();

            //  mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), fileName);

            //get the imageUri
            // final Uri tempImageUri = Uri.fromFile(mFile);

            //String mAuthority =    tempImageUri.getAuthority();

            //mAuthority.isEmpty();

            mThumbImage = ThumbnailUtils.createVideoThumbnail(fileName, MediaStore.Video.Thumbnails.MINI_KIND);
            /*

            final int THUMBSIZE = 400;

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                    THUMBSIZE, THUMBSIZE);*/

            mRecordVideo.setImageBitmap(mThumbImage);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture_or_video);


        mFarmVideo = findViewById(R.id.farmerDetailsvideo1);
        mVideoGroup = findViewById(R.id.group_video);
        mPictureGroup = findViewById(R.id.group_picture);
        mRecordVideo = findViewById(R.id.record_video);
        mTakePhoto = findViewById(R.id.open_camera);
        mSave = findViewById(R.id.save);
        mCancel = findViewById(R.id.cancel_button);
        mFirstVisit = findViewById(R.id.group_first_visit_button);
        mUpdateVisit = findViewById(R.id.group_update_button);


        mCancelUpdate = findViewById(R.id.cancel);
        mUpdate = findViewById(R.id.update_picture);
        mDeleteUpdate = findViewById(R.id.delete_picture);

/*
        mFirstVisit.setVisibility(View.GONE);
        mUpdateVisit.setVisibility(View.VISIBLE);

 */

        initBundle();


        mCancelUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();

/*
                        if (mThumbImage != null) {
                            Intent mThumbImageIntent = new Intent();
                            mThumbImageIntent.putExtra("fileName", fileName);
                            setResult(RESULT_OK, mThumbImageIntent);
                            TakePictureOrVideo.this.finish();
                        } else {
                            Toast.makeText(TakePictureOrVideo.this, "currently thumbnail is unavailable", Toast.LENGTH_LONG).show();
                        }


 */

                    }
                }
        );

        mUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (isTakeVideo && isFromVideo) {
                            Intent mUpdateVideo = new Intent();
                            mUpdateVideo.putExtra("mVideoFileName", fileName);
                            setResult(RESULT_OK, mUpdateVideo);
                            finish();
                        } else if (isTakePhoto && isFromImage) {

                            Intent mUpdateImage = new Intent();
                            mUpdateImage.putExtra("imageID", mImageTagID);
                            mUpdateImage.putExtra("fileName", fileName);
                            //updatescreen
                            mUpdateImage.putExtra("updatescreen", true);

                            setResult(RESULT_OK, mUpdateImage);
                            finish();

                            // mImageTagID
                            // sendFileName or fileName
                            //fileName

                        } else {
                            Toast.makeText(TakePictureOrVideo.this, "take another video:", Toast.LENGTH_LONG).show();
                        }

                   /*
                        if (mThumbImage != null) {
                            Toast.makeText(TakePictureOrVideo.this, "no  updates", Toast.LENGTH_LONG).show();
                        }*/


                    }
                }

        );

        mDeleteUpdate.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // visibiltiy to gone
                        //   setResult();
                        //mTakePhoto

                        Integer mPicutreTag = (Integer) mImageTagID;

                        if (mPicutreTag != null) {
                            Intent mImageIntent = new Intent();
                            mImageIntent.putExtra("image_file_name", fileName);
                            mImageIntent.putExtra("imageToDelete", mPicutreTag);
                            setResult(RESULT_OK, mImageIntent);
                            finish();
                        }


                    }
                }
        );


        mTakePhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open CameraActivity
                        isTakePhoto = true;
                        Intent mTakePicture = new Intent(TakePictureOrVideo.this, CameraActivity.class);

                        mTakePicture.putExtra("picture", true);
                        startActivityForResult(mTakePicture, REQUEST_CODE_CAMERA);
                    }
                }

        );


        mRecordVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isTakeVideo = true;
                        Intent mTakeVideo = new Intent(TakePictureOrVideo.this, CameraActivity.class);

                        mTakeVideo.putExtra("video", true);

                        startActivityForResult(mTakeVideo, REQUEST_CODE_VIDEO);
                    }
                });

        mCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                        // delete the image from the storage
                        // Unable to delete the files in package folder

                        if (mFile != null) {
                            boolean isFilePresent = mFile.delete();
                            if (!isFilePresent) {
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


                        if (mThumbImage != null) {
                            Intent mThumbImageIntent = new Intent();
                            mThumbImageIntent.putExtra("fileName", fileName);
                            setResult(RESULT_OK, mThumbImageIntent);
                            TakePictureOrVideo.this.finish();
                        } else {
                            Toast.makeText(TakePictureOrVideo.this, "currently thumbnail is unavailable", Toast.LENGTH_LONG).show();
                        }




                      /*
                      takes request code and an intent
                        setResult();
                       */

                    }
                }
        );

    }


    // file name for video need to be sent from FormAction
    private void initBundle() {


        int camera = getIntent().getIntExtra("camera", 0);
        int video = getIntent().getIntExtra("video", 0);

        // get file name and where it is coming from
        String mFileName = getIntent().getStringExtra("mFileNameForPicture");
        isFromImage = getIntent().getBooleanExtra("comingfrom_image", false);


        mImageTagID = getIntent().getIntExtra("deleteImagePosition", 1);


        // get file name and load the thumbnail

        String mVideoFileName = getIntent().getStringExtra(FormAction.VIDEO);
        boolean isFromVideo = getIntent().getBooleanExtra("comingFromVideo", false);


        setVideoData(isFromVideo, mVideoFileName);


        if (camera == 1) {
            mVideoGroup.setVisibility(View.GONE);
            mPictureGroup.setVisibility(View.VISIBLE);

            mFirstVisit.setVisibility(View.VISIBLE);
            mUpdateVisit.setVisibility(View.GONE);
        }


        if (video == 2) {
            mPictureGroup.setVisibility(View.GONE);
            mVideoGroup.setVisibility(View.VISIBLE);
            mFirstVisit.setVisibility(View.VISIBLE);
            mUpdateVisit.setVisibility(View.GONE);
        }



        if (mFileName != null) {
            mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), mFileName);
            final int THUMBSIZE = 400;

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    mThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                            THUMBSIZE, THUMBSIZE);

                    setmThumbImage(mThumbImage);
                }
            }.start();

            mFirstVisit.setVisibility(View.GONE);
            mUpdateVisit.setVisibility(View.VISIBLE);

        }
    }

    private void setVideoData(boolean isFromVideo, String mVideoFileName) {

        if (isFromVideo && mVideoFileName != null) {

            //   mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), mVideoFileName);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);
            setmThumbVideo(bitmap);


        }


        mPictureGroup.setVisibility(View.GONE);
        mVideoGroup.setVisibility(View.VISIBLE);

        mFirstVisit.setVisibility(View.GONE);
        mUpdateVisit.setVisibility(View.VISIBLE);
    }





    private void setmThumbVideo(Bitmap mThumbImage) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mRecordVideo.setImageBitmap(mThumbImage);
                    }
                }

        );
    }


    public void setmThumbImage(Bitmap mThumbImage) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mTakePhoto.setImageBitmap(mThumbImage);
                    }
                }

        );

    }


}

