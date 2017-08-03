package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.DaoUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.join.R.id.dateC;
import static com.join.R.id.milliliter;
import static com.join.R.id.motilityRate;
import static com.join.R.id.number;
import static com.join.R.id.operator;
import static com.join.R.id.result;
import static com.join.R.id.smell;
import static com.join.R.id.timeC;
import static com.join.R.id.vitality;

/**
 * 精液原液详情结果
 */

public class StosteDetection2 extends Activity implements View.OnClickListener, ServiceConnection {
    private Button print, bu_return;
    private ImageView icon_1;
    private TextView humidity,
            color_1,
            smell_1,
            dateC_1,
            timeC_1,
            number_1,//公猪编号
            milliliter_1,//采精量
            operator_1,//操作员
            add_1,          //需增加的份数
            density_1,//密度
            vitality_1,//活力
            motilityRate_1,//活率
            copies_1,//推荐分装份数
            result_1;
    private Humidity.HumidityBinder humidityBinder;
    private String[] stosteDetectionData;
    private double[] arithmetic;
    private int tab;
    private String TAG = "jjjStosteDetection2";
    private List<Storage> storages;//查询某个ID得到的数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_2);
        init();

    }

    private void init() {
        result_1 = (TextView) findViewById(result);
        add_1 = (TextView) findViewById(R.id.add);
        density_1 = (TextView) findViewById(R.id.density);
        vitality_1 = (TextView) findViewById(vitality);
        motilityRate_1 = (TextView) findViewById(motilityRate);
        copies_1 = (TextView) findViewById(R.id.copies);
        color_1 = (TextView) findViewById(R.id.color);
        smell_1 = (TextView) findViewById(smell);
        dateC_1 = (TextView) findViewById(dateC);
        timeC_1 = (TextView) findViewById(timeC);
        number_1 = (TextView) findViewById(number);
        milliliter_1 = (TextView) findViewById(milliliter);
        operator_1 = (TextView) findViewById(operator);

        humidity = (TextView) findViewById(R.id.humidity);
        print = (Button) findViewById(R.id.print);
        print.setOnClickListener(this);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);


        int action = getIntent().getFlags();//区别IDQuery还是StosteDetection1
        Log.e(TAG, action + "");
        if (action == 1) {
            setSaveData();
        } else if (action == 2) {
            getSetDataIDQuery();

        }
    }

    /**
     * IDQuery传过来的ID,查询传过来的ID设置到页面
     */

    private volatile boolean queryLoveIDThreadState = true;

    private void getSetDataIDQuery() {
        final long idqUeryData = getIntent().getLongExtra("IDQUeryData", -1L);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (queryLoveIDThreadState) ;
                storages = OperationDao.queryLoveID(idqUeryData);
                Log.e(TAG, "run: " + "查询几次");
                queryLoveIDThreadState = false;
            }
        }).start();
        storages = OperationDao.queryLoveID(idqUeryData);
        Storage storage = storages.get(0);
        //显示到页面
        add_1.setText(storage.getAdd() + "ml");
        copies_1.setText(storage.getCopies());
        density_1.setText(storage.getDensity() + "(亿/ml)");
        vitality_1.setText(storage.getVitality());
        motilityRate_1.setText(storage.getMotilityRate());
        color_1.setText(storage.getColor());
        smell_1.setText(storage.getSmell());
        dateC_1.setText(storage.getDate());
        timeC_1.setText(storage.getTime());
        number_1.setText(storage.getNumber());
        milliliter_1.setText(storage.getMilliliter());
        operator_1.setText(storage.getOperator());
        result_1.setText("结果: " + storage.getResult());
    }

    private void setSaveData() {
        //得到上一个Activity的数据
        stosteDetectionData = getIntent().getStringArrayExtra("data");
        //得到算法的数据
        arithmetic = getIntent().getDoubleArrayExtra("arithmetic");

        double density = arithmetic[1];
        double vitality = arithmetic[5];
        double motilityRate = arithmetic[4];
        Log.e(TAG, "setSaveData: "+"density"+density+"vitality"+vitality );
        String densityS = String.valueOf(Double.parseDouble(String.format("%.3f", density)));
        String vitalityS = String.valueOf(Double.parseDouble(String.format("%.3f", vitality)));
        String motilityRateS = String.valueOf(Double.parseDouble(String.format("%.3f", motilityRate)));

        String color = stosteDetectionData[0];
        String smell = stosteDetectionData[1];
        String dateC = stosteDetectionData[2];
        String timeC = stosteDetectionData[3];
        String number = stosteDetectionData[4];
        String milliliter = stosteDetectionData[5];
        String result = null;

        String type = "精液原液";

        int length = milliliter.length();
        String milliliterSubstring = milliliter.substring(0, length - 2);
        Integer milliliterInt = Integer.valueOf(milliliterSubstring);


        if (milliliterInt >= 250 && density >= 3.0 && vitality >= 0.8) {
            result = "优";
        } else if (milliliterInt >= 150 && milliliterInt <= 250 && density >= 2.0 && density <= 3.0 && vitality >= 0.7 && vitality <= 0.8) {
            result = "良";
        } else if (milliliterInt >= 150 && milliliterInt <= 150 && density >= 0.8 && density <= 2.0 && vitality >= 0.6 && vitality <= 0.7) {
            result = "合格";
        } else {
            result = "不合格";
        }

        double copies = milliliterInt * motilityRate * vitality / 30; //得到推荐份数
        double capacity = milliliterInt / copies;//得到每剂容量
        double add = copies * 80 - milliliterInt;  //得到需增加多少稀释精液
        if (add < 0) {
            add = 0.0;
        }
        String addS = String.valueOf(Double.parseDouble(String.format("%.3f", add)));//取小数点后三位的数,四舍五入
        String copiesS = String.valueOf(Math.round(copies));//取整数,四舍五入


        //拿到管理员序号
        SharedPreferences preferences = getSharedPreferences("IdSelect", Context.MODE_PRIVATE);
        String operatorData = preferences.getString("operator", "defaultname");

        String operator = operatorData;


        //显示到页面
        add_1.setText(addS + "ml");
        copies_1.setText(copiesS);
        density_1.setText(densityS + "(亿/ml)");
        vitality_1.setText(vitalityS);
        motilityRate_1.setText(motilityRateS);
        color_1.setText(color);
        smell_1.setText(smell);
        dateC_1.setText(dateC);
        timeC_1.setText(timeC);
        number_1.setText(number);
        milliliter_1.setText(milliliter);
        operator_1.setText(operator);
        result_1.setText("结果: " + result);


        SimpleDateFormat sdate = new SimpleDateFormat("yyyyMMdd");
        String checkoutDate = sdate.format(new java.util.Date());
        Integer checkoutDateInt = Integer.valueOf(checkoutDate);


        SimpleDateFormat stime = new SimpleDateFormat("hh:mm");
        String checkoutTime = stime.format(new java.util.Date());

        //保存到数据库
        Storage storage = new Storage();
        DaoUtil.sD2(storage, number, operator, milliliter, dateC, timeC, checkoutDateInt, checkoutTime, copiesS, addS, densityS, vitalityS, motilityRateS, smell, color, type, result);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.print:
                CustomToast.showToast(this, "正在完善中......");

                break;
            case R.id.bu_return:
                finish();
                break;
        }
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
      //  unbindService(this);
        queryLoveIDThreadState = false;
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
