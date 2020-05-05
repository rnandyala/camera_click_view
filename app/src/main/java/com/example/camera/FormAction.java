package com.example.camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;


// relook into list of filenames
public class FormAction extends AppCompatActivity implements View.OnClickListener {


    Button mJustCameraButton;

    String mVideoFileName;
    String mPictureFileName;

    int i = 0;


    boolean isCameraPicAvailable = true;
    ImageView mTakePicture;
    ImageButton mTakeVideo;

    ImageView[] mArrayOFImages;

    ImageView mFarmerDetailsImage1;
    ImageView mFarmerDetailsImage2;
    ImageView mFarmerDetailsImage3;
    ImageView mFarmerDetailsImage4;
    ImageView mFarmerDetailsImage5;
    ImageView mFarmerDetailsVideo1;

    Bitmap mBitMapImage;


    public static final String CAMERA = "camera";
    public static final String VIDEO = "video";
    public static final int CLICK_TYPE1 = 1;
    public static final int CLICK_TYPE2 = 2;
    private static final int DELETE_IMAGE = 989;
    private static final int UPDATE_IMAGE = 888;
    private static final int RequestThumbnailPicture = 005;
    private static final int RequestThumbnailVideo = 006;


    public boolean checkHardwareLevel() {
        CameraManager mCameramanager = (CameraManager) FormAction.this.getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String mCameraId : mCameramanager.getCameraIdList()) {
                CameraCharacteristics mCameraCharacteristics = mCameramanager.getCameraCharacteristics(mCameraId);
                Object mHardwareLevel = mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);

                return mHardwareLevel.toString().contains("2");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    //CameraCharacteristics mCharacterstics = mCameramanager.getCameraCharacteristics()

    // take user to video screen or photo screen or
    // default case is for all five photo imageview
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mJustCamera:


                try {

                    // If user tries to take more than 5 photos then do not allow the user
                    if (isCameraPicAvailable) {
                        if (checkHardwareLevel()) {
                            Toast.makeText(FormAction.this, "CAMERA2 is unavilable", Toast.LENGTH_LONG);
                        } else {


                            Intent i = new Intent(FormAction.this, TakePictureOrVideo.class);
                            // This is used to determine whether it is a camera or  video

                            // throw new RuntimeException("camera not working::");

                            i.putExtra(CAMERA, CLICK_TYPE1);
                            startActivityForResult(i, RequestThumbnailPicture);
                        }
                        ;
                    } else {
                        Toast.makeText(FormAction.this, "total count reached", Toast.LENGTH_SHORT);
                    }

                } catch (Exception ex) {

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


                break;


            case R.id.take_picture:

                break;

            case R.id.take_video:


                try{

                    if (mVideoFileName != null) {
                        Toast.makeText(this, "maximum one video", Toast.LENGTH_LONG).show();
                    } else {



                        Intent i = new Intent(FormAction.this, TakePictureOrVideo.class);

                        i.putExtra(VIDEO, CLICK_TYPE2);
                        startActivityForResult(i, RequestThumbnailVideo);
                        // TestAlertDialog mCommonMessage = new TestAlertDialog(this, "take video capture even triggered");


                    }
                }
                catch(Exception ex){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }
                    );
                    AlertDialog alert = mBuilder.create();
                    alert.show();
                }

                break;


            // onTap of video recording  I will be sending filename
            case R.id.farmerDetailsvideo1:

                try{

                    Intent mVideoIntent = new Intent(FormAction.this, TakePictureOrVideo.class);
                    mVideoIntent.putExtra(VIDEO, mVideoFileName);
                    mVideoIntent.putExtra("comingFromVideo", true);
                    startActivityForResult(mVideoIntent, RequestThumbnailVideo);
                }catch (Exception ex){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                    mBuilder.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            }
                    );
                    AlertDialog alert = mBuilder.create();
                    alert.show();
                }




                break;



            /*
            attributes are
            send filename
            click_type(ambiguity)
            whether it is comingfrom list of image
            image position to delete the file (this position is captured as part of tag)
             */
            default:
try{

    mPictureFileName = (String) v.getTag(R.string.tag_name);
    Intent mStartTakePictureFromFormAction = new Intent(FormAction.this, TakePictureOrVideo.class);
    mStartTakePictureFromFormAction.putExtra("mFileNameForPicture", mPictureFileName);
    mStartTakePictureFromFormAction.putExtra(CAMERA, CLICK_TYPE1);
    // did you tap on image then set is true
    mStartTakePictureFromFormAction.putExtra("comingfrom_image", true);
    // passing tag back and forth used to
    mStartTakePictureFromFormAction.putExtra("deleteImagePosition", (Integer) v.getTag(R.string.image));
    startActivityForResult(mStartTakePictureFromFormAction, DELETE_IMAGE);
    Toast.makeText(FormAction.this, "list of image:: ", Toast.LENGTH_SHORT).show();
}
catch(Exception ex){
    AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(this);
    mBuilder2.setMessage(ex.getMessage()).setTitle("PMC POC").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }
    );
    AlertDialog alert2 = mBuilder2.create();
    alert2.show();

}



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // when delete or update are tapped
        if (requestCode == DELETE_IMAGE && resultCode == RESULT_OK) {
            boolean isUpdate = data.getBooleanExtra("updatescreen", false);
            if (isUpdate) {
                String mFileName = data.getStringExtra("fileName");
                // adding newfile name
                // getting filename from image and deleting the filename from list
                for (int i = 0; i < mArrayOFImages.length; i++) {
                    if (mArrayOFImages[i].getTag(R.string.image) != null) {
                        if (mArrayOFImages[i].getTag(R.string.image).equals(data.getIntExtra("imageID", 0))) {
                            // adding new file name to the list
                            SingletonFileNameLibrary.getInstance().setmListOfFileNames(mFileName);
                            // getting the file name for the image that is being updated
                            String mPreviousFileName = (String) mArrayOFImages[i].getTag(R.string.tag_name);
                            // deleting the filename from the list
                            if (SingletonFileNameLibrary.getInstance().getListOfFileName().contains(mPreviousFileName)) {
                                SingletonFileNameLibrary.getInstance().getListOfFileName().remove(mPreviousFileName);
                            }
                        }
                    }
                }


                if (mFileName != null) {
                    ArrayList<String> mListOfFileNames = SingletonFileNameLibrary.getInstance().getListOfFileName();
                    mListOfFileNames.size();
                    // disable click for camera icon
                    if (mListOfFileNames.size() >= 5) {
                        isCameraPicAvailable = false;
                    }
                    for (int i = 0; i < mListOfFileNames.size(); i++) {

                        File mFile = new File(FormAction.this.getExternalFilesDir(null), mListOfFileNames.get(i));
                        final int THUMBSIZE = 400;

                        mBitMapImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()), THUMBSIZE, THUMBSIZE);

                        mArrayOFImages[i].setImageBitmap(mBitMapImage);


                        // adding file name twice as well as  position
                        mArrayOFImages[i].setTag(R.string.tag_name, mListOfFileNames.get(i));
                        mArrayOFImages[i].setTag(R.string.image, i);
                        //    mArrayOFImages[i].setTag(R.string.filename, mFileName);
                        mArrayOFImages[i].setOnClickListener(this);
                        mArrayOFImages[i].setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.v("FileName", "isMissing & current listOfFileNames size is" + SingletonFileNameLibrary.getInstance().getListOfFileName().size());
                }

            }


            // user taps on delete
            // get image position for that particular image from(TakePictureOrVideoActivity) and
            // delete the file name from the list of filenames
            // make camera available
            // make the imageview gone
            else {
                for (int i = 0; i < mArrayOFImages.length; i++) {
                    if (mArrayOFImages[i].getTag(R.string.image) != null) {
                        // getting tagID to delete or make view gone as well as remove file name from to make camera button available
                        if (mArrayOFImages[i].getTag(R.string.image).equals(data.getIntExtra("imageToDelete", 0))) {
                            if (SingletonFileNameLibrary.getInstance().getListOfFileName().contains(mPictureFileName)) {
                                SingletonFileNameLibrary.getInstance().getListOfFileName().remove(mPictureFileName);
                                if (SingletonFileNameLibrary.getInstance().getListOfFileName().size() < 5) {
                                    isCameraPicAvailable = true;
                                }
                            }
                            mArrayOFImages[i].setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

// I am getting the fileName from TakePictureOrVideo class
        // adding filename to the list
        //  if list is equal to five or greater tap on camera is disabled

/*
important imageview tags are:
file name, image position,
 */
        if (requestCode == RequestThumbnailPicture && resultCode == RESULT_OK) {
            // getting newFileName & isUpdate from onClick of Update
            String mFileName = data.getStringExtra("fileName");
            if (mFileName != null) {
                SingletonFileNameLibrary.getInstance().setmListOfFileNames(mFileName);
                ArrayList<String> mListOfFileNames = SingletonFileNameLibrary.getInstance().getListOfFileName();
                mListOfFileNames.size();
                if (mListOfFileNames.size() >= 5) {
                    isCameraPicAvailable = false;
                }
                for (int i = 0; i < mListOfFileNames.size(); i++) {
                    File mFile = new File(FormAction.this.getExternalFilesDir(null), mListOfFileNames.get(i));
                    final int THUMBSIZE = 400;
                    mBitMapImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFile.toString()), THUMBSIZE, THUMBSIZE);
                    mArrayOFImages[i].setImageBitmap(mBitMapImage);
                    mArrayOFImages[i].setTag(R.string.tag_name, mListOfFileNames.get(i));
                    mArrayOFImages[i].setTag(R.string.image, i);
                    //     mArrayOFImages[i].setTag(R.string.filename, mFileName);
                    mArrayOFImages[i].setOnClickListener(this);
                    mArrayOFImages[i].setVisibility(View.VISIBLE);
                }
            } else {
                Log.v("FileName", "isMissing & current listOfFileNames size is"
                        + SingletonFileNameLibrary.getInstance().getListOfFileName().size());
            }
        }

        /*fileName
         */
        // get the video recording filename and load the bitmap in the imageview
        if (requestCode == RequestThumbnailVideo && resultCode == RESULT_OK) {
            mVideoFileName = data.getExtras().getString("fileName");
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);
            mFarmerDetailsVideo1.setImageBitmap(bitmap);
            mFarmerDetailsVideo1.setVisibility(View.VISIBLE);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // these two represent icons
        mTakePicture = findViewById(R.id.take_picture);

        mTakeVideo = findViewById(R.id.take_video);


        mJustCameraButton = findViewById(R.id.mJustCamera);


        //farmerDetailsvideo1
        //mFarmerDetailsVideo =   findViewById(R.id.farmerDetailsvideo1);
// five images placed next to each other  formAction
        mFarmerDetailsImage1 = findViewById(R.id.farmerDetails1);
        mFarmerDetailsImage2 = findViewById(R.id.farmerDetails2);
        mFarmerDetailsImage3 = findViewById(R.id.farmerDetails3);
        mFarmerDetailsImage4 = findViewById(R.id.farmerDetails4);
        mFarmerDetailsImage5 = findViewById(R.id.farmerDetails5);

        mFarmerDetailsVideo1 = findViewById(R.id.farmerDetailsvideo1);
        // mFarmerImageGroup = findViewById(R.id.group);

        // all click events are handled in switch case buddy
        mTakePicture.setOnClickListener(this);
        mTakeVideo.setOnClickListener(this);
        mJustCameraButton.setOnClickListener(this);
        mArrayOFImages = new ImageView[]{
                mFarmerDetailsImage1,
                mFarmerDetailsImage2,
                mFarmerDetailsImage3,
                mFarmerDetailsImage4,
                mFarmerDetailsImage5

        };
        mFarmerDetailsVideo1.setOnClickListener(this);
    }

}
