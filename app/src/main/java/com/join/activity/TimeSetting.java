package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.join.R;
import com.join.three.DateSelectDialog;
import com.join.three.TimeSelectDialog;
import com.zhy.android.percent.support.PercentLinearLayout;

/**
 *
 */

public class TimeSetting extends Activity implements View.OnClickListener {
    private PercentLinearLayout date_t, time_t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);
        initView();

    }

    private void initView() {
        date_t = (PercentLinearLayout) findViewById(R.id.date_t);
        date_t.setOnClickListener(this);
        time_t = (PercentLinearLayout) findViewById(R.id.time_t);
        time_t.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_t:
                DateSelectDialog mChangeAddressDialog = new DateSelectDialog(
                        TimeSetting.this);
               // mChangeAddressDialog.setAddress("四川", "自贡");
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new DateSelectDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String strArea) {
                                // TODO Auto-generated method stub
                                Toast.makeText(TimeSetting.this,
                                        province + "-" + city + "-" + strArea,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                break;
            case R.id.time_t:
                TimeSelectDialog timeSelectDialog = new TimeSelectDialog(TimeSetting.this);
                timeSelectDialog.show();
                timeSelectDialog.setAddresskListener(new TimeSelectDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String province, String city) {
                        Toast.makeText(TimeSetting.this,
                                province + "-" + city,
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
