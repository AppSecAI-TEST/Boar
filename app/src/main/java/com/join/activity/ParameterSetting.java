package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import com.join.R;
import com.join.databinding.ParameterSettingBinding;
import com.join.entity.SettingResult;
import com.join.service.Humidity;


/**
 * Created by join on 2017/5/31.
 */

public class ParameterSetting extends Activity implements ServiceConnection {
    private Intent intent;
    private Humidity.HumidityBinder humidityBinder;
    private SettingResult settingResult;
    private ParameterSettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.parameter_setting);
        init();
    }

    private void init() {
        intent = new Intent();
        settingResult = new SettingResult();
        binding.setSetting(this);
        binding.setResult(settingResult);
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
        //unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        Humidity humidityClass = humidityBinder.getHumidity();
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final String data) {
                settingResult.setHumidity( data);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void returnFunction(View view) {
        intent.setAction("com.join.Function");
        startActivity(intent);
    }

    public void returnAbove(View view) {
        finish();
    }

    public void confirm(View view) {
        finish();
    }
}
