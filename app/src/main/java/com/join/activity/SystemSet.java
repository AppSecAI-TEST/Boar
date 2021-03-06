package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
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
import com.join.dialog.PasswordManageDialog;
import com.join.service.Humidity;

/**
 * 标准参数,系统参数设置入口
 */

public class SystemSet extends Activity implements View.OnClickListener, ServiceConnection {
    private Button time_bu, wifi_bu, brightness_bu, standard_bu, manage_bu, about_bu;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private Intent intent;
    private ImageView icon_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_set);
        init();
    }

    private void init() {
        humidity = (TextView) findViewById(R.id.humidity);
        time_bu = (Button) findViewById(R.id.time_bu);
        time_bu.setOnClickListener(this);
        wifi_bu = (Button) findViewById(R.id.wifi_bu);
        wifi_bu.setOnClickListener(this);
        brightness_bu = (Button) findViewById(R.id.brightness_bu);
        brightness_bu.setOnClickListener(this);
        standard_bu = (Button) findViewById(R.id.standard_bu);
        standard_bu.setOnClickListener(this);
        manage_bu = (Button) findViewById(R.id.manage_bu);
        manage_bu.setOnClickListener(this);
        about_bu = (Button) findViewById(R.id.about_bu);
        about_bu.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_bu:

                intent.setAction("com.join.TimeSetting");
                startActivity(intent);
                break;
            case R.id.wifi_bu:
                intent.setAction("com.join.WiFiSetting");
                startActivity(intent);
                break;
            case R.id.brightness_bu:
                intent.setAction("com.join.Brightness");
                startActivity(intent);
                break;
            case R.id.standard_bu:
                intent.setAction("com.join.ParameterSetting");
                startActivity(intent);
                break;
            case R.id.manage_bu:
                showAlertDialog();
                break;
            case R.id.about_bu:

                break;
            case R.id.icon_1:
                intent.setAction("com.join.Function");
                startActivity(intent);
                break;
        }
    }

    public void showAlertDialog() {

        final PasswordManageDialog.Builder builder = new PasswordManageDialog.Builder(this);
        builder.setObtainPassword(new PasswordManageDialog.Builder.ObtainPassword() {
            @Override
            public void getPassword(String pa) {
                Log.e("jjjj", "getPassword: " + pa);
            }
        });
/*        builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("提示");*/
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                intent.setAction("com.join.PasswordManager");
                startActivity(intent);
                // 隐藏dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏dialog
                        dialog.dismiss();
                        System.out.println("------------------点击取消----------------");
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    /*    Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //   unbindService(this);
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
