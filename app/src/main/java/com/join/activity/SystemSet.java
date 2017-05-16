package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.join.R;

/**
 * Created by join on 2017/5/15.
 */

public class SystemSet extends Activity implements View.OnClickListener {
    private Button affirm1, affirm2, affirm3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_set);
        init();
    }

    private void init() {
        affirm1 = (Button) findViewById(R.id.affirm1);
        affirm1.setOnClickListener(this);
        affirm2 = (Button) findViewById(R.id.affirm2);
        affirm2.setOnClickListener(this);
        affirm3 = (Button) findViewById(R.id.affirm3);
        affirm3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.affirm1:
                Toast.makeText(this, "正在完善中", Toast.LENGTH_LONG).show();
                break;
            case R.id.affirm2:
                Intent intent = new Intent();
                intent.setAction("com.join.SystemSet1");
                startActivity(intent);
                break;
            case R.id.affirm3:
                finish();
                break;
        }
    }
}
