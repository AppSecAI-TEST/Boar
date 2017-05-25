package com.join.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.join.serialport.SerialPortCommand;
import com.join.serialport.SerialPortUtil;
import com.join.utils.Convert;

import java.util.Random;


public class Humidity extends Service implements SerialPortUtil.OnDataReceiveListener {
    private HumidityCallback humidityCallback;
    private boolean connect;
    private Random random;
    private SerialPortUtil instance;
    private String TAG = Humidity.class.getSimpleName();
    private int in;

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        String data = Convert.bytesToHexString(buffer);
        Log.e("jjj", "onDataReceive: " + data);
        if (buffer[5] == 0x04) {

            String substring = data.substring(18, 20);
            in = Integer.valueOf(substring, 16);
            Log.e("jjj", "onDataReceive: " + in);


        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        getCallbackData();
        instance = SerialPortUtil.getInstance();
        instance.setOnDataReceiveListener(this);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int flags = intent.getFlags();
      if (flags==1){
          boolean b = instance.sendBuffer(SerialPortCommand.handshake);
          Log.e("jjj", b + "");
      }
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
                        int humidiy = in;
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
        int s = random.nextInt((40) % (40 - 30 + 1) + 30);
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
