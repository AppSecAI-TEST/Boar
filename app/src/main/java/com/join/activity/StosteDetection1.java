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
import android.util.Log;
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
import com.join.camera.IPictureCallback4;
import com.join.camera.MsgCons;
import com.join.service.Humidity;
import com.join.utils.Arithmetic;
import com.join.utils.CustomToast;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * 开始检测
 */
public class StosteDetection1 extends Activity implements View.OnClickListener, ServiceConnection, IPictureCallback2, IPictureCallback3 {
    private String TAG = "jjjStosteDetection1";
    private AnimatedCircleLoadingView animatedCircleLoadingView;  //进度条
    private Button bu_return, bu_enter;
    private ImageView icon_1;
    private TextView humidity, title;
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
    private double[] arithmeticData;
    private Arithmetic arithmetic;
    private boolean boolTag = true;
    private int flag;
    private Intent intent;
    private boolean boolTag1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        initParam();
        startLoading();
        startPercentMockThread();

        stosteDetectionData = this.getIntent().getStringArrayExtra("data");
        flag = getIntent().getFlags();
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
    public void photoPrepared(int tag, final String path) {
        handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 1000L);
        if (tag == 14) {
            arithmetic.setiPictureCallback4(new IPictureCallback4() {
                @Override
                public String photoPrepared4() {
                    return path;
                }
            });
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
    public void photoPrepared3(double[] arithmetic, int returnState) {
        this.arithmeticData = arithmetic;
        boolTag = false;
        state = returnState;

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
        Boolean isPrepared = false; //标记是否拍照完毕

        MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JUMP_FRAGMENT:
                    if (count == 15) {
                        isPrepared = true;
                        removeMessages(MsgCons.CAMERA_TIMEOUT);
                        mCameraManager.delCallback();
                    } else {
                        if (boolTag1) {
                            takePicture();
                            count++;
                        }
                    }
                    break;
                case TAKE_PHOTO:
                    if (boolTag1) {
                        takePicture();
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                boolTag1 = false;
                intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.bu_enter:
                if (flag == 1) {
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", stosteDetectionData);
                    bundle.putDoubleArray("arithmetic", arithmeticData);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection2");
                    intent.addFlags(1);
                    startActivity(intent);
                } else if (flag == 2) {
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", stosteDetectionData);
                    bundle.putDoubleArray("arithmetic", arithmeticData);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection22");
                    intent.addFlags(1);
                    startActivity(intent);
                }
                break;
            case R.id.bu_return:
                boolTag1 = false;
                finish();
                break;
        }
    }

    private void init() {
        title = (TextView) findViewById(R.id.title);
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
        intent = new Intent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);
        arithmetic.setiPictureCallback3(this);
        if (flag == 2) {
            title.setText("稀释精液检测");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        closeCamera();
        stateThread = false;
        finish();
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

    private int state;
    private boolean stateThread = true;

    private void startPercentMockThread() {

        Runnable runnable = new Runnable() {
            boolean progress = true;

            @Override
            public void run() {
                while (stateThread) {
                    Log.e(TAG, "run: " + "jjjjjjjjjjjjjj");
                    try {
                        // Thread.sleep(1500);
                        for (int i = 0; i <= 100; i++) {
                            if (boolTag) {
                                Thread.sleep(1500);
                            }

                            if (!boolTag) {
                                if (state == -1) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    CustomToast.showToast(StosteDetection1.this, "您输入参数不对,请从新输入..");
                                    break;
                                } else if (state == -6) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    stateThread = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomToast.showToast(StosteDetection1.this, "样本图像背景异常,请从新检测..");

                                        }
                                    });
                                    break;
                                } else if (state == -5) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    stateThread = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomToast.showToast(StosteDetection1.this, "样本异常，活动精子数太少");
                                        }
                                    });
                                    break;
                                } else if (state == -4) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomToast.showToast(StosteDetection1.this, "样本图像张数不够");
                                        }
                                    });
                                    break;
                                } else if (state == -3) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomToast.showToast(StosteDetection1.this, "未找到图像或图像文件名不规范");
                                        }
                                    });
                                    break;
                                } else if (state == -2) {
                                    animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomToast.showToast(StosteDetection1.this, "数据写入异常");
                                        }
                                    });
                                    break;
                                }
                            }

                            if (progress) {
                                changePercent(i);
                                if (i == 100) {
                                    Thread.sleep(4000);
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


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
