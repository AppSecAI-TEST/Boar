package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.dialog.ManageDialog;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.PopupWindowUtil;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.text.SimpleDateFormat;

/**
 * 检测员ID选择   管理员手机号码
 */

public class IDSelect extends Activity implements View.OnClickListener, ServiceConnection {

    private Button affirm;
    private ImageView icon;
    private TextView date, time, input, humidity;
    private PopupWindowUtil util;
    private PercentLinearLayout one;
    private Humidity.HumidityBinder humidityBinder;
    public static String id_manage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_select);
        initView();
        showDialog();
        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);

    }

    private void initView() {
        humidity = (TextView) findViewById(R.id.humidity);
        one = (PercentLinearLayout) findViewById(R.id.one);
        one.setOnClickListener(this);
        icon = (ImageView) findViewById(R.id.icon_click);
        input = (TextView) findViewById(R.id.input);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        affirm = (Button) findViewById(R.id.affirm);
        affirm.setOnClickListener(this);
        util = new PopupWindowUtil(IDSelect.this, input, icon);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm");
        String tateFormat = sDateFormat.format(new java.util.Date());
        date.setText(tateFormat);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                util.showWindow(icon);
                icon.setBackgroundResource(R.drawable.a_012);
                break;
            case R.id.affirm:
                if (input.length() > 0) {
                    Log.e("jjj", input + "");
                    id_manage = input.getText().toString();
                    Intent intent = new Intent();
                    intent.setAction("com.join.function");
                    startActivity(intent);
                } else {
                    CustomToast.showToast(this, "请选择管理员ID......");
                }
                break;
        }
    }


    public void showDialog() {
        showAlertDialog();
    }

    public void showAlertDialog() {

        ManageDialog.Builder builder = new ManageDialog.Builder(this);
/*        builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("提示");*/
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏dialog
                dialog.dismiss();
            }
        });

 /*       builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏dialog
                        dialog.dismiss();
                        System.out.println("------------------点击取消----------------");
                    }
                });*/
        builder.create().show();
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

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
