package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.service.Humidity;

/**
 *
 */

public class WindowSelect extends Activity implements View.OnClickListener,ServiceConnection {
    private String TAG = "jjjWindowSelect";
    private Button continue_ws ,win_1,win_2,win_3,win_4;
    private ImageView icon_1;
    private int win_tag_1, win_tag_2, win_tag_3, win_tag_4;
    private int flags;
    private Intent intent;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windows_select);
        initView();
        flags = getIntent().getFlags();
    }
    @Override
    protected void onResume() {
        super.onResume();
/*        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }
    @Override
    protected void onPause() {
        super.onPause();
        //unbindService(this);
    }

    private void initView() {
        humidity = (TextView) findViewById(R.id.humidity);
        win_1 = (Button) findViewById(R.id.win_1);
        win_1.setOnClickListener(this);
        win_2 = (Button) findViewById(R.id.win_2);
        win_2.setOnClickListener(this);
        win_3 = (Button) findViewById(R.id.win_3);
        win_3.setOnClickListener(this);
        win_4 = (Button) findViewById(R.id.win_4);
        win_4.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        continue_ws = (Button) findViewById(R.id.continue_ws);
        continue_ws.setOnClickListener(this);
        intent = new Intent();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.win_1:
                win_tag_1++;
                if (win_tag_1 % 2 != 0) {
                    win_1.setBackgroundResource(R.mipmap.window_select_2);

                } else {
                    win_1.setBackgroundResource(R.mipmap.window_select_1);

                }
                break;
            case R.id.win_2:
                win_tag_2++;
                if (win_tag_2 % 2 != 0) {
                    win_2.setBackgroundResource(R.mipmap.window_select_4);

                } else {
                    win_2.setBackgroundResource(R.mipmap.window_select_3);

                }
                break;
            case R.id.win_3:
                win_tag_3++;
                if (win_tag_3 % 2 != 0) {
                    win_3.setBackgroundResource(R.mipmap.window_select_6);

                } else {
                    win_3.setBackgroundResource(R.mipmap.window_select_5);

                }
                break;
            case R.id.win_4:
                win_tag_4++;
                if (win_tag_4 % 2 != 0) {
                    win_4.setBackgroundResource(R.mipmap.window_select_8);

                } else {
                    win_4.setBackgroundResource(R.mipmap.window_select_7);

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
                    intent.putExtras(bundle);
                    intent.setAction("com.join.StosteDetection");
                    startActivity(intent);
                } else if (flags == 2) {
                    bundle.putIntArray("windowSelect", winArray);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.StosteDetectionDiluent");
                    startActivity(intent);
                }
                break;

            case R.id.icon_1:
                intent.setAction("com.join.function");
                startActivity(intent);
                break;

        }
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        Humidity humidityClass = humidityBinder.getHumidity();
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText(data);
                    }
                });
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
