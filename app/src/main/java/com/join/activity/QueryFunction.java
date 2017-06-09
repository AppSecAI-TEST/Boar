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

import com.github.mjdev.libaums.fs.UsbFile;
import com.join.R;
import com.join.UDisk.SaveToExcelAndSD;
import com.join.UDisk.UDiskToSD;
import com.join.callback.MonitorUDiskCallback;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.KeyboardIDQuery;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.File;

/**
 *
 */

public class QueryFunction extends Activity implements View.OnClickListener, ServiceConnection, MonitorUDiskCallback {
    private String TAG = "jjjQueryFuncetion";
    private PercentLinearLayout function_1, function_2, function_3, function_4;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private Intent intent;
    private Button bu_copy;
    private ImageView icon;
    private KeyboardIDQuery keyboardIDQuery;
    private PercentLinearLayout percent;
    private boolean UDiskState;//是否插入U盘

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_function);
        init();
    }

    private void init() {
        percent = (PercentLinearLayout) findViewById(R.id.percent);
        intent = new Intent();
        icon = (ImageView) findViewById(R.id.icon);
        bu_copy = (Button) findViewById(R.id.bu_copy);
        bu_copy.setOnClickListener(this);
        function_1 = (PercentLinearLayout) findViewById(R.id.function_1);
        function_1.setOnClickListener(this);
        function_2 = (PercentLinearLayout) findViewById(R.id.function_2);
        function_2.setOnClickListener(this);
        function_3 = (PercentLinearLayout) findViewById(R.id.function_3);
        function_3.setOnClickListener(this);
        function_4 = (PercentLinearLayout) findViewById(R.id.function_4);
        function_4.setOnClickListener(this);
        humidity = (TextView) findViewById(R.id.humidity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function_1:

                break;
            case R.id.function_2:
                keyboardIDQuery = new KeyboardIDQuery(QueryFunction.this, intent);
                keyboardIDQuery.showWindow(percent);
                break;
            case R.id.function_3:

                break;
            case R.id.function_4:

                break;
            case R.id.icon:
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.bu_copy:
                UDiskToSD uDiskToSD = new UDiskToSD(this);
                uDiskToSD.setMonitorUDiskCallback(this);
                uDiskToSD.registerReceiver();
                uDiskToSD.redDeviceList();
                if (UDiskState) {
                    SaveToExcelAndSD saveToExcelAndSD = new SaveToExcelAndSD(this);
                    saveToExcelAndSD.saveToExcelAndSD("/storage/emulated/0/CreateCare" + File.separator + "demo.xls");
                    UsbFile usbFile = uDiskToSD.getUsbFile();
                    uDiskToSD.readFile(usbFile, "/storage/emulated/0/CreateCare" + File.separator + "demo.xls");
                } else {
                    CustomToast.showToast(this, "请插入你的U盘...");
                }
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
    public void getState(boolean state) {
        Log.e(TAG, "getState: " + state);
        UDiskState = state;

    }
}