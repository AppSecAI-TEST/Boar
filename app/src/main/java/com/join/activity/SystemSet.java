package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.join.R;
import com.join.service.Humidity;

/**
 * 标准参数,系统参数设置入口
 */

public class SystemSet extends Activity implements View.OnClickListener,ServiceConnection {
    private Button affirm1, affirm2, affirm3;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_set);
        init();
    }

    private void init() {
        humidity = (TextView) findViewById(R.id.humidity);
        affirm1 = (Button) findViewById(R.id.affirm1);
        affirm1.setOnClickListener(this);
        affirm2 = (Button) findViewById(R.id.affirm2);
        affirm2.setOnClickListener(this);
        affirm3 = (Button) findViewById(R.id.affirm3);
        affirm3.setOnClickListener(this);
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.affirm1:
                intent.setAction("com.join.ParameterSetting");
                startActivity(intent);

                break;
            case R.id.affirm2:
                intent.setAction("com.join.SystemSet1");
                startActivity(intent);
                break;
            case R.id.affirm3:
                finish();
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
