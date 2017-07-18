package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;
import com.join.camera.IPictureCallback2;
import com.join.camera.IPictureCallback3;
import com.join.camera.IPictureCallback4;
import com.join.serialport.SerialPortCommand;
import com.join.service.Humidity;
import com.join.utils.Arithmetic;
import com.join.video.NewCamera;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * 开始检测
 */
public class StosteDetection1 extends Activity implements View.OnClickListener, ServiceConnection,
        Humidity.CommandCallback, IPictureCallback3, IPictureCallback2, SurfaceHolder.Callback {
    private String TAG = "jjjStosteDetection1";
    private AnimatedCircleLoadingView animatedCircleLoadingView;  //进度条
    private Button bu_return, bu_enter;
    private ImageView icon_1;
    private TextView humidity, title, abnormalShow_1, abnormalShow_2;
    private Humidity.HumidityBinder humidityBinder;
    private PercentLinearLayout camera_ll;
    private double[] arithmeticData;//算法的结果数据
    private double[] arithmeticData2;
    private double[] arithmeticData3;
    private double[] arithmeticData4;
    private double[] arithmeticData5;//算法的结果数据
    private String[] stosteDetectionData;

    private Arithmetic arithmetic;
    private boolean boolTag = true;//算法出结果就取消睡眠
    private int flag;//判断哪个activity标记
    private Intent intent;
    private boolean boolTag1 = true;  //如果离开页面停止调用相机
    private int state;//算法返回的状态
    private int state2;
    private int state3;
    private int state4;
    private int tag;
    private int affirmOne = 0;
    private boolean stateThread = true;//控制线程的状态
    private LinearLayout abnormalShow_layout;
    private Humidity humidityClass;

    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private NewCamera newCamera;
    private Camera mcamera;
    private NewCamera.MyHandler myHandler;
    private Message message;
    private boolean serialProt1, serialProt2, serialProt3, serialProt4;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        startLoading();
        startPercentMockThread();
        stosteDetectionData = this.getIntent().getStringArrayExtra("data");
        flag = getIntent().getFlags();
        mPreview = (SurfaceView) findViewById(R.id.surface_view);
        mPreview.setY(-175);
        mPreview.setX(-49);
        ViewGroup.LayoutParams layoutParams = mPreview.getLayoutParams();
        layoutParams.height = 850;
        layoutParams.width = 700;
        mPreview.setLayoutParams(layoutParams);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);


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
        newCamera.setStartPreview(mcamera, mHolder);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        stateThread = false;
        myHandler.sendEmptyMessage(2);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newCamera.releaseCamera();
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
                    bundle.putDoubleArray("arithmetic", arithmeticData5);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection2");
                    intent.addFlags(1);
                    startActivity(intent);
                } else if (flag == 2) {

                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", stosteDetectionData);
                    bundle.putDoubleArray("arithmetic", arithmeticData5);
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
        newCamera = new NewCamera();
        myHandler = newCamera.getMyHandler();
        mcamera = newCamera.getMcamera();
        newCamera.addCallback2(this);


    }

    @Override
    public void photoPrepared2(int tag, final String path) {
        if (tag == 1) {
            arithmetic.setiPictureCallback4(new IPictureCallback4() {
                @Override
                public String photoPrepared4() {
                    return path;
                }
            });
            boolean temp = arithmetic.getArithmetic();
            if (temp) {
                this.tag = tag;
            }
            humidityClass.sendCommand(SerialPortCommand.two);
            serialProt2 = true;

        }
        if (tag == 2) {
            arithmetic.setiPictureCallback4(new IPictureCallback4() {
                @Override
                public String photoPrepared4() {
                    return path;
                }
            });

            boolean temp = arithmetic.getArithmetic();

            if (temp) {
                this.tag = tag;
            }
            humidityClass.sendCommand(SerialPortCommand.three);
            serialProt3 = true;

        }
        if (tag == 3) {
            arithmetic.setiPictureCallback4(new IPictureCallback4() {
                @Override
                public String photoPrepared4() {
                    return path;
                }
            });
            boolean temp = arithmetic.getArithmetic();
            if (temp) {
                this.tag = tag;
            }
            humidityClass.sendCommand(SerialPortCommand.four);
            serialProt4 = true;

        }
        if (tag == 4) {
            arithmetic.setiPictureCallback4(new IPictureCallback4() {
                @Override
                public String photoPrepared4() {
                    return path;
                }
            });
            affirmOne = 1;
            newCamera.releaseCamera();
            humidityClass.sendCommand(SerialPortCommand.one);
            boolean temp = arithmetic.getArithmetic();
            if (temp) {
                this.tag = tag;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            humidityClass.sendCommand(SerialPortCommand.guan);

        }
    }

    @Override
    public void photoPrepared3(double[] arithmetic, int returnState) {
        Log.e(TAG, "photoPrepared3: returnState" + returnState);
        if (tag == 1) {
            this.arithmeticData = arithmetic;
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state = returnState;
            // state = 1;
        } else if (tag == 2) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            this.arithmeticData2 = arithmetic;
            state2 = returnState;
        } else if (tag == 3) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state3 = returnState;
            this.arithmeticData3 = arithmetic;
        } else if (tag == 4) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state4 = returnState;
            this.arithmeticData4 = arithmetic;
            int i = 0;
            if (state == 1) {
                i++;
            }
            if (state2 == 1) {
                i++;
            }
            if (state3 == 1) {
                i++;
            }
            if (state4 == 1) {
                i++;
            }
            Log.e(TAG, "photoPrepared3: " + i);
            double ar1 = (arithmeticData[0] + arithmeticData2[0] + arithmeticData3[0] + arithmeticData4[0]) / i;
            double ar2 = (arithmeticData[1] + arithmeticData2[1] + arithmeticData3[1] + arithmeticData4[1]) / i;
            double ar3 = arithmeticData[2] + arithmeticData2[2] + arithmeticData3[2] + arithmeticData4[2] / i;
            double ar4 = arithmeticData[3] + arithmeticData2[3] + arithmeticData3[3] + arithmeticData4[3] / i;
            double ar5 = arithmeticData[4] + arithmeticData2[4] + arithmeticData3[4] + arithmeticData4[4] / i;
            double ar6 = arithmeticData[5] + arithmeticData2[5] + arithmeticData3[5] + arithmeticData4[5] / i;
            double ar7 = arithmeticData[7] + arithmeticData2[7] + arithmeticData3[7] + arithmeticData4[7] / i;
            arithmeticData5 = new double[]{
                    ar1, ar2, ar3, ar4, ar5, ar6, 0.0, ar7
            };
            boolTag = false;


        }
    }


    @Override
    public void onCommandResult(int data) {
        Log.e(TAG, "onCommandResult: " + data);
        if (serialProt1) {
            if (data != 0) {
                humidityClass.sendCommand(SerialPortCommand.one);

                serialProt1 = false;
            }
            serialProt1 = false;
        }
        if (serialProt2) {
            if (data != 1) {
                humidityClass.sendCommand(SerialPortCommand.two);
                Log.e(TAG, "onCommandResult: " + "tttttttttttttttttttttttttttttttttttttttttt" + 1);
                serialProt2 = false;
            }
            serialProt2 = false;
        }
        if (serialProt3) {
            if (data != 2) {
                humidityClass.sendCommand(SerialPortCommand.three);
                Log.e(TAG, "onCommandResult: " + "tttttttttttttttttttttttttttttttttttttttttt" + 2);
                serialProt3 = false;
            }
            serialProt3 = false;
        }
        if (serialProt4) {
            if (data != 3) {
                humidityClass.sendCommand(SerialPortCommand.four);
                Log.e(TAG, "onCommandResult: " + "tttttttttttttttttttttttttttttttttttttttttt" + 3);
                serialProt4 = false;
            }
            serialProt4 = false;
        }
        if (data == 0 && affirmOne == 0) {
            message = new Message();
            message.what = 1;
            message.arg1 = 1;
            myHandler.sendMessageDelayed(message, 4000);
        }
        if (data == 1) {
            message = new Message();
            message.what = 1;
            message.arg1 = 2;
            myHandler.sendMessageDelayed(message, 3000);
        }
        if (data == 2) {
            message = new Message();
            message.what = 1;
            message.arg1 = 3;
            myHandler.sendMessageDelayed(message, 3000);
        }
        if (data == 3) {
            message = new Message();
            message.what = 1;
            message.arg1 = 4;
            myHandler.sendMessageDelayed(message, 3000);
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        humidityClass = humidityBinder.getHumidity();
        humidityClass.setCommandCallback(this);
        if (humidityClass != null) {
            humidityClass.sendCommand(SerialPortCommand.one);
            serialProt1 = true;
        }
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText(data);
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
                        for (int i = 0; i <= 100; i++) {
                            if (boolTag) {
                                Thread.sleep(3000);
                            }
                            if (!boolTag) {

                                if (state == -1 && state2 == -1 && state3 == -1 && state4 == -1) {
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
                                } else if (state == -6 && state2 == -6 && state3 == -6 && state4 == -6) {
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
                                } else if (state == -5 && state2 == -5 && state3 == -5 && state4 == -5) {
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
                                } else if (state == -4 && state2 == -4 && state3 == -4 && state4 == -4) {
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
                                } else if (state == -3 && state2 == -3 && state3 == -3 && state4 == -3) {
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
                                } else if (state == -2 && state2 == -2 && state3 == -2 && state4 == -2) {
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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        newCamera.setStartPreview(mcamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        newCamera.setStartPreview(mcamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        newCamera.releaseCamera();
    }

}
