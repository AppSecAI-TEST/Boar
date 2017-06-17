package com.join.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CertifyCameraManager implements ErrorCallback {
    private String TAG = "jjjCertifyCameraManager";
    private Camera camera = null;
    private boolean mPreviewing = false;
    private Parameters cameraParameters = null;
    private SurfaceHolder holder;
    private Context context;
    private IPictureCallback2 iPictureCallback2;
    private String path;

    /**
     * 拿到相机
     *
     * @param context
     * @return
     */
    public Camera getCamera(Context context) {
        this.context = context;

        if (camera == null) {
            camera = openCamera();
        }
        return camera;
    }

    /**
     * 打开相机
     *
     * @return
     */
    private Camera openCamera() {
        try {
            camera = findCamera();
            if (camera != null) {
                initCamera(1920, 1080);
            }
        } catch (Exception e) {
            camera = null;

            closeCamera();
        }
        return camera;
    }

    /**
     * 发现相机
     *
     * @return
     */
    public Camera findCamera() {
        //得到相机的数目
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            Log.e(TAG, "findCamera: " + "jjjjjjjjjjjjj");
            return null;
        }
        Log.e(TAG, "findCamera: " + numberOfCameras);
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                Log.e(TAG, "findCamera: CAMERA_FACING_BACK");
                return Camera.open(i);
            } else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.e(TAG, "findCamera: CAMERA_FACING_FRONT");
                return Camera.open(i);
            }
        }
        return null;
    }

    /**
     * 初始化相机
     *
     * @param width
     * @param height
     */
    public void initCamera(int width, int height) {
        if (camera != null) {
            camera.setErrorCallback(this);
            setCameraParameters(width, height);
        } else {

        }
    }

    public void initSurfaceView(SurfaceView surfaceView) {

        surfaceView.setY(-250);
        surfaceView.setX(-300);
        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        layoutParams.height = 1000;
        layoutParams.width = 1200;
        surfaceView.setLayoutParams(layoutParams);

        holder = surfaceView.getHolder();


    }

    /**
     * 设置相机参数
     *
     * @param width
     * @param height
     */
    private void setCameraParameters(int width, int height) {
        cameraParameters = camera.getParameters();
        cameraParameters.setPreviewFrameRate(30);

        cameraParameters.setPreviewSize(width, height);
        cameraParameters.setPictureSize(width, height);
        cameraParameters.setJpegQuality(100);
        cameraParameters.setPictureFormat(ImageFormat.JPEG);
        //  cameraParameters.set("jpeg-quality", 100);
        camera.setDisplayOrientation(90);

        camera.setParameters(cameraParameters);
    }

    /**
     * 设置图片的角度
     *
     * @param angle
     * @param width
     * @param height
     * @param bitmap
     * @return
     */
    public static Bitmap rotaingImageView(int angle, int width, int height, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public void takePhoto() {
        if (camera != null) {
            camera.takePicture(null, null, pictureCallback);
        } else {

        }
    }

    /**
     * 拍照之后返回的字节
     */

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        private int i;

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String format = String.format("%03d", i++);
            stopPreview();
            String photo_name = "";
            if (path == null) {
                path = Settings.get().getCertifyPath();
            }
            Log.e(TAG, "onPictureTaken: " + path);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                //   Date date = new Date();
                Log.e(TAG, "onPictureTaken: format" + format);
                String save_name = (format + ".jpg");
                File saveFile = new File(path + File.separator + save_name);


              /*  BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                int picWidth = mBitmap.getWidth();
                int picHeight = mBitmap.getHeight();*/

                // Bitmap rBitmap = null;

                //if (picWidth > picHeight) {
                //rBitmap = rotaingImageView(90, picHeight, picHeight, mBitmap);
                //  }
                // rBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                // bos.flush();
                //  bos.close();
                FileOutputStream fos = new FileOutputStream(saveFile);
                fos.write(data);
                fos.close();
                fos.flush();
                startPreview(holder);
                // photo_name = saveFile.getAbsolutePath();
            } catch (Exception e) {
                closeCamera();
            } finally {
                if (iPictureCallback2 != null) {

                 /*
                    Message message = Message.obtain();
                    message.what = MsgCons.CAMERA_PHOTO_PREPARED;
                    message.obj = photo_name;*/
                    iPictureCallback2.photoPrepared(i, path);

                }

            }

        }
    };


    /**
     * 增加回调
     *
     * @param iPictureCallback
     */
    public void addCallback2(IPictureCallback2 iPictureCallback) {
        this.iPictureCallback2 = iPictureCallback;

    }


    /**
     * 删除回调
     */
    public void delCallback() {
        this.iPictureCallback2 = null;

    }

    /**
     * 开始预览
     *
     * @param holder
     */
    public void startPreview(SurfaceHolder holder) {

        if (!mPreviewing) {
            try {
                if (camera != null) {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    mPreviewing = true;
                }
            } catch (IOException e) {
                closeCamera();
                e.printStackTrace();
                LogUtil.wError("startPreview failed.");
            }
        }

    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            mPreviewing = false;
        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera() {
        if (camera == null) {
            return;
        } else {
            camera.setErrorCallback(null);
            if (mPreviewing) {
                stopPreview();
            }
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onError(int error, Camera camera) {
    }

}
