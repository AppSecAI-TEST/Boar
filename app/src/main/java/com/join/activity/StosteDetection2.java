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
import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;
import com.join.service.Humidity;
import com.join.utils.CustomToast;

import java.util.List;

import static com.join.R.id.motilityRate;
import static com.join.R.id.operator;
import static com.join.R.id.vitality;

/**
 * 单次结果
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
            copies_1;//推荐分装份数
    private Humidity.HumidityBinder humidityBinder;
    private String[] stosteDetectionData;
    private float[] arithmetic;
    private int tab;
    private String TAG = "jjjStosteDetection2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_2);
        init();


    }

    private void init() {
        add_1 = (TextView) findViewById(R.id.add);
        density_1 = (TextView) findViewById(R.id.density);
        vitality_1 = (TextView) findViewById(vitality);
        motilityRate_1 = (TextView) findViewById(motilityRate);
        copies_1 = (TextView) findViewById(R.id.copies);
        color_1 = (TextView) findViewById(R.id.color);
        smell_1 = (TextView) findViewById(R.id.smell);
        dateC_1 = (TextView) findViewById(R.id.dateC);
        timeC_1 = (TextView) findViewById(R.id.timeC);
        number_1 = (TextView) findViewById(R.id.number);
        milliliter_1 = (TextView) findViewById(R.id.milliliter);
        operator_1 = (TextView) findViewById(operator);
        int action = getIntent().getFlags();
        Log.e("jjj", action + "");
        if (action == 1) {
            stosteDetectionData = getIntent().getStringArrayExtra("data");
            arithmetic = getIntent().getFloatArrayExtra("arithmetic");
            float valid = arithmetic[7];
            float density = arithmetic[1];
            float vitality = arithmetic[5];
            float motilityRate = arithmetic[4];
            String densityS = String.valueOf(Float.parseFloat(String.format("%.3f", density)));
            String vitalityS = String.valueOf(Float.parseFloat(String.format("%.3f", vitality)));
            String motilityRateS = String.valueOf(Float.parseFloat(String.format("%.3f", motilityRate)));

            String color = stosteDetectionData[0];
            String smell = stosteDetectionData[1];
            String dateC = stosteDetectionData[2];
            String timeC = stosteDetectionData[3];
            String number = stosteDetectionData[4];
            String milliliter = stosteDetectionData[5];
            int length = milliliter.length();
            String substring = milliliter.substring(0, length - 2);
            Integer integer = Integer.valueOf(substring);
            float copies = integer * motilityRate * vitality / 30;
            Log.e(TAG, "init: " + copies);
            int round = Math.round(copies);
            String copiesS = String.valueOf(round);
            String operator = IDSelect.id_manage;
            copies_1.setText(copiesS);
            density_1.setText(densityS);
            vitality_1.setText(vitalityS);
            motilityRate_1.setText(motilityRateS);
            color_1.setText(color);
            smell_1.setText(smell);
            dateC_1.setText(dateC);
            timeC_1.setText(timeC);
            number_1.setText(number);
            milliliter_1.setText(milliliter);
            operator_1.setText(operator);
            Storage storage = new Storage(color, smell, dateC, timeC, number, operator, null, densityS, vitalityS, motilityRateS, null);
            OperationDao.addData(storage);
            List<Storage> storages = OperationDao.queryAll();
            int size = storages.size();
            for (int i = 1; i < size; i++) {
                Storage storage1 = storages.get(i);
                String smell1 = storage1.getSmell();
                Log.e("jjjj", smell1);
            }
        }
        humidity = (TextView) findViewById(R.id.humidity);
        print = (Button) findViewById(R.id.print);
        print.setOnClickListener(this);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
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
