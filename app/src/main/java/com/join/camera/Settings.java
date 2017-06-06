package com.join.camera;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设置路径 去的路径
 */
public class Settings {

    private Settings() {
        FileUtil.mkdir(storagePath);
    }

    private String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath().concat(File.separator)
            .concat("CreateCare"); // /ExternalStorageDirectory/appName

    static Settings instance;

    public static Settings get() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getLogPath() {
        return storagePath + File.separator + "log";
    }

    public String getPhotoPath() {
        return storagePath + File.separator + "photo";
    }

    public String getCertifyPath() {
        SimpleDateFormat stime = new SimpleDateFormat("yy-MM-dd hh:mm");
        String timeFormat = stime.format(new Date());
        return storagePath + File.separator + timeFormat;
    }


}
