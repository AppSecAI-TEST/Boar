package com.join.camera;

/**
 * Created by join on 2017/3/7.
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * 005.
 *
 * @author zw.yan
 * <p>
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 * <p>
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 */

/**
 * sufaceView 的预览类，其中SurfaceHolder.CallBack用来监听Surface的变化，
 * 当Surface发生改变的时候自动调用该回调方法
 * 通过调用方SurfaceHolder.addCallBack来绑定该方法
 * @author zw.yan
 *
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.join.app.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private Handler handler;
    private int mPictureCount = 20;//默认自动拍一张
    private final int mPictureCountMax = 30;//默认自动拍一张
    private int pic_count = 0;
    private String TAG = "CameraPreview";
    /**
     * Surface的控制器，用来控制预览等操作
     */
    private SurfaceHolder mHolder;
    /**
     * 相机实例
     */
    private Camera mCamera = null;
    /**
     * 图片处理
     */
    public static final int MEDIA_TYPE_IMAGE = 1;
    /**
     * 预览状态标志
     */
    private boolean isPreview = false;
    /**
     * 设置一个固定的最大尺寸
     */
    private int maxPictureSize = 5000000;
    /**
     * 是否支持自动聚焦，默认不支持
     */
    private Boolean isSupportAutoFocus = false;
    /**
     * 获取当前的context
     */
    private Context mContext;
    /**
     * 当前传感器的方向，当方向发生改变的时候能够自动从传感器管理类接受通知的辅助类
     */
    MyOrientationDetector cameraOrientation;
    /**
     * 设置最适合当前手机的图片宽度
     */
    int setFixPictureWidth = 0;
    /**
     * 设置当前最适合的图片高度
     */
    int setFixPictureHeight = 0;

    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Handler handler) {
        super(context);
        this.mContext = context;
        this.handler = handler;
        isSupportAutoFocus = context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
        mHolder = getHolder();
        //兼容android 3.0以下的API，如果超过3.0则不需要设置该方法
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mHolder.addCallback(this);//绑定当前的回调方法
    }

    /**
     * 创建的时候自动调用该方法
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = CameraCheck.getCameraInstance(mContext);
        }
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            if (null != mCamera) {
                //释放相机
                mCamera.release();
                mCamera = null;
                isPreview = false;
            }
            e.printStackTrace();
        }
    }

    /**
     * 当surface的大小发生改变的时候自动调用的
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            setCameraParms();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            // reAutoFocus();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private void setCameraParms() {
        Camera.Parameters myParam = mCamera.getParameters();
        List<Camera.Size> mSupportedsizeList = myParam.getSupportedPictureSizes();
        if (mSupportedsizeList.size() > 1) {
            Iterator<Camera.Size> itos = mSupportedsizeList.iterator();
            while (itos.hasNext()) {
                Camera.Size curSize = itos.next();
                int curSupporSize = curSize.width * curSize.height;
                int fixPictrueSize = setFixPictureWidth * setFixPictureHeight;
                if (curSupporSize > fixPictrueSize && curSupporSize <= maxPictureSize) {
                    setFixPictureWidth = curSize.width;
                    setFixPictureHeight = curSize.height;
                }
            }
    }
        myParam.setPictureFormat(ImageFormat.JPEG);
        int pictureFormat = myParam.getPictureFormat();
        Log.e("jjjj",""+pictureFormat);
        myParam.setPreviewFrameRate(30);
        int previewFrameRate = myParam.getPreviewFrameRate();
        Log.e("jjjj",""+previewFrameRate);
        myParam.setPictureSize(1920, 1080);
        myParam.setJpegQuality(100);
        mCamera.setParameters(myParam);
        if (myParam.getMaxNumDetectedFaces() > 0) {
            mCamera.startFaceDetection();
        }
    }

    /**
     * 在surface销毁时候调用，这里一般对资源进行释放；
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    /**
     * 相机自动对焦
     */
    public void reAutoFocus() {
        if (isSupportAutoFocus) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
        }
    }

    /**
     * 自动聚焦，然后拍照
     */
    public void takePicture() {
        if (mCamera != null) {
            // mCamera.autoFocus(autoFocusCallback);
            mCamera.takePicture(shutterCallback, pictureCallback, mPicture);
        }
    }

    //自动对焦
    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        public void onAutoFocus(boolean success, Camera camera) {

            if (success) {
                Log.i(TAG, "autoFocusCallback: success...");
                takePhoto();
            } else {
                Log.i(TAG, "autoFocusCallback: fail...");
                if (isSupportAutoFocus) {
                    takePhoto();
                }
            }
        }
    };

    /**
     * 调整照相的方向，设置拍照相片的方向
     */
    public void takePhoto() {
        cameraOrientation = new MyOrientationDetector(mContext);
        if (mCamera != null) {
            int orientation = cameraOrientation.getOrientation();
            Camera.Parameters cameraParameter = mCamera.getParameters();
            cameraParameter.setRotation(90);
            cameraParameter.set("rotation", 90);
            if ((orientation >= 45) && (orientation < 135)) {
                cameraParameter.setRotation(180);
                cameraParameter.set("rotation", 180);
            }
            if ((orientation >= 135) && (orientation < 225)) {
                cameraParameter.setRotation(270);
                cameraParameter.set("rotation", 270);
            }
            if ((orientation >= 225) && (orientation < 315)) {
                cameraParameter.setRotation(0);
                cameraParameter.set("rotation", 0);
            }
            mCamera.setParameters(cameraParameter);
            mCamera.takePicture(shutterCallback, pictureCallback, mPicture);
        }
    }

    //快门回调
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

        }
    };
    /**
     * 拍照之后返回的图片字节
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
   /*         new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Message message = new Message();
                    message.arg1 = 1;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            }).start();*/
            new SavePictureTask().execute(data);
            mCamera.startPreview();//重新开始预览
        }
    };

    public class SavePictureTask extends AsyncTask<byte[], String, String> {
        FileOutputStream fos;

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(byte[]... params) {
            File pictureFile = FileUtil.getOutputMediaFile(MEDIA_TYPE_IMAGE,
                    mContext);
            if (pictureFile == null) {
                Toast.makeText(mContext, "请插入存储卡！", Toast.LENGTH_SHORT).show();
                return null;
            }
            try {


                fos = new FileOutputStream(pictureFile);
                fos.write(params[0]);


            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            } finally {

                if (fos != null) {
                    try {
                        if (mPictureCount > mPictureCountMax) {
                            // Toast.makeText(AutoTakePicturesActivity.this, "为了节约内存，连拍张数不要超过" + mPictureCountMax + "张", Toast.LENGTH_SHORT).show();
                        } else {
                            if (++pic_count < mPictureCount) {
                                //连拍三张

                                setStartPreview(mCamera, mHolder);
                                Log.e("jjjjj", "" + pic_count);
                              // SystemClock.sleep(1);
                            } else {

                                fos.close();
                                fos.flush();
                            }
                            //  Toast.makeText(AutoTakePicturesActivity.this, "图片保存成功" + pic_count + "张", Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


//刷新系统相册
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(pictureFile); //指定SD卡路径
            mediaScanIntent.setData(contentUri);
            BaseApplication.getContext().sendBroadcast(mediaScanIntent);

            return null;
        }
    }

    /**
     * 设置并且开启相机预览
     */
    public void setStartPreview(Camera camera, SurfaceHolder holder) {
       try {
            //将Camera与SurfaceView开始绑定
           camera.setPreviewDisplay(holder);
            //调整拍摄的方向（默认横屏）
            camera.setDisplayOrientation(90);//旋转90度
            //开启预览
           // camera.startPreview();
            camera.takePicture(null, null, mPicture);
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        reAutoFocus();
        return false;
    }


}
