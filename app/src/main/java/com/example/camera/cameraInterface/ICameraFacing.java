package com.example.camera.cameraInterface;

public interface ICameraFacing {

    // use these two methods for the first launch then follows findCameraId's method
    boolean isCameraFrontFacing();
    boolean isCameraBackFacing();


    // then get front and back cameraId and set them in this method
    // such as camera position front or back string variables
    void setFrontFacingCamera(String mFrontFacingCameraId);
    void setBackFacingCamera(String mBackFacingCameraId);


    // these are used to toggle between orientation
    void setCameraFrontFacing();
    void setCameraBackFacing();


    // what ever is set at line number we get it here tada..
    String getBackCameraId();
    String getFrontCameraId();
}
