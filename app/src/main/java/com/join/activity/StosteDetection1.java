package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;

/**
 * Created by join on 2017/5/14.
 */

public class StosteDetection1 extends Activity implements View.OnClickListener {
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private Button bu_return, bu_enter;

    @Override
    public void onClick(View v) {

    }

    private void init() {
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        bu_enter = (Button) findViewById(R.id.bu_enter);
        bu_enter.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoste_detection_1);
        init();
        startLoading();
        startPercentMockThread();
    }


    /**
     * 加载动漫
     */
    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    /**
     * 改变进度的线程
     */
    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Thread.sleep(1500);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(20);
                        changePercent(i);
                        Log.e("jjjj","jjjj");
                        if (i == 100) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    animatedCircleLoadingView.setVisibility(View.GONE);
                                    bu_return.setVisibility(View.GONE);
                                    bu_enter.setVisibility(View.VISIBLE);

                                }
                            });

                        }
                    }
                    Log.e("jjjj","jjjj");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 改变进度条的值,必须要在UI线程
     *
     * @param percent
     */
    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    /**
     * 重置进度
     */
    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }


}
