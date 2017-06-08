package com.join.activity;

import android.app.Activity;
import android.os.Bundle;

import com.join.R;
import com.join.UDisk.UDiskToSD;

import java.io.File;

/**
 * Created by join on 2017/5/7.
 */

public class Greet extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard);
        UDiskToSD diskToSD = new UDiskToSD(this, "/storage/emulated/0/CreateCare" + File.separator + "demo.xls");

    }


}
