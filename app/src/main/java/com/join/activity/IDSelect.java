package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.utils.PopupWindowUtil;

/**
 * Created by join on 2017/5/8.
 */

public class IDSelect extends Activity implements View.OnClickListener {

    private Button affirm;
    private ImageView icon;
    private EditText input;
    private TextView date, time;
    private PopupWindowUtil util;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_select);
        initView();
         util = new PopupWindowUtil(IDSelect.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_click:
                util.showWindow(v);
                Log.e("jjjj", "jjjj");
                break;
        }
    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.icon_click);
        icon.setOnClickListener(this);
    }


}
