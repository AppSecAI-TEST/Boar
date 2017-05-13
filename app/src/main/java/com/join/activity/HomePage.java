package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.join.R;

/**
 * 欢迎界面
 */

public class HomePage extends Activity {

    private AnimatedCircleLoadingView animatedCircleLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
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
                        if (i == 100) {
                            Intent intent = new Intent();
                            intent.setAction("com.join.idselect");
                            startActivity(intent);
                            finish();
                        }
                    }
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
