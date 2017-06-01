package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.join.R;

/**
 *
 */

public class StosteDetection22 extends Activity {
    private TextView density, motilityRate, date, time, vitality, motileSperms, capacity, operator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_2_2);
    }

}
