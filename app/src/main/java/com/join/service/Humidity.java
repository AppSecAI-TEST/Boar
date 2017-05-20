package com.join.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;


public class Humidity extends Service {
    private HumidityCallback humidityCallback;
    private boolean connect;
    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        getCallbackData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new HumidityBinder();
    }

    public class HumidityBinder extends Binder {
        public Humidity getHumidity() {
            return Humidity.this;
        }
    }

    public void getCallbackData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect = true;
                while (connect) {
                    if (humidityCallback != null) {
                        int humidiy = getHumidiy();
                        humidityCallback.onHumidityChange(humidiy);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public int getHumidiy() {
        int s = random.nextInt((40)%(40-30+1) + 30);
        return s;
    }

    public void setHumidityCallback(HumidityCallback callback) {
        this.humidityCallback = callback;
    }

    public interface HumidityCallback {
        void onHumidityChange(int data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        connect = false;
    }
}
