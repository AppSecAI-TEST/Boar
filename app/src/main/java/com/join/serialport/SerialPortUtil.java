package com.join.serialport;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by join on 2017/4/19.
 */

public class SerialPortUtil {
    private String TAG = SerialPortUtil.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String path = "/dev/ttyUSB0";
    private int baudrate = 9600;
    private static SerialPortUtil portUtil;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;

    /**
     * 用于接收串口返回的数据
     */
    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    /**
     * 设置接收串口的数据的监听器
     *
     * @param dataReceiveListener
     */
    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public static SerialPortUtil getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPortUtil();
            portUtil.onCreate();
        }
        return portUtil;
    }

    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd + "\r\n").getBytes();

        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        // String tail = "\r\n";
        //byte[] tailBuffer = tail.getBytes();
        // byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
        byte[] mBufferTemp = new byte[mBuffer.length];
        // Log.e("jj", mBufferTemp.length + "");
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        //System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);

        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                Log.e("jj", "写失败");
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
            Log.e("jjj", "异常");
        }
        return result;
    }

    private class  ReadThread extends Thread {

        @Override
        public synchronized void run() {
            super.run();

            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null)
                        return;
                    byte[] buffer = new byte[11];
                    size = mInputStream.read(buffer);

                    // Log.e("JJ",""+size);
                    if (size > 0) {
                   /*     if(MyLog.isDyeLevel()){
                            MyLog.log(TAG, MyLog.DYE_LOG_LEVEL, "length is:"+size+",data is:"+new String(buffer, 0, size));
                        }*/
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);

                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        //sendShellCommond1();
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }

}
