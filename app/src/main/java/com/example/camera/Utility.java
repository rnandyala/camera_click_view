package com.example.camera;

import android.util.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utility {


    static Size getApproriateVideoSize(List<Size> mHighResoutionVideoSizes, int width, int height) {


        Size mVideoSize = new Size(width, height);

        if (mHighResoutionVideoSizes.contains(mVideoSize)) {
            return mVideoSize;
        }


        else {

            for (int i = 0; i < mHighResoutionVideoSizes.size(); i++) {
                Size mHighResolutionVideoSizes = mHighResoutionVideoSizes.get(i);
                // so first approiate size is set as video size
             /*
                if (mHighResolutionVideoSizes.getWidth() - mVideoSize.getWidth() < 150
                        || mVideoSize.getWidth() - mHighResolutionVideoSizes.getWidth() < 150) {
                    mVideoSize = mHighResolutionVideoSizes;
                }*/

          int    sumOfCommonResolutions = mHighResolutionVideoSizes.getWidth() + mHighResolutionVideoSizes.getHeight();

                // offset 1200
                // offset 560 560
                int offSet = 250;
                if(sumOfCommonResolutions > (1120 + offSet)){

                    return mHighResolutionVideoSizes;
                }
                else {
                    return mHighResolutionVideoSizes;
                }

            }
        }

        return mVideoSize;
    }


    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        public static CompareSizesByArea newInstance() {
            return new CompareSizesByArea();
        }

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

// mapping textureview size with camera sizes
    // if not choose near by matching value

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                  int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
//            Log.d(TAG, "chooseOptimalSize: w: " + option.getWidth() + ", h: " + option.getHeight());

            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }
}

