package com.join.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

/**
 * Created by join on 2017/3/7.
 */

public class CameraCheck {
    public static boolean CheckCamera(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            Toast.makeText(mContext, "相机不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance(Context mContext) {
        Camera c = null;
        if (CheckCamera(mContext)) {
            try {
                c = Camera.open();
         /*       Camera.Parameters parameters = c.getParameters();

                parameters.setPreviewFpsRange(20, 30); // 每秒显示20~30帧
                parameters.setPreviewFrameRate(20);// 每秒3帧 每秒从摄像头里面获得3个画面,
                parameters.setJpegQuality(100);

                c.setParameters(parameters);*/
            } catch (Exception e) {
                c = null;
            }
        }
        return c; // returns null if camera is unavailable
    }
}
