package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.join.R;
import com.join.brightnessLibrary.BubbleSeekBar;

import java.util.Locale;

import static com.join.R.layout.brightness;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public class Brightness extends Activity {
    private String TAG="jjjBrightness";
    private BubbleSeekBar bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(brightness);
        bar= (BubbleSeekBar) findViewById(R.id.bar);

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
                Log.e(TAG, "getProgressOnActionUp: "+s +"\n");

                    Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,progress);

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
}
