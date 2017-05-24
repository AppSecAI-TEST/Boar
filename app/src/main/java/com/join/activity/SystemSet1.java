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

/**
 * Created by join on 2017/5/15.
 */

public class SystemSet1 extends Activity implements View.OnClickListener ,ServiceConnection {
    private Button bu_return;
    private ImageView icon_1;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_set_1);
        init();
    }

    private void init() {
        humidity = (TextView) findViewById(R.id.humidity);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_return:
                finish();
                break;
            case R.id.icon_1:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
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
}
