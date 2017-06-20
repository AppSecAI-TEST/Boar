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
    private String aonePath;

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
                Log.e(TAG, "findCamera: CAMERA_FACING_BACK" + i);

                return Camera.open(i);
            } else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.e(TAG, "findCamera: CAMERA_FACING_FRONT" + i);
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
    private String twoPath;
    private String threePath;
    private String fourPath;
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        private int i;
        private int tempNumber = 0;
        private int tempNumber2 = 0;
        private int tempNumber3 = 0;
        private int tempNumber4 = 0;
        String format2 = null;
        String format3 = null;
        String format4 = null;
        String format5 = null;

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String format = String.format("%03d", i++);


            stopPreview();
            String photo_name = "";
            if (i <= 20) {
                format2 = String.format("%03d", tempNumber++);
                if (aonePath == null) {

                    aonePath = Settings.get().getCertifyPath() + "aonePath";
                }
                File aonePathFile = new File(aonePath);

                if (!aonePathFile.exists()) {
                    aonePathFile.mkdir();
                }
            }

            if (i <= 40 && i >= 20) {
                format3 = String.format("%03d", (tempNumber2++) - 1);
                Log.e(TAG, "onPictureTaken: " + tempNumber2);
                if (twoPath == null) {

                    twoPath = Settings.get().getCertifyPath() + "twoPath";
                }

                File twoPathField = new File(twoPath);
                if (!twoPathField.exists()) {
                    twoPathField.mkdir();
                }
            }
            if (i <= 60 && i >= 40) {
                format4 = String.format("%03d", (tempNumber3++) - 1);
                if (threePath == null) {

                    threePath = Settings.get().getCertifyPath() + "threePath";
                }
                File twoPathField = new File(threePath);
                if (!twoPathField.exists()) {
                    twoPathField.mkdir();
                }

            }

            if (i <= 80 && i >= 60) {
                format5 = String.format("%03d", (tempNumber4++) - 1);
                if (fourPath == null) {
                    fourPath = Settings.get().getCertifyPath() + "fourPath";
                }
                File twoPathField = new File(fourPath);
                if (!twoPathField.exists()) {
                    twoPathField.mkdir();
                }
            }
            try {

                Log.e(TAG, "onPictureTaken: format" + format);
                String save_name = (format2 + ".jpg");
                String save_name2 = (format3 + ".jpg");
                String save_name3 = (format4 + ".jpg");
                String save_name4 = (format5 + ".jpg");

                File saveFile = new File(aonePath + File.separator + save_name);
                File saveFile1 = new File(twoPath + File.separator + save_name2);
                File saveFile2 = new File(threePath + File.separator + save_name3);
                File saveFile3 = new File(fourPath + File.separator + save_name4);

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
                if (i <= 20) {
                    FileOutputStream fos = new FileOutputStream(saveFile);
                    fos.write(data);
                    fos.close();
                    fos.flush();
                } else if (i <= 40 && i >= 20) {
                    FileOutputStream fos = new FileOutputStream(saveFile1);
                    fos.write(data);
                    fos.close();
                    fos.flush();
                } else if (i <= 60 && i >= 40) {
                    FileOutputStream fos = new FileOutputStream(saveFile2);
                    fos.write(data);
                    fos.close();
                    fos.flush();
                } else if (i <= 80 && i >= 60) {
                    FileOutputStream fos = new FileOutputStream(saveFile3);
                    fos.write(data);
                    fos.close();
                    fos.flush();
                }
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
                    if (i <= 20) {
                        iPictureCallback2.photoPrepared(i, aonePath);
                    } else if (i <= 40 && i >= 20) {
                        iPictureCallback2.photoPrepared(i, twoPath);
                    } else if (i <= 60 && i >= 40) {
                        iPictureCallback2.photoPrepared(i, threePath);
                    } else if (i <= 80 && i >= 60) {
                        iPictureCallback2.photoPrepared(i, fourPath);
                    }


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
