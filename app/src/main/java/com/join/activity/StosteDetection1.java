package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;
import com.join.camera.CertifyCameraManager;
import com.join.camera.IPictureCallback2;
import com.join.camera.IPictureCallback3;
import com.join.camera.MsgCons;
import com.join.service.Humidity;
import com.join.utils.Arithmetic;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * 开始检测
 */
public class StosteDetection1 extends Activity implements View.OnClickListener, ServiceConnection, IPictureCallback2, IPictureCallback3 {
    private String TAG = "jjjStosteDetection1";
    private AnimatedCircleLoadingView animatedCircleLoadingView;  //进度条
    private Button bu_return, bu_enter;
    private ImageView icon_1;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private String[] stosteDetectionData;
    private PercentLinearLayout camera_ll;
    private CameraSurfaceView cameraSurfaceView = null;
    private Camera camera;
    private CertifyCameraManager mCameraManager;
    private SurfaceHolder holder = null;
    public static final int TAKE_PHOTO = 1;//第一次照相
    public static final int JUMP_FRAGMENT = 4;//之后的照相 照完之后回调通知再次照相
    private int count = 1; //控制照相的张数
    private MyHandler handler = new MyHandler();
    private float[] arithmeticData;
    private Arithmetic arithmetic;
    private boolean boolTag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        initParam();
        startLoading();
        startPercentMockThread();

        stosteDetectionData = this.getIntent().getStringArrayExtra("data");

    }

    /**
     * 初始化相机,增加回调
     */
    private void initParam() {
        mCameraManager = new CertifyCameraManager();
        camera = mCameraManager.getCamera(this);
        mCameraManager.addCallback2(this);
        if (camera == null) {

        } else {
            initSurafceView();
            handler.sendEmptyMessageDelayed(MsgCons.CAMERA_TIMEOUT, 15000);
        }
    }

    private void takePicture() {
        mCameraManager.takePhoto();
    }


    @Override
    public void photoPrepared(int tag) {
        handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 1000L);
        if (tag == 9) {
            arithmetic.getArithmetic();
        }
    }


    /**
     * 增加相机到布局
     */
    private void initSurafceView() {
        camera_ll.removeAllViews();
        cameraSurfaceView = new CameraSurfaceView(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        camera_ll.addView(cameraSurfaceView, params);

        mCameraManager.initSurfaceView(cameraSurfaceView);
    }

    @Override
    public void photoPrepared3(float[] arithmetic) {
        this.arithmeticData = arithmetic;
        boolTag = false;

    }


    class CameraSurfaceView extends SurfaceView {

        public CameraSurfaceView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(surfaceCallback);
        }
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            if (mCameraManager != null) {
                mCameraManager.startPreview(holder);
            }
            handler.sendEmptyMessageDelayed(TAKE_PHOTO, 200L);
            //已经预览的时候通知照相
            // handler.sendEmptyMessage(TAKE_PHOTO);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        if (mCameraManager != null) {
            mCameraManager.closeCamera();
            mCameraManager.delCallback();
            mCameraManager = null;
        }
        if (holder != null) {
            holder.removeCallback(surfaceCallback);
            holder = null;
        }
    }

    private class MyHandler extends Handler {
        Boolean isPrepared = false;

        MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JUMP_FRAGMENT:
                    if (count == 10) {
                        isPrepared = true;
                        removeMessages(MsgCons.CAMERA_TIMEOUT);
                        mCameraManager.delCallback();
                    } else {
                        takePicture();
                        count++;
                    }
                    break;
                case TAKE_PHOTO:
                    takePicture();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.bu_enter:
                Intent intent2 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArray("data", stosteDetectionData);
                bundle.putFloatArray("arithmetic", arithmeticData);
                intent2.putExtras(bundle);
                intent2.setAction("com.join.stostedetection2");
                intent2.addFlags(1);
                startActivity(intent2);
                break;
            case R.id.bu_return:
                finish();
                break;
        }
    }

    private void init() {
        camera_ll = (PercentLinearLayout) findViewById(R.id.camera_ll);
        humidity = (TextView) findViewById(R.id.humidity);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        bu_enter = (Button) findViewById(R.id.bu_enter);
        bu_enter.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        arithmetic = new Arithmetic();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);
        arithmetic.setiPictureCallback3(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        Humidity humidityClass = humidityBinder.getHumidity();
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final int data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText("" + data);
                    }
                });
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    /**
     * 加载动漫
     */
    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    /**
     * 改变进度的线程
     */
    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Thread.sleep(1500);
                    for (int i = 0; i <= 100; i++) {
                        if (boolTag) {
                            Thread.sleep(1500);
                        }
                        changePercent(i);

                        if (i == 100) {
                            Thread.sleep(5000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    animatedCircleLoadingView.setVisibility(View.GONE);
                                    bu_return.setVisibility(View.GONE);
                                    bu_enter.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 改变进度条的值,必须要在UI线程
     *
     * @param percent
     */
    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    /**
     * 重置进度
     */
    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }


}
