package com.example.camera;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.telecom.VideoProfile;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.example.camera.cameraInterface.ICameraFacing;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_SHORT;

public class CameraActivity extends AppCompatActivity implements ICameraFacing, IBitmapConnector {

    List<Size> mHighSpeedVideoresolutions;
    String mVideoUpdate;

    String mVideoQuality;
    String mVideResolution;
    String mImageQuality;
    String mImageResolution;

    private IBitmapConnector mIBitmapConnector;

    CameraSettingModel mCameraSettingModel;

    public static final int REQUEST_CODE_SETTINGS = 786;

    private ImageButton mOrientation;

    private ImageButton mSettings;

    private ConstraintLayout mPrimayContainerImageView;


    private String mCameraOrientation = "none";
    ICameraFacing mICameraFacing;
    static Uri tempImageUri;
    private ImageButton mDiscardImage;
    private ImageButton mAcceptImage;
    String CAMERA_POSITION_FRONT = "";
    String CAMERA_POSITION_BACK = "";

    private static String fileName;
    private MediaRecorder mMediaRecorder;
    private Chronometer mChronometer;
    private Size mVideoSize;

    private ImageButton mVideoRecorder;

    private File mVideoFolder;

    private String mVideoFileName;


    private Image mCapturedImage;
    private boolean mIsImageAvailable = false;
    private ImageReader mImageReader;
    private static final String TAG = "Camera2API";
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ImageView mInternalMemoryImage;
    private ConstraintLayout mConstraintContainer;
    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;
    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     * <p>
     * Semaphore has in total 1 lock
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * An {@link ScalingTextureView} for camera preview.
     */
    private ScalingTextureView mTextureView;

    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private int MAX_PREVIEW_HEIGHT = 1080;

    private int SCREEN_WIDTH = 0;


    private int SCREEN_HEIGHT = 0;

    private float ASPECT_RATIO_ERROR_RANGE = 0.1f;

    private boolean isVideorecording = false;
    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;


    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private void acceptImage() {

        mAcceptImage.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mThumbnailIntent = new Intent();

                        //

                        if (!fileName.isEmpty()) {

                            mThumbnailIntent.putExtra("fileName", fileName);

                            mThumbnailIntent.putExtra("fromVideothubnail", mVideoUpdate);

                            setResult(RESULT_OK, mThumbnailIntent);
                            CameraActivity.this.finish();
                        } else {
                            Toast.makeText(CameraActivity.this, "unable to save the file buddy", Toast.LENGTH_LONG).show();
                        }

                    }
                }

        );

    }


    private void discardImage() {
        mDiscardImage.setOnClickListener(

                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        // The below method retruns the file object
                        getFilesDir();


                        // Where as this method return the path
                        String dir = getFilesDir().getAbsolutePath();
                        if (!fileName.isEmpty())
                            try {
                                // get the file path
                                File mFile = new File(getApplicationContext().getExternalFilesDir(null), fileName);
                                //get the imageUri
                                tempImageUri = Uri.fromFile(mFile);
                                // what is happening here?
                                CameraActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, tempImageUri));


                                closeCamera();
                                reOpenCamera();
                                //         mPrimayContainerImageView.setVisibility(View.GONE);
                                mInternalMemoryImage.setVisibility(View.GONE);
                                mDiscardImage.setVisibility(View.GONE);
                                mAcceptImage.setVisibility(View.GONE);

                                mConstraintContainer.setVisibility(View.GONE);

                                mIsImageAvailable = false;
                                //         MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), tempImageUri);

                            } catch (Exception ex) {

                            }


                    }
                }
        );
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences mSharedResults = null;
        mSharedResults = PreferenceManager.getDefaultSharedPreferences(this);

        mImageQuality = mSharedResults.getString("PREF_UPDATE_IMAGE_QUALITY", "");
        mImageResolution = mSharedResults.getString("PREF_UPDATE_RESOULTION", "");
        mVideoQuality = mSharedResults.getString("PREF_VIDEO_QUALITY", "");
        mVideResolution = mSharedResults.getString("PREF_VIDEO_RESOULTION", "");

        if (mImageQuality.isEmpty() && mImageResolution.isEmpty() && mVideoQuality.isEmpty() && mVideResolution.isEmpty()) {

            mImageQuality = "100";
            mImageResolution = "1920*1080";
            mVideoQuality = "LOW";
            mVideResolution = "1280*720";

        }


        mCameraSettingModel = new CameraSettingModel(mVideoQuality, mVideResolution, mImageQuality, mImageResolution);

        Log.v("video", "image" + mVideoQuality + "\n" + mVideResolution + "\n" + mImageQuality + "\n" + mImageResolution);


        IntentFilter mIntentFilterAction = new IntentFilter();
        mIntentFilterAction.addAction("Custom_Intent");

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilterAction);
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        closeCamera();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // instance of callback


    // call back to infalte surface

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        // width and height of the textureview that created in the xml file.
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("camera2", "onSurfaceTextureavailable: w: " + width + "onSurfaceTextureavailable: h: " + height);


            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d("camera2", "onSurfaceTextureavailable: w: " + width + "onSurfaceTextureavailable: h: " + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //"@+id/texture"
    ImageButton mCamera;
    private final static int REQUEST_CODE_READ_PERMISSION = 01;

    private final static int REQUEST_CODE_VIDEO_PERMISSION = 786;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        startBackGroundProcess();
        reOpenCamera();


    }
    /*
       public void reopenCamera() {
        Log.d(TAG, "reopenCamera: called.");
        if (mTextureView.isAvailable()) {
            Log.d(TAG, "reopenCamera: a surface is available.");
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            Log.d(TAG, "reopenCamera: no surface is available.");
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
     */


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reOpenCamera() {
// gets called when surface is already present
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
            //  openCamera(640, 480);
        } else {
            // If surface is not present then below method gets called
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openCamera(int width, int height) {
        askPermission(width, height);
        //      setUpCameraOutputs(width, height);
    }

    /*
      if(mBackgroundThread == null){
            Log.d(TAG, "startBackgroundThread: called.");
            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
     */

    private void startBackGroundProcess() {

        if (mBackgroundThread == null) {
            Log.d("backgroundThread", "backgroundthreadInstance is created");

            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            mInternalMemoryImage.setVisibility(View.VISIBLE);
            byte[] byteArray = intent.getByteArrayExtra("image");
            displayCapturedImage(byteArray);
            //     mCapturedImage
            mDiscardImage.setVisibility(View.VISIBLE);
            mAcceptImage.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        intViews();
        initIntent();
        discardImage();
        acceptImage();


        mSettings.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent mSettingIntent = new Intent(CameraActivity.this, CameraSettings.class);
                        startActivityForResult(mSettingIntent, REQUEST_CODE_SETTINGS);


                    }
                }

        );


        mVideoRecorder.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if (isVideorecording) {

                            mChronometer.stop();
                            mChronometer.setVisibility(View.GONE);
// when video is not being recorded then
                            videoRecording();
                            mMediaRecorder.stop();
                            mMediaRecorder.reset();
                            isVideorecording = false;


                            File mFile = new File(CameraActivity.this.getExternalFilesDir(null), mVideoFileName);
                            final int THUMBSIZE = 400;
                            //final Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(recordedFile, MediaStore.Video.Thumbnails.MINI_KIND);

                            CancellationSignal mCancelSignal = new CancellationSignal();
                            fileName = mFile.getAbsolutePath();

                            Bitmap mThumbImage = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);

                            //   setmThumbImage(mThumbImage);
                            mConstraintContainer.setVisibility(View.VISIBLE);
                            mInternalMemoryImage.setImageBitmap(mThumbImage);
                            mInternalMemoryImage.setVisibility(View.VISIBLE);
                            mDiscardImage.setVisibility(View.VISIBLE);
                            mAcceptImage.setVisibility(View.VISIBLE);

                            mVideoFileName.toCharArray();

                            fileName = mVideoFileName;
                            //Drawable img = getResources().getDrawable(android.R.d)
                            mVideoRecorder.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_video_online));
                        } else {
                            // when videos is recording then
                            isVideorecording = true;
                            mVideoRecorder.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_video_busy));
                            startRecord();
                            mMediaRecorder.start();
                            mChronometer.setBase(SystemClock.elapsedRealtime());
                            mChronometer.setVisibility(View.VISIBLE);
                            mChronometer.start();
                        }

                    }
                }

        );


        mCamera.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if (!mIsImageAvailable) {
                            takePicture();

                        }

                        Toast.makeText(CameraActivity.this, "wait", LENGTH_SHORT).show();
                    }
                }

        );


        mOrientation.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        toggleCameraDisplayOrientation();
                    }
                }
        );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {

            //PREF_UPDATE_IMAGE_QUALITY
            //  PREF_UPDATE_RESOULTION

            //PREF_VIDEO_IMAGE_QUALITY
            //PREF_VIDEO_RESOULTION


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void toggleCameraDisplayOrientation() {

        if (mCameraId.equals(mICameraFacing.getBackCameraId())) {
            mCameraId = mICameraFacing.getFrontCameraId();
            mICameraFacing.setCameraFrontFacing();
            closeCamera();
            reOpenCamera();

        } else if (mCameraId.equals(mICameraFacing.getFrontCameraId())) {
            mCameraId = mICameraFacing.getBackCameraId();
            mICameraFacing.setCameraBackFacing();
            closeCamera();
            reOpenCamera();
        }
    }

    private void initIntent() {
        Boolean mPhoto = getIntent().getExtras().getBoolean("picture");

        mVideoUpdate = getIntent().getStringExtra("fromVideothubnail");

        // Boolean mVideo = getIntent().getExtras().getBoolean("video");

        String isVideo = getIntent().getExtras().getString("");
        if (mPhoto != null && mPhoto) {

            mCamera.setVisibility(View.VISIBLE);
            mVideoRecorder.setVisibility(View.GONE);
        } else {

            mVideoRecorder.setVisibility(View.VISIBLE);
            mCamera.setVisibility(View.GONE);

        }

        String mFileName = getIntent().getExtras().getString(FormAction.VIDEO);

        if (mFileName != null) {

        }


    }

    private void videoRecording() {
    }

    private void takePicture() {
        /*case R.id.stillshot: {
            if(!mIsImageAvailable){
                Log.d(TAG, "onClick: taking picture.");
                takePicture();
            }
            break;
        }*/


        lockFocus();
    }

    private void lockFocus() {

        try {
            // lock the focus AF - foucs
            mPreviewRequestBuilder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);


            mState = STATE_WAITING_LOCK;


            mCaptureSession.capture(
                    mPreviewRequestBuilder.build(),
                    mCaptureCallback,
                    mBackgroundHandler
            );


        } catch (CameraAccessException ex) {
            ex.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askPermission(int width, int height) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&
                (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                &&
                (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "already_granted_the_permission", Toast.LENGTH_SHORT).show();
            // preview size is set here

            setUpCameraOutputs(width, height);
            CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            try {
                if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("Time out waiting to lock camera opening.");
                }

                // cameraId is whether is front camera or a back camera details
                //     cameradevice statecallback --> just used for preview
                manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
                //   manager


            } catch (CameraAccessException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
            }

            // user granted permission

        }
        // for second and subsequent denial time when user visits this gets executed
        else if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))

                &&
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                &&
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                        &&
                        (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
                )

        ) {
            Toast.makeText(this, "required camera, write  & read permission or else", Toast.LENGTH_SHORT).show();

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_READ_PERMISSION);

        } else {
            // first denial
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA}, REQUEST_CODE_READ_PERMISSION);


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE_READ_PERMISSION:
// gets called when we request the permission for the first time

// forget about this condition as we are opening camera in onresume checkselfPermission gets triggered every time,..
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "yeah I am asking permission for the first time and you granted it", Toast.LENGTH_LONG).show();

                    //


                }
                // on check & deny shouldshowrequestpermissionrationale will  be false
                // as well as when I allow  permission shouldshowrequestpermissionrationale will be false
                //
                // else it will be true
                else if (!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                        && !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
                        !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                        &&
                        !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))

                ) {
                    Toast.makeText(this, "go to setting and accept the permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, " oops I cannot open camera grant failed", Toast.LENGTH_LONG).show();
                }




                 /*
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(this, "requesting audio permissions",Toast.LENGTH_LONG);
                    }

                    else if(!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))){
                        Toast.makeText(this,"go to setting of the application and accept the permission", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(this, "oops I cannot record audio grant failed", Toast.LENGTH_LONG).show();
                    }
*/

        }


    }


    /**
     * check cameraDevice state
     * Release lock and create a camerapreviewsession (different resolutions of the camera are compared  to the phone resolution & appropriate
     * resolution is applied to the textureview )
     * cameradevice disconnected call then release the lock and close the cameradevice
     * <p>
     * <p>
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            mMediaRecorder = new MediaRecorder();

            if (isVideorecording) {
                startRecord();
                mMediaRecorder.start();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.setVisibility(View.VISIBLE);
                mChronometer.start();
            } else {

                createCameraPreviewSession();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            Log.d(TAG, "onError: " + error);
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = CameraActivity.this;
            if (null != activity) {
                activity.finish();
            }
        }
    };


    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showSnackBar(final String text, final int length) {
        final Activity activity = CameraActivity.this;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = activity.findViewById(android.R.id.content).getRootView();
                    Snackbar.make(view, text, length).show();
                }
            });
        }
    }


    // All this method does collect list of available aspect ratios and filter
    // the appropriate resolution and assign it to the preview size

    // CameraMaxnager has list of characteristics that characterstics are use to find the filterID such as front or back facing camera

    /**
     * Sets up member variables related to camera.
     * <p>
     * adding valid camera sizes and
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void setUpCameraOutputs(int width, int height) {


        Activity activity = CameraActivity.this;

        //  VideoProfile.CameraCapabilities mCameraCapabilities = new VideoProfile.CameraCapabilities(width, height);
        //  mCameraCapabilities.


        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            mICameraFacing = this;
// for initial case front and back facing camera  will be false so findCameraIds returns whether it is front or back facing camera
            if (!(mICameraFacing.isCameraBackFacing()) && !(mICameraFacing.isCameraFrontFacing())) {
                findCameraIds();
            }

            // gets the characterstics of front or back camera
            // mCmaeraId represent front or back camera which is assigned in findcameraIds method!
            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(mCameraId);

            Log.d(TAG, "setUpCameraOutputs: camera id: " + mCameraId);


            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            mHighSpeedVideoresolutions = Arrays.asList(map.getHighSpeedVideoSizes());

            //     map.getHighSpeedVideoFpsRanges();


            // I should size object here
            //map.getHighSpeedVideoFpsRangesFor()

            // map.getHighSpeedVideoSizesFor()  // should pass a range of fps(frames per second)

            assert map != null;
            Size largest = null;
            float screenAspectRatio = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
            List<Size> sizes = new ArrayList<>();
            List<Size> sizesBasedOnScreenAspectRatioIfAny = new ArrayList<>();


            // map.getOutSizes(ImageFormat.JPEG) gives me the list of available resolutions in the camera
            for (Size size : Arrays.asList(map.getOutputSizes(ImageFormat.JPEG))) {

                float temp = (float) size.getWidth() / (float) size.getHeight();

                Log.d(TAG, "setUpCameraOutputs: temp: " + temp);
                Log.d(TAG, "setUpCameraOutputs: w: " + size.getWidth() + ", h: " + size.getHeight());
//sizes.add(2.1);
                sizes.add(size);

                if (temp > (screenAspectRatio - screenAspectRatio * ASPECT_RATIO_ERROR_RANGE)
                        && temp < (screenAspectRatio + screenAspectRatio * ASPECT_RATIO_ERROR_RANGE)) {
                    sizesBasedOnScreenAspectRatioIfAny.add(size);

                    // sizes.add(size);

                    Log.d(TAG, "setUpCameraOutputs: found a valid size: w: " + size.getWidth() + ", h: " + size.getHeight());
                }

            }
            if (!sizesBasedOnScreenAspectRatioIfAny.isEmpty()) {
                largest = Collections.max(
                        sizes,
                        new Utility.CompareSizesByArea());

                String[] mArrayOfRes = mImageResolution.split("[*]+");
                int mWidth = Integer.parseInt(mArrayOfRes[0]);
                int mHeight = Integer.parseInt(mArrayOfRes[1]);

                mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.JPEG, 2);
                //  mImageReader = ImageReader.newInstance(40, 20, ImageFormat.JPEG, 2);

                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
                Log.d(TAG, "setUpCameraOutputs: largest width: " + largest.getWidth());
                Log.d(TAG, "setUpCameraOutputs: largest height: " + largest.getHeight());


            } else if (sizes.size() > 0) {
                largest = Collections.max(
                        sizes,
                        new Utility.CompareSizesByArea());
                String[] mArrayOfRes = mImageResolution.split("[*]+");
                int mWidth = Integer.parseInt(mArrayOfRes[0]);
                int mHeight = Integer.parseInt(mArrayOfRes[1]);

                mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.JPEG, 2);

                //    mImageReader = ImageReader.newInstance(40, 20, ImageFormat.JPEG, 2);

                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
                Log.d(TAG, "setUpCameraOutputs: largest width: " + largest.getWidth());
                Log.d(TAG, "setUpCameraOut   puts: largest height: " + largest.getHeight());
            }


            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
                default:
                    Log.e(TAG, "Display rotation is invalid: " + displayRotation);
            }

            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            Log.d(TAG, "setUpCameraOutputs: max preview width: " + maxPreviewWidth);
            Log.d(TAG, "setUpCameraOutputs: max preview height: " + maxPreviewHeight);


            if (largest != null) {

                mPreviewSize = Utility.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                mVideoSize = Utility.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),

                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest
                );
                String[] mVideoRes = mVideResolution.split("[*]+");

                int mWidth = Integer.parseInt(mVideoRes[0]);

                int mHeight = Integer.parseInt(mVideoRes[1]);


                List<Size> mArraySize = Arrays.asList(map.getHighSpeedVideoSizes());


            }


            Log.d(TAG, "setUpCameraOutputs: preview width: " + mPreviewSize.getWidth());
            Log.d(TAG, "setUpCameraOutputs: preview height: " + mPreviewSize.getHeight());

            // We fit the aspect ratio of TextureView to the size of preview we picked.
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(
                        mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(
                        mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }


            Log.d(TAG, "setUpCameraOutputs: cameraId: " + mCameraId);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
          /*
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG); */
        }

    }

    // set front and back facing camera ID's
    private void findCameraIds() {
        Activity activity = CameraActivity.this;
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            // returns two integers which determines whether it is a front facing camera or a back facing camera
            for (String cameraId : manager.getCameraIdList()) {
                Log.d(TAG, "setCameraOrientation: CAMERA ID: " + cameraId);
                if (cameraId == null) continue;
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);


                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    //mCameraId = cameraId;
                    //   mICameraFacing.

                    mICameraFacing.setBackFacingCamera(cameraId);
                }

                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    //  mCameraId = cameraId;

                    mICameraFacing.setFrontFacingCamera(cameraId);
                }

                // for the first time launch we will set the camera to front facing

                mICameraFacing.setCameraFrontFacing();

                mCameraId = mICameraFacing.getFrontCameraId();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //  capturesession (mCaptureCallback) has callbacks like captureprogressed and captureCompleted!

    // supports JPEG format
    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }

                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    } else if (afState == CaptureResult.CONTROL_AF_STATE_INACTIVE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }

            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };


    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void captureStillPicture() {

        Log.d(TAG, "captureStillPicture: capturing picture.");
        try {

            final Activity activity = CameraActivity.this;
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);


            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // Orientation
            // Rotate the image from screen orientation to image orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            // before unlocking focus making views visible


            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    // preview size
    /*

    // set up camera device and keep on showing camera preview


    preview size is set as textureview buffersize
    OS allocates memory for the preview when it is saving the previous image

  preview size is nothing but the aspect ratio of the device

  capture request builder has an instance of the surface

  cameraDevice.CreateCaptureSession Callback  housekeeping work such as auto focus etc
   //
    mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
     */

    // create preview session is equivalent to startPreview session
    private void createCameraPreviewSession() {


        try {

            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());


            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);


            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;

                            try {
                                // Auto focus should be continuous for camera preview.
                                // Most new-ish phones can auto focus
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);


                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showSnackBar("Failed", Snackbar.LENGTH_LONG);
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void startRecord() {

        try {
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());


            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);
            Surface recordSurface = mMediaRecorder.getSurface();

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            mPreviewRequestBuilder.addTarget(surface);
            mPreviewRequestBuilder.addTarget(recordSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface, recordSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            try {
                                session.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                        }
                    }, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {

            if (!mIsImageAvailable) {

                mCapturedImage = reader.acquireNextImage();





                /*
convert image to ByteBuffer,
get the bytes array
and then get bitmap using decodeByteArray
                 */
                /*
                ByteBuffer buffer = mCapturedImage.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);*/

                if (mCapturedImage != null) {
                    Log.d(TAG, "onImageAvailable: captured image width: " + mCapturedImage.getWidth());
                    Log.d(TAG, "onImageAvailable: captured image height: " + mCapturedImage.getHeight());

                    String[] mArrayOfRes = mImageResolution.split("[*]+");

                    int width = Integer.parseInt(mArrayOfRes[0]);
                    int height = Integer.parseInt(mArrayOfRes[1]);


                    ReducePixelImage mReducedPixelImage = new ReducePixelImage(mCapturedImage, width, height, CameraActivity.this);


                    mBackgroundHandler.post(mReducedPixelImage);
                    // Before saving picture to the storage I want to reduce the pixel s
                    saveTempImageToStorage();

                }
            }
            // when image is available in the image reader then this gets triggered..

        }
    };

    private void saveTempImageToStorage() {

// code is moved to interface callback that is created in reducePixelImage
    }




    /*
      values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
    values.put(Images.Media.MIME_TYPE, "image/jpeg");
    values.put(MediaStore.MediaColumns.DATA, filePath);

    context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
     */

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


    }

    @Override
    public boolean isCameraFrontFacing() {


        if (mCameraOrientation.equals(CAMERA_POSITION_FRONT)) {

            return true;
        } else {
            return false;
        }


    }

    @Override
    public boolean isCameraBackFacing() {

        if (mCameraOrientation.equals(CAMERA_POSITION_BACK)) {
            return true;
        } else {

            return false;
        }


    }

    @Override
    public void setFrontFacingCamera(String mFrontFacingCameraId) {
        CAMERA_POSITION_FRONT = mFrontFacingCameraId;
    }

    @Override
    public void setBackFacingCamera(String mBackFacingCameraId) {
        CAMERA_POSITION_BACK = mBackFacingCameraId;
    }

    @Override
    public void setCameraFrontFacing() {
        mCameraOrientation = CAMERA_POSITION_FRONT;
    }

    @Override
    public void setCameraBackFacing() {
        mCameraOrientation = CAMERA_POSITION_BACK;
    }

    @Override
    public String getBackCameraId() {
        return CAMERA_POSITION_BACK;
    }

    @Override
    public String getFrontCameraId() {
        return CAMERA_POSITION_FRONT;
    }

    @Override
    public void setReducedBitmap(Image mCapturedImage) {
//mBitmap.getWidth();
//mBitmap.getHeight();

        this.mCapturedImage = mCapturedImage;
        Log.d(TAG, "saveTempImageToStorage: saving temp image to disk.");
        final ICallback callback = new ICallback() {
            @Override
            public void done(Exception e) {
                if (e == null) {
                    Log.d(TAG, "onImageSavedCallback: image saved!");

                    mIsImageAvailable = true;


                    Intent i = new Intent(CameraActivity.this, ReadImage.class);
                    if (fileName != null) {
                        i.putExtra("fileNameOfImage", fileName);
                    } else {
                        Toast.makeText(CameraActivity.this, "image not available in the storage", Toast.LENGTH_LONG);
                    }
                    startService(i);
                    if (mCapturedImage != null) {
                        mCapturedImage.close();
                    }

                } else {
                    Log.d(TAG, "onImageSavedCallback: error saving image: " + e.getMessage());
                    showSnackBar("Error displaying image", Snackbar.LENGTH_SHORT);
                }
            }
        };


        ImageSaver imageSaver = new ImageSaver(
                mCapturedImage
                ,
                CameraActivity.this.getExternalFilesDir(null),
                callback, this, mImageQuality
        );
        mBackgroundHandler.post(imageSaver);
// pass this bitmap
    }
/*
    @Override
    public void getBitMapImage(Bitmap mBitMap) {

mImageView.setImageBitmap(mBitMap);
closeCamera();
    }

    @Override
    public void errorResult(String s) {
        Toast.makeText(this, "unable to read image from the storage:: "+s,LENGTH_SHORT).show();

    }*/


    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The file we save the image into.
         */
        private final File mFile;
        private Context mContext;
        /**
         * Original image that was captured
         */
        private Image mCapturedImage;

        private ICallback mCallback;

        private String mImageQuality;

        ImageSaver(Image mCapturedImage, File file, ICallback callback, Context mContext, String mImageQuality) {
            mFile = file;
            mCallback = callback;
            this.mContext = mContext;
            this.mCapturedImage = mCapturedImage;
            this.mImageQuality = mImageQuality;
        }

        @Override
        public void run() {
            if (mCapturedImage != null) {


                ByteBuffer buffer = mCapturedImage.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {

                    Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);



                    /*x
                    formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    strDate = formatter.format(date);
*/
                    Date mDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
                    String mCurrentDateTime = formatter.format(mDate);

                    fileName = mCurrentDateTime + "image.jpg";
                    File file = new File(mFile, fileName);


                    // This add images to the galleryaddImageToGallery(file.getAbsolutePath(), mContext);
                    output = new FileOutputStream(file);
                    int mImageQualityPercentage = Integer.parseInt(mImageQuality);

                    mBitmap.compress(Bitmap.CompressFormat.JPEG, mImageQualityPercentage, output);

                    //  output.write(mImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.done(e);
                } finally {
                    //   mImage.close();
                    if (null != output) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mCallback.done(null);


                }
            }
        }
    }


    private void showStillshotContainer() {
        closeCamera();
        mConstraintContainer.setVisibility(View.VISIBLE);


    }


    /**
     * Closes the current {@link CameraDevice}.
     * close capturesession,
     * close cameradevice,
     * close imageReader
     */
    private void closeCamera() {

        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                //    mCameraDevice.

                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    // display captured picture
    private void displayCapturedImage(byte[] mByteArrayImage) {
        Log.d(TAG, "displayCaptureImage: displaying stillshot image.");
        final Activity activity = CameraActivity.this;
        if (activity != null) {
            activity.runOnUiThread(() -> {

                        RequestOptions options = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true);

                        Glide.with(activity)
                                .setDefaultRequestOptions(options)
                                .load(mByteArrayImage)
                                .into(mInternalMemoryImage);
                        showStillshotContainer();
                    }


            );
        }
    }


    // video saver


    /**
     * Saves a MP4 {@link Image} into the specified {@link File}.
     */
    private static class VideoSaver implements Runnable {

        /**
         * The file we save the image into.
         */
        private final File mFile;
        private Context mContext;
        /**
         * Original image that was captured
         */
        private Image mImage;

        private ICallback mCallback;

        VideoSaver(Image image, File file, ICallback callback, Context mContext) {
            mImage = image;
            mFile = file;
            mCallback = callback;
            this.mContext = mContext;
        }

        @Override
        public void run() {

            if (mImage != null) {
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {
                    File file = new File(mFile, "temp_image.jpg");

                    // This add images to the galleryaddImageToGallery(file.getAbsolutePath(), mContext);


                    output = new FileOutputStream(file);
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    mCallback.done(e);
                } finally {
                    mImage.close();
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mCallback.done(null);


                }
            }
        }
    }

    private void setUpMediaRecorder() throws IOException {
//  CameraActivity.this.getExternalFilesDir(null)
        //   getExternalFilesDir(null).getAbsolutePath();

        File file = new File(getExternalFilesDir(null), "temp_video.mp4");
        mVideoFileName = file.getAbsolutePath();
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//

        //  CamcorderProfile.QUALITY_480P;

//CamcorderProfile.QUALITY_480P;

//CamcorderProfile.QUALITY_2160P;

        //CamcorderProfile.QUALITY_2160P;


//640,480
        if (mVideoQuality.equals("HIGH")) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH)) {
                Log.v("apprently", "result is");
                CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;

                String[] resolutions = mVideResolution.split("[*]+");

                if (mCameraId.equals(mICameraFacing.getBackCameraId())) {


Size mResultVideoResolution =                     Utility.getApproriateVideoSize(mHighSpeedVideoresolutions,
                                    Integer.parseInt(resolutions[0]), Integer.parseInt(resolutions[1]));


                    // Utility.getApproriateVideoSize(m)
                    profile.videoFrameWidth = mResultVideoResolution.getWidth();
                    profile.videoFrameHeight = mResultVideoResolution.getHeight();


                    // profile
                } else {
             //       mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());

                    profile.videoFrameWidth = mVideoSize.getWidth();
                    profile.videoFrameHeight = mVideoSize.getHeight();
                    // mMediaRecorder.setOrientationHint(270);
                }


                profile.videoCodec = MediaRecorder.VideoEncoder.H264;
                profile.audioCodec = MediaRecorder.AudioEncoder.AAC;
                mMediaRecorder.setProfile(profile);
            }
        } else if (mVideoQuality.equals("LOW")) {
            if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_LOW)) {

                CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);

                //input.split("[\\s@&.?$+-]+");
                String[] resolutions = mVideResolution.split("[*]+");
                if (mCameraId.equals(mICameraFacing.getBackCameraId())) {
                    Size mResultVideoResolution =                     Utility.getApproriateVideoSize(mHighSpeedVideoresolutions,
                            Integer.parseInt(resolutions[0]), Integer.parseInt(resolutions[1]));

                    profile.videoFrameWidth = mResultVideoResolution.getWidth();
                    profile.videoFrameHeight = mResultVideoResolution.getHeight();
                }
                else {
                  //  mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());


                    profile.videoFrameWidth = mVideoSize.getWidth();
                    profile.videoFrameHeight = mVideoSize.getHeight();
                    //    mMediaRecorder.setOrientationHint(270);


                }


                profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
                profile.videoCodec = MediaRecorder.VideoEncoder.H264;
                mMediaRecorder.setProfile(profile);

            }


        }


        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setVideoEncodingBitRate(1000000);
        mMediaRecorder.setVideoFrameRate(30);
        //  mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());

        //     mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // mMediaRecorder.setOrientationHint();

        if (mCameraId.equals(mICameraFacing.getFrontCameraId()))
            mMediaRecorder.setOrientationHint(270);


        mMediaRecorder.prepare();

    }


    private void intViews() {
// orientation
        mOrientation = findViewById(R.id.switch_orientation);
        //discard_image
        mDiscardImage = findViewById(R.id.discard_image);
        mAcceptImage = findViewById(R.id.accept_image);
        mVideoRecorder = findViewById(R.id.record_video);
        mConstraintContainer = findViewById(R.id.image_container);
        mInternalMemoryImage = findViewById(R.id.mCapturedBitmap);


        mPrimayContainerImageView = findViewById(R.id.primary_container_image);

        mTextureView = findViewById(R.id.texture);
        mCamera = findViewById(R.id.camera);
        mChronometer = findViewById(R.id.mChronometer);

        mSettings = findViewById(R.id.settings);

    }


}

