package com.join.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.join.R;
import com.join.databinding.StosteDetection22Binding;
import com.join.entity.Result1;

/**
 * 稀释精液检测详情结果
 */

public class StosteDetection22 extends Activity {
    private String TAG = "jjjStosteDetection22";
    private Result1 result1;
    private String[] originalData;
    private float[] arithmetic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StosteDetection22Binding binding = DataBindingUtil.setContentView(this, R.layout.stoste_detection_2_2);
        result1 = new Result1();
        binding.setResult1(result1);
        originalData = getIntent().getStringArrayExtra("data");
        //得到算法的数据
        arithmetic = getIntent().getFloatArrayExtra("arithmetic");
        Log.e(TAG, "init: " + originalData[1]);
        init();

    }

    private void init() {
        String color = originalData[0];
        String smell = originalData[1];
        String dateC = originalData[2];
        String timeC = originalData[3];
        String number = originalData[4];
        String milliliter = originalData[5];
        String operator = IDSelect.id_manage;
        String type = "稀释精液";
/*        float valid = arithmetic[7];
        float density = arithmetic[1];
        float motileSperms=arithmetic[2];
        float vitality = arithmetic[5];
        float motilityRate = arithmetic[4];
        String densityS = String.valueOf(Float.parseFloat(String.format("%.3f", density)));
        String vitalityS = String.valueOf(Float.parseFloat(String.format("%.3f", vitality)));
        String motilityRateS = String.valueOf(Float.parseFloat(String.format("%.3f", motilityRate)));
        String motileSpermsS = String.valueOf(Float.parseFloat(String.format("%.3f", motileSperms)));*/

        result1.setTime(timeC);
        result1.setDate(dateC);
        result1.setColor(color);
        result1.setSmell(smell);
        result1.setBatch(number);
        result1.setOperator(operator);
        result1.setCapacity(milliliter);


 /*       //保存到数据库
        Storage storage = new Storage(color, smell, dateC, timeC, number, operator, type,
                densityS, vitalityS, motilityRateS, null, null, null,milliliter,motileSpermsS);
       OperationDao.addData(storage);*/
    }

}
