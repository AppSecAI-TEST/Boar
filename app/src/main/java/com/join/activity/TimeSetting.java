package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.service.Humidity;
import com.join.three.DateSelectDialog;
import com.join.three.TimeSelectDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class TimeSetting extends Activity implements View.OnClickListener, ServiceConnection {
    private PercentLinearLayout date_t, time_t;
    private TextView humidity, date_tv_1, date_tv_2, time_tv;
    private ImageView icon_1;
    private Intent intent;
    private Humidity.HumidityBinder humidityBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);
        initView();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
        String tateFormat = sDateFormat.format(new java.util.Date());
        date_tv_2.setText(tateFormat);
        SimpleDateFormat sDateFormat2 = new SimpleDateFormat("dd");
        String tateFormat2 = sDateFormat2.format(new java.util.Date());
        date_tv_1.setText(tateFormat2);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        String timeFormatS = timeFormat.format(new java.util.Date());
        time_tv.setText(timeFormatS);


    }

    @Override
    protected void onResume() {
        super.onResume();
   /*     Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unbindService(this);
    }

    private void initView() {
        humidity = (TextView) findViewById(R.id.humidity);
        date_t = (PercentLinearLayout) findViewById(R.id.date_t);
        date_t.setOnClickListener(this);
        time_t = (PercentLinearLayout) findViewById(R.id.time_t);
        time_t.setOnClickListener(this);
        date_tv_1 = (TextView) findViewById(R.id.date_tv_1);
        date_tv_2 = (TextView) findViewById(R.id.date_tv_2);
        time_tv = (TextView) findViewById(R.id.time_tv);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_t:
                DateSelectDialog mChangeAddressDialog = new DateSelectDialog(
                        TimeSetting.this);
                // mChangeAddressDialog.setAddress("四川", "自贡");
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new DateSelectDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String strArea) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("hhmmss");
                                String tateFormat = sDateFormat.format(new java.util.Date());
                                testDate(province + city + strArea + "." + tateFormat);
                                date_tv_2.setText(province + "年" + city + "月");

                                date_tv_1.setText(strArea);
                                Toast.makeText(TimeSetting.this,
                                        province + "-" + city + "-" + strArea,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                break;
            case R.id.time_t:
                TimeSelectDialog timeSelectDialog = new TimeSelectDialog(TimeSetting.this);
                timeSelectDialog.show();
                timeSelectDialog.setAddresskListener(new TimeSelectDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String province, String city) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
                        String tateFormat = sDateFormat.format(new java.util.Date());
                        testDate(tateFormat + "." + province + city + "30");
                        time_tv.setText(province + ":" + city);
                        Toast.makeText(TimeSetting.this,
                                province + "-" + city,
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.icon_1:
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
        }
    }

    public void testDate(String time) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            String datetime = time; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
