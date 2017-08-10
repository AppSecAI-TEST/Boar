package com.join.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.join.camera.IPictureCallback2;
import com.join.camera.Settings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class NewCamera {
    private String TAG = "jjjNewCamera";
    private Camera camera;
    private boolean isPreview = false; // 是否在浏览中
    private int pic_name = -1;
    private boolean saveTag = false;
    private boolean threadState = false;
    private IPictureCallback2 iPictureCallback2;
    private MyHandler myHandler;
    private int threadClose;

    public MyHandler getMyHandler() {
        if (myHandler == null) {
            myHandler = new MyHandler();
        }

        return myHandler;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    if (camera != null) {
                        camera.setPreviewCallback(new StreamIt()); // 设置回调的类
                    }
                    pic_name = msg.arg1;
                    threadState = true;
                    saveTag = true;
                    Log.e(TAG, "handleMessage: " + msg.arg1);
                    break;
                case 2:
                    threadClose = 2;
                    threadState = false;
                    break;
            }
        }
    }

    /**
     * 拿到相机 设置参数
     */
    public Camera getMcamera() {
        try {
            camera = Camera.open(0);
            if (camera != null && !isPreview) {
                try {
                    Camera.Parameters parameters = camera.getParameters();
           /*         List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
                    for (int i = 0; i < supportedPreviewSizes.size(); i++) {
                        Camera.Size size = supportedPreviewSizes.get(i);
                        int height = size.height;
                        int width = size.width;
                        Log.e("jjjjj", height + "\n" + width);
                    }*/
                    parameters.setPreviewFpsRange(20, 30); // 每秒显示20~30帧
                    parameters.setPreviewFrameRate(20);// 每秒3帧 每秒从摄像头里面获得3个画面,
                    parameters.setPictureSize(1600, 1200); // 设置照片的大小
                    parameters.setPreviewSize(1600, 1200); // 设置预览照片的大小
                    parameters.setPictureFormat(ImageFormat.NV21); // 设置图片格式
                    camera.setPreviewCallback(new StreamIt()); // 设置回调的类

                    camera.setParameters(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isPreview = true;
            }
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();

        }
        return camera;
    }

    Settings settings = Settings.get();

    private class StreamIt implements Camera.PreviewCallback {

        byte[][] data = new byte[20][];//存储相片的原始数据
        int i = 0;

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (saveTag) {
                Log.e(TAG, "onPreviewFrame: " + "正在保存到内存第:" + i);
                this.data[i] = data;
                if (i == this.data.length - 1) {
                    save(camera, this.data, pic_name);
                    camera.setPreviewCallback(null); // 设置回调的类

                    saveTag = false;
                    i = 0;
                    this.data = null;
                }
                i++;
            }
        }

      private  String certifyPath;

        private void save(final Camera camera, final byte[][] data, final int tag) {
            if (tag == 1) {
                certifyPath = settings.getCertifyPath() + "one";
            } else if (tag == 2) {
                certifyPath = settings.getCertifyPath() + "two";
            } else if (tag == 3) {
                certifyPath = settings.getCertifyPath() + "three";
            } else if (tag == 4) {
                certifyPath = settings.getCertifyPath() + "four";
            }

            File onePathField = new File(certifyPath);
            if (!onePathField.exists()) {
                onePathField.mkdir();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (threadState) {
                        Camera.Size size = camera.getParameters().getPreviewSize();
                        try {
                            int tempNumber = 0;
                            String format = null;
                            for (int i = 0; i < data.length; i++) {
                                if (threadClose == 2) {
                                    return;
                                }

                                // 调用image.compressToJpeg（）将YUV格式图像数据data转为jpg格式
                                YuvImage image = new YuvImage(data[i], ImageFormat.NV21, size.width,
                                        size.height, null);
                                if (image != null) {

                              /*  ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, outstream);*/

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    boolean b = image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

                                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                    format = String.format("%03d", tempNumber);
                                    saveBitmap(bmp, certifyPath, format);
                                    format = String.format("%03d", tempNumber++);
                                    // outstream.flush();
                                    if (i == 19) {

                                        threadState = false;
                                    }
                                }
                            }
                            iPictureCallback2.photoPrepared2(tag, certifyPath);
                        } catch (Exception ex) {
                            Log.e("Sys", "Error:" + ex.getMessage());

                        }
                    }
                }
            }).start();

        }

    }

    /**
     * 增加回调
     *
     * @param iPictureCallback
     */
    public void addCallback2(IPictureCallback2 iPictureCallback) {
        this.iPictureCallback2 = iPictureCallback;
    }

    //新添加的保存到手机的方法
    private void saveBitmap(Bitmap bitmap, String path, String bitName) throws IOException {

        File file = new File(path + "/" + bitName + ".jpg");
        Log.e(TAG, "saveBitmap: " + file.toString());
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始预览相机内容
     */
    public void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {

            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
