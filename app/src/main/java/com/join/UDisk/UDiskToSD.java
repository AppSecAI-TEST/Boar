package com.join.UDisk;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.github.mjdev.libaums.partition.Partition;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by join on 2017/6/7.
 * http://blog.csdn.net/csdn635406113/article/details/70146041
 */

public class UDiskToSD {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private String TAG = "jjjUDiskToSD";
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;//当前U盘的根目录
    private Context context;


    public UDiskToSD(Context context) {
        this.context = context;
        executorService = Executors.newCachedThreadPool();

    }

    public UsbFile getUsbFile() {

        return this.cFolder;
    }

    /**
     * 注册监听U盘的广播
     */

    public synchronized void registerReceiver() {

        //监听otg插入 拔出
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(mUsbReceiver, usbDeviceStateFilter);
        //注册监听自定义广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);

    }

    /**
     * 关闭广播
     */
    public void closeReceiver() {
        if (mUsbReceiver != null) {
            context.unregisterReceiver(mUsbReceiver);
            mUsbReceiver = null;
        }
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION://接受到自定义广播

                    Log.e(TAG, "onReceive: " + "接收到自定义广播");
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {  //允许权限申请

                        if (usbDevice != null) {

                            Log.e(TAG, "onReceive: " + "用户已授权，可以进行读取操作");

                            readDevice(getUsbMass(usbDevice));
                        } else {

                            Log.e(TAG, "onReceive: " + "未获取到设备信息");
                        }
                    } else {
                        Log.e(TAG, "onReceive: " + "用户未授权，读取失败");

                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED://接收到存储设备插入广播
                    UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device_add != null) {
                        Log.e(TAG, "onReceive: " + "接收到存储设备插入广播，尝试读取");

                        redDeviceList();
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://接收到存储设备拔出广播
                    UsbDevice device_remove = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device_remove != null) {
                        Log.e(TAG, "onReceive: " + "接收到存储设备拔出广播");

                    }
                    break;
            }
        }
    };

    /**
     * 得到U盘的根目录
     *
     * @param device
     */
    private void readDevice(UsbMassStorageDevice device) {
        // before interacting with a device you need to call init()!
        try {
            device.init();//初始化
//          Only uses the first partition on the device
            Partition partition = device.getPartitions().get(0);
            FileSystem currentFs = partition.getFileSystem();
//fileSystem.getVolumeLabel()可以获取到设备的标识
//通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
//            Log.d(TAG, "Capacity: " + currentFs.getCapacity());
//            Log.d(TAG, "Occupied Space: " + currentFs.getOccupiedSpace());
//            Log.d(TAG, "Free Space: " + currentFs.getFreeSpace());
//            Log.d(TAG, "Chunk size: " + currentFs.getChunkSize());

            UsbFile root = currentFs.getRootDirectory();//获取根目录
            String deviceName = currentFs.getVolumeLabel();//获取设备标签
            Log.e(TAG, "readDevice: " + "正在读取U盘" + deviceName);

            // tv_title.setText(deviceName);//设置标题
            cFolder = root;//设置当前文件对象

        } catch (Exception e) {
            e.printStackTrace();
            context.unregisterReceiver(mUsbReceiver);
            Log.e(TAG, "readDevice: " + "读取失败，异常：" + e.getMessage());
        }
    }

    /**
     * 得到一台机器有几个USB设备   并把设备传入readDevice去得到根目录
     */
    public void redDeviceList() {

        Log.e(TAG, "redDeviceList: " + "开始读取设备列表...");
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //获取存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        for (UsbMassStorageDevice device : storageDevices) {//可能有几个 一般只有一个 因为大部分手机只有1个otg插口
            int length = storageDevices.length;
            Log.e(TAG, "redDeviceList: 长度: " + length);
            if (usbManager.hasPermission(device.getUsbDevice())) {//有就直接读取设备是否有权限
                Log.e(TAG, "redDeviceList: " + "检测到有权限，直接读取");

                readDevice(device);
            } else {//没有就去发起意图申请

                Log.e(TAG, "redDeviceList: " + "检测到设备，但是没有权限，进行申请");
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent); //该代码执行后，系统弹出一个对话框，
            }
        }
        if (storageDevices.length == 0) {

            context.unregisterReceiver(mUsbReceiver);
            Log.e(TAG, "redDeviceList: " + "未检测到有任何存储设备插入");
        }

    }

    /**
     * 得到U盘设备
     *
     * @param usbDevice
     * @return
     */
    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    /**
     * 把手机文件写入U盘
     *
     * @param uFile
     */
    private ExecutorService executorService;

    public void readFile(final UsbFile uFile, final String inputPath) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {


                    UsbFile directory = uFile.createFile("demo.xls");

                    // FileInputStream fis = new FileInputStream("/storage/emulated/0/CreateCare" + File.separator + "demo.xls");//读取选择的文件的
                    FileInputStream fis = new FileInputStream(inputPath);//读取选择的文件的
                    OutputStream os1 = new UsbFileOutputStream(directory);//写入U盘的输出流
                    Log.e(TAG, "run: " + "开始写入");
                    int bytesRead = 0;
                    byte[] buffer = new byte[1024];
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os1.write(buffer, 0, bytesRead);
                    }
                    Log.e(TAG, "run: " + "写入成功");
                    os1.flush();
                    os1.close();
                    fis.close();
                } catch (Exception e) {
                    Log.e(TAG, "run: " + "异常");

                }
            }
        });

    }

}
