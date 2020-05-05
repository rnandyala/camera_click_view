package com.example.camera;

import android.content.Context;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.TextureView;

public class ScalingTextureView extends TextureView {
Matrix mMartrix;
    int mRatioWidth = 0;
    int mRatioHeight = 0;

    ScaleGestureDetector mScaleGestureDetector;

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










    // adding zoom functionality

    private void init(Context mContext){

       // matrix is used to transform the textureview
        // mMartrix = new Matrix();

        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });

    }
}
