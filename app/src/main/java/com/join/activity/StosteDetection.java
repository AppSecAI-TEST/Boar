package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.Keyboard;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.text.SimpleDateFormat;


/**
 * 精液原液检测参数设置
 */

public class StosteDetection extends Activity implements View.OnClickListener, ServiceConnection {
    private String TAG = "jjjStosteDetection";
    private PercentLinearLayout id_Gong, id_Gong_2, id_Gong_3, percent;
    private TextView id_Gong_1, id_ml, time, date;
    private ImageView icon_1;
    private Keyboard keyboard;//自定义键盘
    private Button normal_1, abnormal_1, normal_2, abnormal_2, start;
    private boolean normal_1_tab = true;
    private boolean abnormal_1_tab = false;
    private Intent intent;

    private boolean normal_2_tab = true;
    private boolean abnormal_2_tab = false;
    private TextView humidity, title;
    private Humidity.HumidityBinder humidityBinder;

    private String dateFormat;
    private String timeFormat;
    private int[] windowSelect;//窗口的标记

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.id_Gong:
                keyboard = new Keyboard(StosteDetection.this, id_Gong_1, id_ml, time, 1);
                keyboard.showWindow(percent);
                break;
            case R.id.id_Gong_2:
                keyboard = new Keyboard(StosteDetection.this, id_Gong_1, id_ml, time, 2);
                keyboard.showWindow(percent);
                break;
            case R.id.id_Gong_3:
                keyboard = new Keyboard(StosteDetection.this, id_Gong_1, id_ml, time, 3);
                keyboard.showWindow(percent);
                break;

            case R.id.normal_1:
                if (!normal_1_tab) {
                    abnormal_1.setBackgroundResource(R.drawable.a_020);
                    abnormal_1_tab = false;
                    normal_1.setBackgroundResource(R.drawable.a_019);
                    normal_1_tab = true;
                }
                break;
            case R.id.abnormal_1:
                if (!abnormal_1_tab) {
                    normal_1.setBackgroundResource(R.drawable.a_020);
                    normal_1_tab = false;
                    abnormal_1.setBackgroundResource(R.drawable.a_019);
                    abnormal_1_tab = true;
                }
                break;
            case R.id.normal_2:
                if (!normal_2_tab) {
                    abnormal_2.setBackgroundResource(R.drawable.a_020);
                    abnormal_2_tab = false;
                    normal_2.setBackgroundResource(R.drawable.a_019);
                    normal_2_tab = true;
                }
                break;
            case R.id.abnormal_2:
                if (!abnormal_2_tab) {
                    normal_2.setBackgroundResource(R.drawable.a_020);
                    normal_2_tab = false;
                    abnormal_2.setBackgroundResource(R.drawable.a_019);
                    abnormal_2_tab = true;
                }
                break;
            case R.id.start:
                if ((!normal_1_tab || !abnormal_1_tab) && (!normal_2_tab || !abnormal_2_tab)) {
                    String color;
                    String smell;
                    String numberGong;
                    String milliliter;
                    if (normal_1_tab) {
                        color = "正常";
                    } else {
                        color = "异常";
                    }
                    if (normal_2_tab) {
                        smell = "正常";
                    } else {

                        smell = "异常";
                    }
                    Log.e(TAG, "onClick: " + color + "\n" + smell);
                    numberGong = id_Gong_1.getText().toString();
                    milliliter = id_ml.getText().toString();
                    String[] dataArray = new String[]{color, smell, dateFormat, timeFormat, numberGong, milliliter};
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("data", dataArray);
                    bundle.putIntArray("windowSelect", windowSelect);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection1");
                    intent.addFlags(1);
                    startActivity(intent);
                } else {
                    CustomToast.showToast(this, "请选择是否正常........");
                }

                break;
        }
    }


    private void init() {
        percent = (PercentLinearLayout) findViewById(R.id.percent);
        intent = new Intent();
        title = (TextView) findViewById(R.id.title);
        humidity = (TextView) findViewById(R.id.humidity);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        id_ml = (TextView) findViewById(R.id.id_ml);
        id_Gong_1 = (TextView) findViewById(R.id.id_Gong_1);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        id_Gong = (PercentLinearLayout) findViewById(R.id.id_Gong);
        id_Gong.setOnClickListener(this);
        id_Gong_2 = (PercentLinearLayout) findViewById(R.id.id_Gong_2);
        id_Gong_2.setOnClickListener(this);
        id_Gong_3 = (PercentLinearLayout) findViewById(R.id.id_Gong_3);
        id_Gong_3.setOnClickListener(this);
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


        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = sdate.format(new java.util.Date());

        date.setText(dateFormat);

        SimpleDateFormat stime = new SimpleDateFormat("hh:mm");
        timeFormat = stime.format(new java.util.Date());
        time.setText(timeFormat);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection);
        init();
        Bundle extras = getIntent().getExtras();
        windowSelect = extras.getIntArray("windowSelect");


    }


    @Override
    protected void onResume() {
        super.onResume();
  /*      Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
       // unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        Humidity humidityClass = humidityBinder.getHumidity();
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
}
