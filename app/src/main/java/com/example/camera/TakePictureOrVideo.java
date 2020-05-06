package com.example.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import java.io.File;

public class TakePictureOrVideo extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


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
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
try {
    outPersistentState.putString("fromVideothubnail", String.valueOf(isFromVideo));
}
catch (Exception ex){
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    ex.getMessage();
                    dialog.dismiss();
                }
            }
    );
    AlertDialog alert = mBuilder.create();
    alert.show();
}

}


    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        try {
            String videoState = persistentState.getString("fromVideothubnail", "");

            isFromVideo = Boolean.getBoolean(videoState);
        }
        catch (Exception ex){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ex.getMessage();
                            dialog.dismiss();
                        }
                    }
            );
            AlertDialog alert = mBuilder.create();
            alert.show();
        }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (REQUEST_CODE_CAMERA == requestCode && resultCode == RESULT_OK) {

                fileName = data.getStringExtra("fileName");

                fileName.isEmpty();

                mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), fileName);
                final int THUMBSIZE = 400;

                mThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()),
                        THUMBSIZE, THUMBSIZE);


                mTakePhoto.setImageBitmap(mThumbImage);

            }
        }
        catch(Exception ex){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ex.getMessage();
                            dialog.dismiss();
                        }
                    }
            );
            AlertDialog alert = mBuilder.create();
            alert.show();
        }


        try {
            if (REQUEST_CODE_VIDEO == requestCode && resultCode == RESULT_OK) {
                fileName = data.getStringExtra("fileName");

                fileName.isEmpty();

                isFromVideo = Boolean.parseBoolean(data.getStringExtra("fromVideothubnail"));


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
        catch (Exception ex){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ex.getMessage();
                            dialog.dismiss();
                        }
                    }
            );
            AlertDialog alert = mBuilder.create();
            alert.show();
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


                       try {
                           finish();
                       }
                       catch(Exception ex){
                           AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                           mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {


                                           ex.getMessage();
                                           dialog.dismiss();
                                       }
                                   }
                           );
                           AlertDialog alert = mBuilder.create();
                           alert.show();
                       }

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
try{
    if (isTakeVideo && isFromVideo) {
        Intent mUpdateVideo = new Intent();
        mUpdateVideo.putExtra("fileName", fileName);
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
}
catch(Exception ex){


    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
    );
    AlertDialog alert = mBuilder.create();
    alert.show();

}


                /*
                        if (mThumbImage != null) {
                            Intent mThumbImageIntent = new Intent();
                            mThumbImageIntent.putExtra("fileName", fileName);
                            setResult(RESULT_OK, mThumbImageIntent);
                            TakePictureOrVideo.this.finish();
                        } else {
                            Toast.makeText(TakePictureOrVideo.this, "currently thumbnail is unavailable", Toast.LENGTH_LONG).show();
                        }*/



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

                        try{
                            Integer mPicutreTag = (Integer) mImageTagID;

                            if (mPicutreTag != null) {
                                Intent mImageIntent = new Intent();
                                mImageIntent.putExtra("image_file_name", fileName);
                                mImageIntent.putExtra("imageToDelete", mPicutreTag);
                                setResult(RESULT_OK, mImageIntent);
                                finish();
                            }

                        }
                        catch(Exception ex){

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }
                            );
                            AlertDialog alert = mBuilder.create();
                            alert.show();
                        }




                    }
                }
        );


        mTakePhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            PopupMenu mCameraMenu = new PopupMenu(TakePictureOrVideo.this, v);

                            mCameraMenu.setOnMenuItemClickListener(TakePictureOrVideo.this);

                            mCameraMenu.inflate(R.menu.popup_menu);

                            mCameraMenu.show();
                        } catch (Exception ex) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    }
                            );
                            AlertDialog alert = mBuilder.create();
                            alert.show();
                        }


                    }
                }

        );


        mRecordVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try{

                            PopupMenu mVideoMenu = new PopupMenu(TakePictureOrVideo.this, v);
                            mVideoMenu.setOnMenuItemClickListener(TakePictureOrVideo.this);
                            mVideoMenu.inflate(R.menu.video_popup_menu);
                            mVideoMenu.show();
                        }
                        catch(Exception ex){

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {



                                        }
                                    }
                            );
                            AlertDialog alert = mBuilder.create();
                            alert.show();
                        }


//


                    }
                });

        mCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            if (mFile != null) {
                                boolean isFilePresent = mFile.delete();
                                if (!isFilePresent) {
                                    Toast.makeText(TakePictureOrVideo.this, "unable_to_delete_the_file", Toast.LENGTH_LONG).show();
                                }
                            }

                            finish();
                        }
                        catch (Exception ex){
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }
                            );
                            AlertDialog alert = mBuilder.create();
                            alert.show();
                        }



                        // delete the image from the storage
                        // Unable to delete the files in package folder


                    }
                }
        );

        mSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {






                        try{
                            if (mThumbImage != null) {


                                Intent mThumbImageIntent = new Intent();
                                mThumbImageIntent.putExtra("fileName", fileName);
                                setResult(RESULT_OK, mThumbImageIntent);
                                TakePictureOrVideo.this.finish();
                            } else {
                                Toast.makeText(TakePictureOrVideo.this, "currently thumbnail is unavailable", Toast.LENGTH_LONG).show();
                            }
                        }

                        catch(Exception ex){

                            // pass bitmap or thumbnail back to formaction
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                            mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }
                            );
                            AlertDialog alert = mBuilder.create();
                            alert.show();

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
        // isFromVideo =  Boolean.parseBoolean(getIntent().getStringExtra("fromVideothubnail"));

        //  String mFromCameraPreview = getIntent().getStringExtra("fromVideothubnail");


try {
    // commented on may 4th for testing
    int camera = getIntent().getIntExtra("camera", 0);
    int video = getIntent().getIntExtra("video", 0);


    // get file name and where it is coming from
    String mFileName = getIntent().getStringExtra("mFileNameForPicture");
    isFromImage = getIntent().getBooleanExtra("comingfrom_image", false);


    mImageTagID = getIntent().getIntExtra("deleteImagePosition", 1);


    // get file name and load the thumbnail

    String mVideoFileName = getIntent().getStringExtra(FormAction.VIDEO);
    isFromVideo = getIntent().getBooleanExtra("comingFromVideo", false);


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
catch(Exception ex){
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
    );
    AlertDialog alert = mBuilder.create();
    alert.show();
}
        /*
        if(mFromCameraPreview != null){
          isFromVideo =   Boolean.getBoolean(mFromCameraPreview);
        }*/

        //"fromVideothubnail"



    }

    private void setVideoData(boolean isFromVideo, String mVideoFileName) {
try{
        if (isFromVideo && mVideoFileName != null) {

            //   mFile = new File(TakePictureOrVideo.this.getExternalFilesDir(null), mVideoFileName);
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);
            setmThumbVideo(bitmap);


        }}
catch(Exception ex){
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
    );
    AlertDialog alert = mBuilder.create();
    alert.show();

}


        mPictureGroup.setVisibility(View.GONE);
        mVideoGroup.setVisibility(View.VISIBLE);

        mFirstVisit.setVisibility(View.GONE);
        mUpdateVisit.setVisibility(View.VISIBLE);
    }


    private void setmThumbVideo(Bitmap mThumbImage) {


      try {
          runOnUiThread(
                  new Runnable() {
                      @Override
                      public void run() {
                          mRecordVideo.setImageBitmap(mThumbImage);
                      }
                  }

          );
      }
      catch(Exception ex){
          AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
          mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  }
          );
          AlertDialog alert = mBuilder.create();
          alert.show();
      }
    }


    public void setmThumbImage(Bitmap mThumbImage) {
     try {

         runOnUiThread(
                 new Runnable() {
                     @Override
                     public void run() {
                         mTakePhoto.setImageBitmap(mThumbImage);
                     }
                 }

         );
     }
     catch(Exception ex){
         AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
         mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 }
         );
         AlertDialog alert = mBuilder.create();
         alert.show();
     }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
try {
    int id = item.getItemId();


    switch (item.getItemId()) {


        case R.id.concept_global_video:
            // case R.id.concept_global_video:
            try {
                Intent mTakeVideo = new Intent(TakePictureOrVideo.this, CameraActivity.class);
                //           mTakeVideo.putExtra("video", true);
                mTakeVideo.putExtra("fromVideothubnail", String.valueOf(isFromVideo));
                startActivityForResult(mTakeVideo, REQUEST_CODE_VIDEO);
                isTakeVideo = true;
            } catch (Exception ex) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
                mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }
                );
                AlertDialog alert = mBuilder.create();
                alert.show();
            }


            return true;


        case R.id.concept_global_camera:


            try {
                // Open CameraActivity
                isTakePhoto = true;
                Intent mTakePicture = new Intent(TakePictureOrVideo.this, CameraActivity.class);

                mTakePicture.putExtra("picture", true);
                startActivityForResult(mTakePicture, REQUEST_CODE_CAMERA);
            } catch (Exception ex) {
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(TakePictureOrVideo.this);
                mBuilder2.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }
                );
                AlertDialog alert2 = mBuilder2.create();
                alert2.show();
            }


            return true;
        case R.id.device_camera:
            Toast.makeText(this, "WIP", Toast.LENGTH_LONG).show();
            return true;

        case R.id.device_video:
            Toast.makeText(this, "WIP", Toast.LENGTH_LONG).show();
            return true;

        default:
            return false;
    }
}
catch(Exception ex){
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakePictureOrVideo.this);
    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
    );
    AlertDialog alert = mBuilder.create();
    alert.show();
}
return false;
    }
}

