package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.utils.Keyboard;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * Created by join on 2017/5/11.
 */

public class StosteDetection extends Activity implements View.OnClickListener {
    private PercentLinearLayout id_Gong, id_Gong_2, id_Gong_3;
    private TextView id_Gong_1, id_ml, time;
    private ImageView icon_1;
    private Keyboard keyboard;
    private Button normal_1, abnormal_1, normal_2, abnormal_2, start;
    private boolean normal_1_tab = true;
    private boolean abnormal_1_tab = true;
    private boolean normal_2_tab = true;
    private boolean abnormal_2_tab = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.id_Gong:

                keyboard.showWindow(id_Gong, 1);
                break;
            case R.id.id_Gong_2:

                keyboard.showWindow(id_Gong, 2);
                break;
            case R.id.id_Gong_3:
                keyboard.showWindow(id_Gong, 3);
                break;
            case R.id.normal_1:
                if (normal_1_tab) {
                    abnormal_1.setBackgroundResource(R.drawable.background_2);
                    abnormal_1_tab = true;
                    normal_1.setBackgroundResource(R.drawable.background_3);
                    normal_1_tab = false;
                } else {
                    normal_1.setBackgroundResource(R.drawable.background_2);
                    normal_1_tab = true;
                }
                break;
            case R.id.abnormal_1:
                if (abnormal_1_tab) {
                    normal_1.setBackgroundResource(R.drawable.background_2);
                    normal_1_tab = true;
                    abnormal_1.setBackgroundResource(R.drawable.background_3);
                    abnormal_1_tab = false;
                } else {
                    abnormal_1.setBackgroundResource(R.drawable.background_2);
                    abnormal_1_tab = true;
                }
                break;
            case R.id.normal_2:
                if (normal_2_tab) {
                    abnormal_2.setBackgroundResource(R.drawable.background_2);
                    abnormal_2_tab = true;
                    normal_2.setBackgroundResource(R.drawable.background_3);
                    normal_2_tab = false;
                } else {
                    normal_2.setBackgroundResource(R.drawable.background_2);
                    normal_2_tab = true;
                }
                break;
            case R.id.abnormal_2:
                if (abnormal_2_tab) {
                    normal_2.setBackgroundResource(R.drawable.background_2);
                    normal_2_tab = true;
                    abnormal_2.setBackgroundResource(R.drawable.background_3);
                    abnormal_2_tab = false;
                } else {
                    abnormal_2.setBackgroundResource(R.drawable.background_2);
                    abnormal_2_tab = true;
                }
                break;
            case R.id.start:
                Intent intent2 = new Intent();
                intent2.setAction("com.join.stostedetection1");
                startActivity(intent2);
                break;
        }
    }

    private void init() {
        time = (TextView) findViewById(R.id.time);
        id_ml = (TextView) findViewById(R.id.id_ml);
        id_Gong_1 = (TextView) findViewById(R.id.id_Gong_1);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        id_Gong = (PercentLinearLayout) findViewById(R.id.id_Gong);
        id_Gong.setOnClickListener(this);
        id_Gong_2 = (PercentLinearLayout) findViewById(R.id.id_Gong_2);
        id_Gong_2.setOnClickListener(this);
        id_Gong_3 = (PercentLinearLayout) findViewById(R.id.id_Gong_3);
        id_Gong_3.setOnClickListener(this);
        normal_1 = (Button) findViewById(R.id.normal_1);
        normal_1.setOnClickListener(this);
        abnormal_1 = (Button) findViewById(R.id.abnormal_1);
        abnormal_1.setOnClickListener(this);
        normal_2 = (Button) findViewById(R.id.normal_2);
        normal_2.setOnClickListener(this);
        abnormal_2 = (Button) findViewById(R.id.abnormal_2);
        abnormal_2.setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        keyboard = new Keyboard(StosteDetection.this, id_Gong_1,id_ml, time);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection);
        init();
    }

}
