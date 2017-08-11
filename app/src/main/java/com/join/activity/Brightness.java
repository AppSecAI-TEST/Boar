package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;
import com.join.brightnessLibrary.BubbleSeekBar;
import com.join.service.Humidity;

import java.util.Locale;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Brightness extends Activity implements ServiceConnection {
    private String TAG = "jjjBrightness";
    private BubbleSeekBar bar;
    private ImageView icon_1;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brightness);
        humidity = (TextView) findViewById(R.id.humidity);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.join.Function");
                startActivity(intent);
            }
        });
        bar = (BubbleSeekBar) findViewById(R.id.bar);
        bar.getConfigBuilder()
                .bubbleTextSize(50)
                .build();

        bar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                String s = String.format(Locale.CHINA, "onActionUp int:%d, float:%.1f", progress, progressFloat);
                Log.e(TAG, "getProgressOnActionUp: " + s + "\n");

                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);

                int anInt = 0;
                try {
                    anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                Log.e("jjjjjj", "" + anInt);
                    /*Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
                    Brightness.this.getContentResolver().notifyChange(uri, null);*/
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unbindService(this);
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
