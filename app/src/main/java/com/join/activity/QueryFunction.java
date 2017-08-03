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

import com.github.mjdev.libaums.fs.UsbFile;
import com.join.R;
import com.join.UDisk.SaveToExcelAndSD;
import com.join.UDisk.UDiskToSD;
import com.join.service.Humidity;
import com.join.utils.KeyboardDateQuery;
import com.join.utils.KeyboardIDQuery;
import com.zhy.android.percent.support.PercentLinearLayout;

/**
 *查询主界面
 */

public class QueryFunction extends Activity implements View.OnClickListener, ServiceConnection {
    private String TAG = "jjjQueryFuncetion";
    private PercentLinearLayout function_1, function_2, function_3, function_4;
    private TextView humidity,textView2;
    private Humidity.HumidityBinder humidityBinder;
    private Intent intent;
    private ImageView icon;
    private KeyboardIDQuery keyboardIDQuery;
    private KeyboardDateQuery keyboardDateQuery;
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
        icon.setOnClickListener(this);
        function_1 = (PercentLinearLayout) findViewById(R.id.function_1);
        function_1.setOnClickListener(this);
        function_2 = (PercentLinearLayout) findViewById(R.id.function_2);
        function_2.setOnClickListener(this);
        function_3 = (PercentLinearLayout) findViewById(R.id.function_3);
        function_3.setOnClickListener(this);
        function_4 = (PercentLinearLayout) findViewById(R.id.function_4);
        function_4.setOnClickListener(this);
        humidity = (TextView) findViewById(R.id.humidity);
        textView2 = (TextView) findViewById(R.id.textView2);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function_1:
                keyboardDateQuery = new KeyboardDateQuery(QueryFunction.this, intent);
                keyboardDateQuery.showWindow(percent);
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
           /* case R.id.bu_copy:

                saveToExcelAndSD = new SaveToExcelAndSD(this);
                uDiskToSD = new UDiskToSD(this);
                uDiskToSD.registerReceiver();
                uDiskToSD.redDeviceList();


                boolean saveStateSD = saveToExcelAndSD.saveToExcelAndSD("/storage/emulated/0/CreateCare" + File.separator + "demo.xls");
                usbFile = uDiskToSD.getUsbFile();
                Log.e(TAG, "onClick: " + saveStateSD);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (saveStateSD) {
                    uDiskToSD.readFile(usbFile, "/storage/emulated/0/CreateCare" + File.separator + "demo.xls");
                }

                break;*/
        }
    }

    private SaveToExcelAndSD saveToExcelAndSD;
    private UsbFile usbFile;
    private UDiskToSD uDiskToSD;

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
                        humidity.setText( data);
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
/*        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
       // unbindService(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
