package com.join.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.dialog.ManageDialog;
import com.join.utils.PopupWindowUtil;

import java.text.SimpleDateFormat;

/**
 * 检测员ID选择   管理员手机号码
 */

public class IDSelect extends Activity implements View.OnClickListener {

    private Button affirm;
    private ImageView icon;
    private TextView date, time,input;
    private PopupWindowUtil util;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_select);
        initView();
        showDialog();
        util = new PopupWindowUtil(IDSelect.this, input, icon);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm");
        String tateFormat = sDateFormat.format(new java.util.Date());
        date.setText(tateFormat);
    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.icon_click);
        icon.setOnClickListener(this);
        input = (TextView) findViewById(R.id.input);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        affirm = (Button) findViewById(R.id.affirm);
        affirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_click:
                util.showWindow(v);
                icon.setBackgroundResource(R.drawable.aa_49);
                break;
            case R.id.affirm:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
        }
    }


    public void showDialog() {
        showAlertDialog();
    }

    public void showAlertDialog() {

        ManageDialog.Builder builder = new ManageDialog.Builder(this);
/*        builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("提示");*/
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏dialog
                dialog.dismiss();
            }
        });

 /*       builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏dialog
                        dialog.dismiss();
                        System.out.println("------------------点击取消----------------");
                    }
                });*/
        builder.create().show();
    }


}
