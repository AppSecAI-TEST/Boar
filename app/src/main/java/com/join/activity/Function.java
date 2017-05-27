package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.join.R;
import com.join.service.Humidity;
import com.zhy.android.percent.support.PercentLinearLayout;

/**
 * Created by join on 2017/5/11.
 */

public class Function extends Activity implements View.OnClickListener, ServiceConnection {
    private PercentLinearLayout function_1, function_2, function_3, function_4;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function);
        initView();
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

    private void initView() {
        humidity = (TextView) findViewById(R.id.humidity);
        function_1 = (PercentLinearLayout) findViewById(R.id.function_1);
        function_1.setOnClickListener(this);
        function_2 = (PercentLinearLayout) findViewById(R.id.function_2);
        function_2.setOnClickListener(this);
        function_3 = (PercentLinearLayout) findViewById(R.id.function_3);
        function_3.setOnClickListener(this);
        function_4 = (PercentLinearLayout) findViewById(R.id.function_4);
        function_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function_1:
                Intent intent = new Intent();
                intent.addFlags(1);
                intent.setAction("com.join.stostedetection");
                startActivity(intent);
                break;
            case R.id.function_2:
                Intent intent2 = new Intent();
                intent2.addFlags(2);
                intent2.setAction("com.join.stostedetection");
                startActivity(intent2);
                break;
            case R.id.function_3:
                Intent intent3 = new Intent();
                intent3.setAction("com.join.IDQuery");
                startActivity(intent3);
                break;
            case R.id.function_4:
                Intent intent4 = new Intent();
                intent4.setAction("com.join.SystemSet");
                startActivity(intent4);
                break;
        }
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
}
