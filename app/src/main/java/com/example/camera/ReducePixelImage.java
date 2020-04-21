package com.example.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.nio.ByteBuffer;


interface IBitmapConnector{
    void setReducedBitmap(Image mCapturedImage);
}

public class ReducePixelImage implements Runnable {
    Image mCapturedImage;

    int mWidth;

    int mHeight;

    IBitmapConnector mIBitmapConnector;

    public ReducePixelImage(Image mCapturedImage, int mWidth, int mHeight, IBitmapConnector mIBitmapConnector) {

        this.mWidth = mWidth;

        this.mHeight = mHeight;

        this.mCapturedImage = mCapturedImage;

        this.mIBitmapConnector = mIBitmapConnector;

    }

    @Override
    public void run() {

        // convert image object to byte array


        try {

      /*
            ByteBuffer mBuffer = null;
            mBuffer = mCapturedImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[mBuffer.remaining()];
            mBuffer.get(bytes);

            // convert bytes to bitmap object
            Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);

            // reduced
            Bitmap mReducedImage = Bitmap.createScaledBitmap(mBitmap, mWidth, mHeight, true);

            // mCapturedImage.close();

*/

            mIBitmapConnector.setReducedBitmap(mCapturedImage);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
