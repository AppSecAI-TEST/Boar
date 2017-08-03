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
    private double[] arithmeticData5;//算法的结果数据
    private String[] stosteDetectionData;//上一个activity传过来的参数
    private int[] windowSelect;
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
    private boolean Prot1 = true, Prot2 = true, Prot3 = true, Prot4 = true;//保证在命令的回调中只调用一次
    private int winTag;//标记开始检测的窗口位置
    private int helpCount = 0;//标记本次需要的检测的数量;
    private int firstTag1 = -1, firstTag2 = -1, firstTag3 = -1, firstTag4 = -1;//标记哪个玻片被使用
    private double arg1, arg2, arg3, arg4, arg5, arg6, arg7;//临时存储算法的变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        startLoading();
        startPercentMockThread();
        stosteDetectionData = this.getIntent().getStringArrayExtra("data");
        windowSelect = this.getIntent().getIntArrayExtra("windowSelect");

        for (int i = 0; i < windowSelect.length; i++) {
            if (windowSelect[i] != 0) {
                winTag = i;

                break;
            }
        }

        for (int i = 0; i < windowSelect.length; i++) {
            if (windowSelect[i] != 0) {
                if (i == 0) {
                    firstTag1 = i;

                }
                if (i == 1) {
                    firstTag2 = i;
                }
                if (i == 2) {
                    firstTag3 = i;
                }
                if (i == 3) {
                    firstTag4 = i;
                }
            }
        }
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
   /*     Intent intentHumidity = new Intent(this, Humidity.class);
        intentHumidity.setFlags(1);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
        arithmetic.setiPictureCallback3(this);
        if (flag == 2) {
            title.setText("稀释精液检测");
        }
        newCamera.setStartPreview(mcamera, mHolder);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  unbindService(this);
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
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.bu_enter:
                if (flag == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", stosteDetectionData);
                    bundle.putDoubleArray("arithmetic", arithmeticData5);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection2");
                    intent.addFlags(1);
                    startActivity(intent);
                } else if (flag == 2) {
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
            if (windowSelect[1] != 0) {
                humidityClass.sendCommand(SerialPortCommand.two);
            } else if (windowSelect[2] != 0) {
                humidityClass.sendCommand(SerialPortCommand.three);
            } else if (windowSelect[3] != 0) {
                humidityClass.sendCommand(SerialPortCommand.four);
            }


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
            if (windowSelect[2] != 0) {
                humidityClass.sendCommand(SerialPortCommand.three);
            } else if (windowSelect[3] != 0) {
                humidityClass.sendCommand(SerialPortCommand.four);
            }


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
            if (windowSelect[3] != 0) {
                humidityClass.sendCommand(SerialPortCommand.four);
            }

        }
        if (tag == 4) {
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


        }
    }


    @Override
    public void photoPrepared3(double[] arithmetic, int returnState) {
        Log.e(TAG, "photoPrepared3: returnState" + returnState);
        if (tag == 1) {
            state = returnState;
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            if (returnState == 1) {
                helpCount++;
                arg1 = arithmetic[0];
                arg2 = arithmetic[1];
                arg3 = arithmetic[2];
                arg4 = arithmetic[3];
                arg5 = arithmetic[4];
                arg6 = arithmetic[5];
                arg7 = arithmetic[7];

            }
            if (firstTag2 == -1 && firstTag3 == -1 && firstTag4 == -1) {
                arithmeticData5 = new double[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7};
                boolTag = false;
                humidityClass.sendCommand(SerialPortCommand.one);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                humidityClass.sendCommand(SerialPortCommand.guan);
            }

        } else if (tag == 2) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state2 = returnState;
            if (returnState == 1) {
                helpCount++;
                arg1 = (arg1 + arithmetic[0]) / helpCount;
                arg2 = (arg2 + arithmetic[1]) / helpCount;
                arg3 = (arg3 + arithmetic[2]) / helpCount;
                arg4 = (arg4 + arithmetic[3]) / helpCount;
                arg5 = (arg5 + arithmetic[4]) / helpCount;
                arg6 = (arg6 + arithmetic[5]) / helpCount;
                arg7 = (arg7 + arithmetic[7]) / helpCount;


            }
            if (firstTag3 == -1 && firstTag4 == -1) {
                arithmeticData5 = new double[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7};
                boolTag = false;
                affirmOne = 1;
                newCamera.releaseCamera();
                humidityClass.sendCommand(SerialPortCommand.one);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                humidityClass.sendCommand(SerialPortCommand.guan);
            }
        } else if (tag == 3) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state3 = returnState;
            if (returnState == 1) {
                helpCount++;
                arg1 = (arg1 + arithmetic[0]) / helpCount;
                arg2 = (arg2 + arithmetic[1]) / helpCount;
                arg3 = (arg3 + arithmetic[2]) / helpCount;
                arg4 = (arg4 + arithmetic[3]) / helpCount;
                arg5 = (arg5 + arithmetic[4]) / helpCount;
                arg6 = (arg6 + arithmetic[5]) / helpCount;
                arg7 = (arg7 + arithmetic[7]) / helpCount;

            }
            if (firstTag4 == -1) {
                arithmeticData5 = new double[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7};
                boolTag = false;
                affirmOne = 1;
                newCamera.releaseCamera();
                humidityClass.sendCommand(SerialPortCommand.one);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                humidityClass.sendCommand(SerialPortCommand.guan);
            }

        } else if (tag == 4) {
            Log.e(TAG, "photoPrepared3: " + "tag" + tag + "\n" + arithmetic[0]);
            state4 = returnState;

            if (returnState == 1) {
                helpCount++;
                arg1 = (arg1 + arithmetic[0]) / helpCount;
                arg2 = (arg2 + arithmetic[1]) / helpCount;
                arg3 = (arg3 + arithmetic[2]) / helpCount;
                arg4 = (arg4 + arithmetic[3]) / helpCount;
                arg5 = (arg5 + arithmetic[4]) / helpCount;
                arg6 = (arg6 + arithmetic[5]) / helpCount;
                arg7 = (arg7 + arithmetic[7]) / helpCount;

            }
            arithmeticData5 = new double[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7};
            boolTag = false;
            affirmOne = 1;
            newCamera.releaseCamera();
            humidityClass.sendCommand(SerialPortCommand.one);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            humidityClass.sendCommand(SerialPortCommand.guan);

        }
    }


    @Override
    public void onCommandResult(int data) {
        Log.e(TAG, "onCommandResult: " + data);

        if (windowSelect[0] != 0) {
            if (Prot1) {
                if (data == 0 && affirmOne == 0) {//data等于0的时候利用handler通知去取相片,并且不是复位的命令  arg1区别是取玻片上的那个窗口的图片
                    Log.e(TAG, "onCommandResult: " + "进入2");
                    message = new Message();
                    message.what = 1;
                    message.arg1 = 1;
                    myHandler.sendMessageDelayed(message, 3000);
                    Prot1 = false;
                }
            }
        }
        if (windowSelect[1] != 0) {
            if (Prot2) {
                if (data == 1) {//data等于1的时候利用handler通知去取相片,
                    Log.e(TAG, "onCommandResult: " + data + "标记" + 3);
                    message = new Message();
                    message.what = 1;
                    message.arg1 = 2;
                    myHandler.sendMessageDelayed(message, 3000);
                    Prot2 = false;
                }
            }
        }
        if (windowSelect[2] != 0) {
            if (Prot3) {
                if (data == 2) {
                    message = new Message();
                    message.what = 1;
                    message.arg1 = 3;
                    myHandler.sendMessageDelayed(message, 3000);
                    Prot3 = false;
                }
            }
        }
        if (windowSelect[3] != 0) {
            if (Prot4) {
                if (data == 3) {
                    message = new Message();
                    message.what = 1;
                    message.arg1 = 4;
                    myHandler.sendMessageDelayed(message, 3000);
                    Prot4 = false;
                }
            }
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        humidityClass = humidityBinder.getHumidity();
        humidityClass.setCommandCallback(this);
        if (winTag == 0) {
            humidityClass.sendCommand(SerialPortCommand.one);
        } else if (winTag == 1) {
            humidityClass.sendCommand(SerialPortCommand.two);
        } else if (winTag == 2) {
            humidityClass.sendCommand(SerialPortCommand.three);
        } else if (winTag == 3) {
            humidityClass.sendCommand(SerialPortCommand.four);
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
