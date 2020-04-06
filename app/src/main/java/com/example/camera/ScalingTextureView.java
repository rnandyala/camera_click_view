package com.example.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

public class ScalingTextureView extends TextureView {

    int mRatioWidth = 0;
    int mRatioHeight = 0;

    public ScalingTextureView(Context context) {
        super(context);
    }

    public ScalingTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalingTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScalingTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // aspect ratio for camera preview based on screen size textureView gets scaled
    // textureview acts as surface for camera
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("size cannot be negative");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        }
        setMeasuredDimension(width,height);
    }
}
