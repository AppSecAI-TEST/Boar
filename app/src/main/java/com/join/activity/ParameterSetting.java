package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.join.R;

/**
 * Created by join on 2017/5/31.
 */

public class ParameterSetting extends Activity implements ImageView.OnClickListener {
    private ImageView icon_1;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_setting);
        init();
    }

    private void init() {
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_1:
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
        }
    }
}
