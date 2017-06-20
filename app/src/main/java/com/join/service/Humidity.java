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
    private CommandCallback commandCallback;
    private boolean connect = true;
    private Random random;
    private SerialPortUtil instance;
    private String TAG = "jjjHumidity";
    private int humidityInt;

    private int commandInt;

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        String data = Convert.bytesToHexString(buffer);

      //  Log.e(TAG, "onDataReceive: " + data);
/*        if (buffer[5] == 0x04) {
            String substring = data.substring(18, 20);
            humidityInt = Integer.valueOf(substring, 16);
            Log.e(TAG, "onDataReceive: " + humidityInt);

        }*/

        if (buffer[5] == 0x03) {
            String substring = data.substring(18, 20);
            commandInt = Integer.valueOf(substring, 16);
            Log.e(TAG, "onDataReceive: " + commandInt + "command");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        random = new Random();
        getCallbackData();
        getCallbackData1();
        instance = SerialPortUtil.getInstance();
        instance.setOnDataReceiveListener(this);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int flags = intent.getFlags();
        Log.e(TAG, "onBind: " + flags);
        if (flags == 1) {
            boolean b = instance.sendBuffer(SerialPortCommand.handshake);
            Log.e(TAG, b + "");
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

                while (connect) {
                    if (humidityCallback != null) {
/*
                        int humidiy = humidityInt;
                        humidityCallback.onHumidityChange(humidiy);*/
                    }
                    if (commandCallback != null) {

                        commandCallback.onCommandResult(commandInt);
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

    public void getCallbackData1() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (connect) {
                    if (humidityCallback != null) {

                        float i = random.nextFloat() + 37;

                        //  String densityS = String.valueOf(Double.parseDouble(String.format("%.3f", density)));
                        String humidiy = String.valueOf(Float.parseFloat(String.format("%.1f", i)));
                        humidityCallback.onHumidityChange(humidiy + "℃");
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 设置温度接口
     *
     * @param callback
     */
    public void setHumidityCallback(HumidityCallback callback) {
        this.humidityCallback = callback;
    }

    /**
     * 设置命令接口
     */
    public void setCommandCallback(CommandCallback commandCallback) {
        this.commandCallback = commandCallback;
    }

    /**
     * 温度回调
     */
    public interface HumidityCallback {
        void onHumidityChange(String data);
    }

    /**
     * 命令回调接口
     */
    public interface CommandCallback {
        void onCommandResult(int data);
    }

    /**
     * 发送命令
     *
     * @param comm
     */
    public void sendCommand(byte[] comm) {
        instance.sendBuffer(comm);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //instance.closeSerialPort();
        commandCallback = null;
        connect = false;
    }
}
