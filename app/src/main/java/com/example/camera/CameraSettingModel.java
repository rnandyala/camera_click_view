package com.example.camera;

public class CameraSettingModel {


    /*

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences.getString("PREF_UPDATE_IMAGE_QUALITY","");
        mPreferences.getBoolean("PREF_BOLEAN",false);
        mPreferences.getString("PREF_UPDATE_RESOULTION","");
        SharedPreferences mSharedResults= null;
        mSharedResults = PreferenceManager.getDefaultSharedPreferences(this);

        String mVideoQuality = mSharedResults.getString("PREF_UPDATE_IMAGE_QUALITY", "");
        String mVideResolution = mSharedResults.getString("PREF_UPDATE_RESOULTION", "");
        String mImageQuality = mSharedResults.getString("PREF_VIDEO_IMAGE_QUALITY", "");
        String mImageResolution = mSharedResults.getString("PREF_VIDEO_RESOULTION", "");
     */




    private String mVideoQuality;

    private String mVideoResolution;

    private String mImageQuality;

    private String mImageResolution;


    public String getmVideoQuality() {
        return mVideoQuality;
    }

    public void setmVideoQuality(String mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }

    public String getmVideoResolution() {
        return mVideoResolution;
    }

    public void setmVideoResolution(String mVideoResolution) {
        this.mVideoResolution = mVideoResolution;
    }

    public String getmImageQuality() {
        return mImageQuality;
    }

    public void setmImageQuality(String mImageQuality) {
        this.mImageQuality = mImageQuality;
    }

    public String getmImageResolution() {
        return mImageResolution;
    }

    public void setmImageResolution(String mImageResolution) {
        this.mImageResolution = mImageResolution;
    }


    public CameraSettingModel(String mVideoQuality, String mVideoResolution, String mImageQuality, String mImageResolution) {
        this.mVideoQuality = mVideoQuality;
        this.mVideoResolution = mVideoResolution;
        this.mImageQuality = mImageQuality;
        this.mImageResolution = mImageResolution;
    }
}
