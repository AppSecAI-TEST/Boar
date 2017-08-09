package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import com.join.R;
import com.join.databinding.DiluentDetectionResultBinding;
import com.join.entity.Result1;
import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.DaoUtil;

import java.util.List;

/**
 * 稀释精液检测详情结果
 */

public class DiluentDetectionResult extends Activity implements ServiceConnection {
    private String TAG = "jjjStosteDetection22";
    private Result1 result1;
    private String[] stosteDetectionData;
    private double[] arithmetic;
    private Intent intent;
    private Humidity.HumidityBinder humidityBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiluentDetectionResultBinding binding = DataBindingUtil.setContentView(this, R.layout.diluent_detection_result);
        result1 = new Result1();
        binding.setResult1(result1);
        binding.setActivity(this);
        init();
    }

    public void closeActivity(View view) {
        finish();
    }

    public void printData(View view) {
        CustomToast.showToast(this, "正在完善中........");
    }

    public void returnFunction(View view) {
        intent.setAction("com.join.function");
        startActivity(intent);
    }

    private void init() {
        intent = new Intent();
        stosteDetectionData = getIntent().getStringArrayExtra("data");
        //得到算法的数据
        arithmetic = getIntent().getDoubleArrayExtra("arithmetic");
        int flags = getIntent().getFlags();//区别IDQuery还是StosteDetection1
        if (flags == 1) {
            setSaveData();
        } else if (flags == 2) {
            getSetDataIDQuery();
        }

    }

    /**
     * 拿到IDQuery的ID之后显示在页面
     */
    private void getSetDataIDQuery() {
        long idqUeryData = getIntent().getLongExtra("IDQUeryData", -1L);
        List<Storage> storages = OperationDao.queryLoveID(idqUeryData);
        Storage storage = storages.get(0);
        //显示到页面
        result1.setResult("结果: " + storage.getResult());
        result1.setMotileSperms(result1.getMotileSperms());
        result1.setDensity(storage.getDensity() + "(亿/ml)");
        result1.setVitality(storage.getVitality());
        result1.setMotilityRate(storage.getMotilityRate());
        result1.setTime(storage.getCheckoutTime());
        int checkoutDateInt = storage.getCheckoutDate();
        String originalData = String.valueOf(checkoutDateInt);
        String checkoutDate = originalData.substring(0, 4) + "-" + originalData.substring(4, 6) + "-" + originalData.substring(6, 8);
        result1.setDate(checkoutDate);
        result1.setColor(storage.getColor());
        result1.setSmell(storage.getSmell());
        result1.setBatch(storage.getNumber());
        result1.setOperator(storage.getOperator());
        result1.setCapacity(storage.getCapacity());
        result1.setMotileSperms(storage.getMotileSperms());

    }

    /**
     * 拿到的StosteDetection1数据之后显示在页面,并存入数据库
     */
    private void setSaveData() {
        String color = stosteDetectionData[0];
        String smell = stosteDetectionData[1];
        String checkoutDateC = stosteDetectionData[2];
        String timeC = stosteDetectionData[3];
        String number = stosteDetectionData[4];
        String capacity = stosteDetectionData[5];
        Integer checkoutDateInt = Integer.valueOf(checkoutDateC);
        String originalData = String.valueOf(checkoutDateInt);
        String checkoutDate = originalData.substring(0, 4) + "-" + originalData.substring(4, 6) + "-" + originalData.substring(6, 8);
        //拿到管理员序号
        SharedPreferences preferences = getSharedPreferences("IdSelect", Context.MODE_PRIVATE);
        String operatorData = preferences.getString("operator", "defaultname");

        String operator = operatorData;
        String type = "稀释精液";
        String result = null;


        double density = arithmetic[1];
        double vitality = arithmetic[5];
        double motilityRate = arithmetic[4];
        double motileSperms = arithmetic[6];
        int length = capacity.length();
        String milliliterSubstring = capacity.substring(0, length - 2);
        Integer milliliterInt = Integer.valueOf(milliliterSubstring);
        if (milliliterInt >= 80 && vitality >= 0.6 && motileSperms >= 30) {
            result = "合格";
        } else {
            result = "不合格";
        }
        String motileSpermsS = String.valueOf(Double.parseDouble(String.format("%.3f", motileSperms)));
        String densityS = String.valueOf(Double.parseDouble(String.format("%.3f", density)));
        String vitalityS = String.valueOf(Double.parseDouble(String.format("%.3f", vitality)));
        String motilityRateS = String.valueOf(Double.parseDouble(String.format("%.3f", motilityRate)));
        result1.setResult("结果: " + result);
        result1.setMotileSperms(motileSpermsS);
        result1.setDensity(densityS + ("亿/ml"));
        result1.setVitality(vitalityS);
        result1.setMotilityRate(motilityRateS);
        result1.setTime(timeC);
        result1.setDate(checkoutDate);
        result1.setColor(color);
        result1.setSmell(smell);
        result1.setBatch(number);
        result1.setOperator(operator);
        result1.setCapacity(capacity);
        result1.setMotileSperms(motileSpermsS);
        //保存到数据库
        Storage storage = new Storage();
        DaoUtil.sD22(storage, densityS, motilityRateS, checkoutDateInt,
                timeC, smell, color, vitalityS, motileSpermsS, capacity, operator, number, type, result);

    }

    @Override
    protected void onResume() {
        super.onResume();
 /*       Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
       // unbindService(this);
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
                        result1.setHumidity("" + data);

                    }
                });
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
