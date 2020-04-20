package com.example.camera;

import android.util.Size;

import java.util.ArrayList;
import java.util.List;

public class VideoFrameSize {
    List<Size> mVideoFrameSize = new ArrayList<>();
    private VideoFrameSize(){

    }

    public static VideoFrameSize getInstance(){

        return new VideoFrameSize();

    }


    public void setmVideoFrameSize(List<Size> mVideoFrameSize){

        this.mVideoFrameSize.addAll(mVideoFrameSize);
    }














}
