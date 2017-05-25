package com.join.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CertifyCameraManager implements ErrorCallback {

    private Camera camera = null;
    private boolean mPreviewing = false;
    private Parameters cameraParameters = null;
    private SurfaceHolder holder;
    private Context context;
    IPictureCallback2 iPictureCallback2;
    @SuppressLint({"SimpleDateFormat"})
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 拿到相机
     *
     * @param context
     * @return
     */
    public Camera getCamera(Context context) {
        this.context = context;
        if (camera == null) {
            // LogUtil.wInfo("init Camera");
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
        //LogUtil.wInfo("openCamera");
        try {
            camera = findCamera();
            if (camera != null) {
                initCamera(1600, 1200);
            }
        } catch (Exception e) {
            camera = null;
            LogUtil.wError(e);
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
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            return null;
        }
        // LogUtil.wInfo("find Camera number:" + numberOfCameras);
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                // LogUtil.wInfo("FACING BACK CAMERA");
                return Camera.open(i);
            } else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                //  LogUtil.wInfo("FACING FRONT CAMERA");
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

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            stopPreview();
            String photo_name = "";
            String path = Settings.get().getCertifyPath();
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                Date date = new Date();

                String save_name = (format.format(date) + ".jpg");
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
                photo_name = saveFile.getAbsolutePath();
            } catch (Exception e) {
                closeCamera();
            } finally {
                if (iPictureCallback2 != null) {

                 /*  /Log.e("jjjjj","jjjjjjjjjjjj");
                    Message message = Message.obtain();
                    message.what = MsgCons.CAMERA_PHOTO_PREPARED;
                    message.obj = photo_name;*/
                    iPictureCallback2.photoPrepared();
                }

            }
            LogUtil.info("pictureCallback", "onPictureTaken ============= 2");
        }
    };


    /**
     * 增加回调
     *
     * @param iPictureCallback
     */
    public void addCallback2(IPictureCallback2 iPictureCallback) {
        this.iPictureCallback2 = iPictureCallback;
        LogUtil.info("pictureCallback", "addCallback ============= 1");
    }

    /**
     * 删除回调
     */
    public void delCallback() {
        this.iPictureCallback2 = null;
        LogUtil.info("pictureCallback", "addCallback ============= 1");
    }

    /**
     * 开始预览
     *
     * @param holder
     */
    public void startPreview(SurfaceHolder holder) {
        // LogUtil.wInfo("startPreview");
        LogUtil.info("pictureCallback", "startPreview ============= 1");
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
        // LogUtil.info("pictureCallback", "stopPreview ============= 1");
        //  LogUtil.wInfo("stopPreview");
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
        // LogUtil.wInfo("closeCamera");
        // LogUtil.info("pictureCallback", "closeCamera ============= 1");
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
