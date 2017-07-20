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
import com.zhy.android.percent.support.PercentLinearLayout;

/**
 *
 */

public class WindowSelect extends Activity implements View.OnClickListener {
    private String TAG = "jjjWindowSelect";
    private PercentLinearLayout windows_1, windows_2, windows_3, windows_4;
    private Button continue_ws;
    private ImageView icon;
    private TextView text_ws_1, text_ws_2, text_ws_3, text_ws_4;
    private int win_tag_1, win_tag_2, win_tag_3, win_tag_4;
    private int flags;
    private Intent intent;
    private String function;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windows_select);
        initView();
        flags = getIntent().getFlags();
        function=getIntent().getStringExtra("function");
    }

    private void initView() {
        windows_1 = (PercentLinearLayout) findViewById(R.id.windows_1);
        windows_1.setOnClickListener(this);
        windows_2 = (PercentLinearLayout) findViewById(R.id.windows_2);
        windows_2.setOnClickListener(this);
        windows_3 = (PercentLinearLayout) findViewById(R.id.windows_3);
        windows_3.setOnClickListener(this);
        windows_4 = (PercentLinearLayout) findViewById(R.id.windows_4);
        windows_4.setOnClickListener(this);
        icon = (ImageView) findViewById(R.id.icon);
        icon.setOnClickListener(this);
        continue_ws = (Button) findViewById(R.id.continue_ws);
        continue_ws.setOnClickListener(this);
        text_ws_1 = (TextView) findViewById(R.id.text_ws_1);
        text_ws_2 = (TextView) findViewById(R.id.text_ws_2);
        text_ws_3 = (TextView) findViewById(R.id.text_ws_3);
        text_ws_4 = (TextView) findViewById(R.id.text_ws_4);
        intent = new Intent();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.windows_1:
                win_tag_1++;
                if (win_tag_1 % 2 != 0) {
                    windows_1.setBackgroundResource(R.mipmap.n_007);
                    text_ws_1.setTextColor(getResources().getColor(R.color.green));
                } else {
                    windows_1.setBackgroundResource(R.mipmap.n_008);
                    text_ws_1.setTextColor(getResources().getColor(R.color.black));
                }
                break;
            case R.id.windows_2:
                win_tag_2++;
                if (win_tag_2 % 2 != 0) {
                    windows_2.setBackgroundResource(R.mipmap.n_007);
                    text_ws_2.setTextColor(getResources().getColor(R.color.green));
                } else {
                    windows_2.setBackgroundResource(R.mipmap.n_008);
                    text_ws_2.setTextColor(getResources().getColor(R.color.black));
                }
                break;
            case R.id.windows_3:
                win_tag_3++;
                if (win_tag_3 % 2 != 0) {
                    windows_3.setBackgroundResource(R.mipmap.n_007);
                    text_ws_3.setTextColor(getResources().getColor(R.color.green));
                } else {
                    windows_3.setBackgroundResource(R.mipmap.n_008);
                    text_ws_3.setTextColor(getResources().getColor(R.color.black));
                }
                break;
            case R.id.windows_4:
                win_tag_4++;
                if (win_tag_4 % 2 != 0) {
                    windows_4.setBackgroundResource(R.mipmap.n_007);
                    text_ws_4.setTextColor(getResources().getColor(R.color.green));
                } else {
                    windows_4.setBackgroundResource(R.mipmap.n_008);
                    text_ws_4.setTextColor(getResources().getColor(R.color.black));
                }
                break;
            case R.id.continue_ws:
                int[] winArray = new int[4];
                if (win_tag_1 % 2 != 0) {
                    winArray[0] = 1;
                }
                if (win_tag_2 % 2 != 0) {
                    winArray[1] = 2;
                }
                if (win_tag_3 % 2 != 0) {
                    winArray[2] = 3;
                }
                if (win_tag_4 % 2 != 0) {
                    winArray[3] = 4;
                }
                Bundle bundle = new Bundle();
                if (flags == 1) {

                    bundle.putIntArray("windowSelect", winArray);
                    bundle.putString("idSelect",function);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.StosteDetection");
                    startActivity(intent);
                } else if (flags == 2) {
                    bundle.putIntArray("windowSelect", winArray);
                    bundle.putString("idSelect",function);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.StosteDetectionDiluent");
                    startActivity(intent);
                }
                break;

        }
    }
}
