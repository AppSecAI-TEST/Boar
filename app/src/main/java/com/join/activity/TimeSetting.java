package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.three.DateSelectDialog;
import com.join.three.TimeSelectDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;



public class TimeSetting extends Activity implements View.OnClickListener {
    private PercentLinearLayout date_t, time_t;
    private TextView date_tv, time_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);
        initView();

    }

    private void initView() {
        date_t = (PercentLinearLayout) findViewById(R.id.date_t);
        date_t.setOnClickListener(this);
        time_t = (PercentLinearLayout) findViewById(R.id.time_t);
        time_t.setOnClickListener(this);
        date_tv = (TextView) findViewById(R.id.date_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
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
                                date_tv.setText(province+"/"+city+"/"+strArea);


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
                        time_tv.setText(province+":"+city);
                        Toast.makeText(TimeSetting.this,
                                province + "-" + city,
                                Toast.LENGTH_LONG).show();
                    }
                });
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
}
