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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;
import com.join.camera.CertifyCameraManager;
import com.join.camera.IPictureCallback2;
import com.join.camera.IPictureCallback3;
import com.join.camera.IPictureCallback4;
import com.join.camera.MsgCons;
import com.join.serialport.SerialPortCommand;
import com.join.service.Humidity;
import com.join.utils.Arithmetic;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * 开始检测
 */
public class StosteDetection1 extends Activity implements View.OnClickListener, ServiceConnection,
        IPictureCallback2, IPictureCallback3, Humidity.CommandCallback {
    private String TAG = "jjjStosteDetection1";
    private AnimatedCircleLoadingView animatedCircleLoadingView;  //进度条
    private Button bu_return, bu_enter;
    private ImageView icon_1;
    private TextView humidity, title, abnormalShow_1, abnormalShow_2;
    private Humidity.HumidityBinder humidityBinder;
    private PercentLinearLayout camera_ll;
    private CameraSurfaceView cameraSurfaceView = null;
    private Camera camera;
    private CertifyCameraManager mCameraManager;
    private SurfaceHolder holder = null;
    public static final int TAKE_PHOTO = 1;//第一次照相
    public static final int JUMP_FRAGMENT = 4;//之后的照相 照完之后回调通知再次照相
    private int count = 1; //控制照相的张数
    private MyHandler handler = new MyHandler();
    private double[] arithmeticData;//算法的结果数据
    private String[] stosteDetectionData;
    private Arithmetic arithmetic;
    private boolean boolTag = true;//算法出结果就取消睡眠
    private int flag;//判断哪个activity标记
    private Intent intent;
    private boolean boolTag1 = true;  //如果离开页面停止调用相机
    private int state;//算法返回的状态
    private boolean stateThread = true;//控制线程的状态
    private LinearLayout abnormalShow_layout;
    private Humidity humidityClass;
    private int commandState;

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
        if (mCameraManager != null) {
           /* final boolean isSupportAutoFocus = getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_AUTOFOCUS);//是否支持自动对焦*/

            //自动对焦
            Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

                public void onAutoFocus(boolean success, Camera camera) {
                    Log.e(TAG, "onAutoFocus: " + success);
                    if (success) {
                        Log.i(TAG, "autoFocusCallback: success...");
                        mCameraManager.takePhoto();
                    } else {
                        Log.i(TAG, "autoFocusCallback: fail...");

                        mCameraManager.takePhoto();

                    }
                }
            };
            camera.autoFocus(autoFocusCallback);

        }
    }


    @Override
    public void photoPrepared(int tag, final String path) {
        if (commandState == 00) {
            handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 800L);
            if (tag == 5) {
                humidityClass.sendCommand(SerialPortCommand.two);
            }
        }

        if (commandState == 1) {
            handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 800L);
            if (tag == 10) {
                humidityClass.sendCommand(SerialPortCommand.three);

            }
        }
        if (commandState == 2) {
            Log.e(TAG, "photoPrepared: " + commandState + "1000L");
            handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 800L);
            if (tag == 15) {
                humidityClass.sendCommand(SerialPortCommand.four);

            }
        }
        if (commandState == 3) {
            handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 800L);
            if (tag == 20) {

                arithmetic.setiPictureCallback4(new IPictureCallback4() {
                    @Override
                    public String photoPrepared4() {
                        return path;
                    }
                });

                arithmetic.getArithmetic();
            }
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

    @Override
    public void onCommandResult(int data) {
        commandState = data;
        Log.e(TAG, "onCommandResult: " + commandState);
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

            handler.sendEmptyMessageDelayed(TAKE_PHOTO, 2800L);
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
                    if (count == 21) {
                        isPrepared = true;
                        removeMessages(MsgCons.CAMERA_TIMEOUT);
                        mCameraManager.delCallback();
                        closeCamera();
                        humidityClass.sendCommand(SerialPortCommand.aone);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        humidityClass.sendCommand(SerialPortCommand.guan);
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
                    double v1 = arithmeticData[1];
                    Log.e(TAG, "onClick: " + v1);
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
        abnormalShow_layout = (LinearLayout) findViewById(R.id.abnormalShow_layout);
        abnormalShow_1 = (TextView) findViewById(R.id.abnormalShow_1);
        abnormalShow_2 = (TextView) findViewById(R.id.abnormalShow_2);
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
        humidityClass = humidityBinder.getHumidity();
        humidityClass.setCommandCallback(this);
        if (humidityClass != null) {
            humidityClass.sendCommand(SerialPortCommand.aone);
        }
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText( data);
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
            boolean progress = true;

            @Override
            public void run() {
                while (stateThread) {

                    try {
                        // Thread.sleep(1500);
                        for (int i = 0; i <= 100; i++) {
                            if (boolTag) {
                                Thread.sleep(1500);
                            }
                            if (!boolTag) {

                                if (state == -1) {
                                    //  animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("参数不对,导致结果异常.");
                                            abnormalShow_2.setText("请重新返回检测!");

                                        }
                                    });
                                    break;
                                } else if (state == -6) {
                                    //  animatedCircleLoadingView.stopFailure();  bu_enter.setVisibility(View.VISIBLE);

                                    progress = false;
                                    stateThread = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("样本图像背景异常,导致结果异常.");
                                            abnormalShow_2.setText("请重新返回检测!");
                                        }
                                    });
                                    break;
                                } else if (state == -5) {
                                    // animatedCircleLoadingView.stopFailure();//显示不对的图像

                                    progress = false;
                                    stateThread = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("活动精子数太少,导致结果异常.");
                                            abnormalShow_2.setText("请重新返回检测!");

                                        }
                                    });
                                    break;
                                } else if (state == -4) {
                                    //  animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("样本图像张数不够,导致结果异常.");
                                            abnormalShow_2.setText("请重新返回检测!");
                                        }
                                    });
                                    break;
                                } else if (state == -3) {
                                    // animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("未找到图像或图像文件名不规范.");
                                            abnormalShow_2.setText("请重新返回检测!");
                                        }
                                    });
                                    break;
                                } else if (state == -2) {
                                    // animatedCircleLoadingView.stopFailure();
                                    progress = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            animatedCircleLoadingView.setVisibility(View.GONE);
                                            abnormalShow_layout.setVisibility(View.VISIBLE);
                                            abnormalShow_1.setText("数据写入异常");
                                            abnormalShow_2.setText("请重新返回检测!");
                                        }
                                    });
                                    break;
                                }
                            }

                            //  if (progress) {
                            if (true) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                        stateThread = false;
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
