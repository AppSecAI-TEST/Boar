package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.join.R;
import com.join.databinding.StosteDetectionDiluentBinding;
import com.join.entity.StosteDetectionDiluentE;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.Keyboard2;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.text.SimpleDateFormat;

/**
 * 稀释精液检测参数设置页面
 */

public class StosteDetectionDiluent extends Activity implements View.OnClickListener, ServiceConnection {
    private Keyboard2 keyboard;
    private PercentLinearLayout linearLayout;
    private StosteDetectionDiluentE diluentE;
    private Button normal_1, abnormal_1, normal_2, abnormal_2, start;
    private Humidity.HumidityBinder humidityBinder;
    private boolean normal_1_tab = true;
    private boolean abnormal_1_tab = true;


    private boolean normal_2_tab = true;
    private boolean abnormal_2_tab = true;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StosteDetectionDiluentBinding binding = DataBindingUtil.setContentView(this, R.layout.stoste_detection_diluent);

        linearLayout = (PercentLinearLayout) findViewById(R.id.id_Gong);
        diluentE = new StosteDetectionDiluentE();
        binding.setDiluent(this);
        binding.setDiluentE(diluentE);
        init();
    }

    private void init() {

        normal_1 = (Button) findViewById(R.id.normal_1);
        normal_1.setOnClickListener(this);

        abnormal_1 = (Button) findViewById(R.id.abnormal_1);
        abnormal_1.setOnClickListener(this);

        normal_2 = (Button) findViewById(R.id.normal_2);
        normal_2.setOnClickListener(this);

        abnormal_2 = (Button) findViewById(R.id.abnormal_2);
        abnormal_2.setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        intent = new Intent();
        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = sdate.format(new java.util.Date());
        diluentE.setDate(dateFormat);

        SimpleDateFormat stime = new SimpleDateFormat("hh:mm");
        String timeFormat = stime.format(new java.util.Date());
        diluentE.setTime(timeFormat);

    }

    public void showDialog(View view) {
        keyboard = new Keyboard2(StosteDetectionDiluent.this, diluentE, 1);
        keyboard.showWindow(linearLayout);
    }

    public void showDialog2(View view) {
        keyboard = new Keyboard2(StosteDetectionDiluent.this, diluentE, 2);
        keyboard.showWindow(linearLayout);
    }

    public void startActivityC(View view) {
        intent.setAction("com.join.function");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_1:
                if (normal_1_tab) {
                    abnormal_1.setBackgroundResource(R.drawable.a_020);
                    abnormal_1_tab = true;
                    normal_1.setBackgroundResource(R.drawable.a_019);
                    normal_1_tab = false;
                } else {
                    normal_1.setBackgroundResource(R.drawable.a_020);
                    normal_1_tab = true;
                }
                break;
            case R.id.abnormal_1:
                if (abnormal_1_tab) {
                    normal_1.setBackgroundResource(R.drawable.a_020);
                    normal_1_tab = true;
                    abnormal_1.setBackgroundResource(R.drawable.a_019);
                    abnormal_1_tab = false;
                } else {
                    abnormal_1.setBackgroundResource(R.drawable.a_020);
                    abnormal_1_tab = true;
                }
                break;
            case R.id.normal_2:
                if (normal_2_tab) {
                    abnormal_2.setBackgroundResource(R.drawable.a_020);
                    abnormal_2_tab = true;
                    normal_2.setBackgroundResource(R.drawable.a_019);
                    normal_2_tab = false;
                } else {
                    normal_2.setBackgroundResource(R.drawable.a_020);
                    normal_2_tab = true;
                }
                break;
            case R.id.abnormal_2:
                if (abnormal_2_tab) {
                    normal_2.setBackgroundResource(R.drawable.a_020);
                    normal_2_tab = true;
                    abnormal_2.setBackgroundResource(R.drawable.a_019);
                    abnormal_2_tab = false;
                } else {
                    abnormal_2.setBackgroundResource(R.drawable.a_020);
                    abnormal_2_tab = true;
                }
                break;
            case R.id.start:
                if ((!normal_1_tab || !abnormal_1_tab) && (!normal_2_tab || !abnormal_2_tab)) {
                    String color;
                    String smell;
                    String dateC;
                    String timeC;
                    String numberGong;
                    String milliliter;
                    if (normal_1_tab) {
                        color = "异常";
                    } else {

                        color = "正常";
                    }
                    if (normal_2_tab) {
                        smell = "异常";
                    } else {

                        smell = "正常";
                    }
                    dateC = diluentE.getDate();
                    timeC = diluentE.getTime();
                    numberGong = diluentE.getId_Gong_1();
                    milliliter = diluentE.getId_ml();
                    String[] dataArray = new String[]{color, smell, dateC, timeC, numberGong, milliliter};
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", dataArray);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection1");
                    intent.addFlags(2);
                    startActivity(intent);
                } else {
                    CustomToast.showToast(this, "请选择是否正常........");
                }

                break;
        }
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
            public void onHumidityChange(int data) {
                diluentE.setHumidity(data + "");
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}