package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;
import com.join.service.Humidity;

/**
 * Created by join on 2017/5/14.
 */

public class StosteDetection1 extends Activity implements View.OnClickListener , ServiceConnection {
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private Button bu_return, bu_enter;
    private ImageView icon_1;
    String[] StosteDetectionData;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
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
                intent2.setAction("com.join.stostedetection2");
                startActivity(intent2);
                break;
            case R.id.bu_return:
                finish();
                break;
        }
    }

    private void init() {
        humidity = (TextView) findViewById(R.id.humidity);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        bu_enter = (Button) findViewById(R.id.bu_enter);
        bu_enter.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        startLoading();
        startPercentMockThread();
        StosteDetectionData = this.getIntent().getStringArrayExtra("data");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);
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
                Log.e("jjjj", data + "");
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
    private boolean addToDAO() {
        if (StosteDetectionData.length > 0) {
            String color = StosteDetectionData[0];
            String smell = StosteDetectionData[1];
            String dateC = StosteDetectionData[2];
            String timeC = StosteDetectionData[3];
            String number = StosteDetectionData[4];
            String operator = IDSelect.id_manage;
        }
        return true;
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
                        Thread.sleep(20);
                        changePercent(i);
                        Log.e("jjjj", "jjjj");
                        if (i == 100) {
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
                    Log.e("jjjj", "jjjj");
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
