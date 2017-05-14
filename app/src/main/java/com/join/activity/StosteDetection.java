package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.join.R;
import com.join.utils.Keyboard;
import com.zhy.android.percent.support.PercentLinearLayout;


/**
 * Created by join on 2017/5/11.
 */

public class StosteDetection extends Activity implements View.OnClickListener {
    private PercentLinearLayout id_Gong;
    private TextView id_Gong_1;

    private Button normal_1, abnormal_1, normal_2, abnormal_2, start;
    private boolean normal_1_tab = true;
    private boolean abnormal_1_tab = true;
    private boolean normal_2_tab = true;
    private boolean abnormal_2_tab = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_Gong:
                Keyboard keyboard = new Keyboard(StosteDetection.this, id_Gong_1);
                keyboard.showWindow(v);
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
        }
    }

    private void init() {
        id_Gong = (PercentLinearLayout) findViewById(R.id.id_Gong);
        id_Gong.setOnClickListener(this);
        id_Gong_1 = (TextView) findViewById(R.id.id_Gong_1);
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
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection);
        init();
    }

}
