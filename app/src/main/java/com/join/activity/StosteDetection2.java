package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.join.R;
import com.join.utils.CustomToast;

/**
 * Created by join on 2017/5/15.
 */

public class StosteDetection2 extends Activity implements View.OnClickListener {
    private Button print, bu_return;
    private ImageView icon_1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_2);
        init();
    }

    private void init() {
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
                CustomToast.showToast(this,"正在完善中......");

                break;
            case R.id.bu_return:
                finish();
                break;
        }
    }
}
