package com.join.utils;

import android.util.Log;

import com.join.camera.IPictureCallback3;

import java.io.File;

/**
 * Created by join on 2017/5/26.
 */

public class Arithmetic {

    private IPictureCallback3 iPictureCallback3;

    public void setiPictureCallback3(IPictureCallback3 iPictureCallback3) {
        this.iPictureCallback3 = iPictureCallback3;
    }

    public void getArithmetic() {

        new Thread(new Runnable() {
            float[] returnData = new float[6];

            @Override
            public void run() {
                float[] tem = {1, 7, 80, 15};
                String resultImage = "/storage/emulated/0/hitester/certify/ResultImgs";
                File file = new File(resultImage);
                if (!file.exists()) {
                    file.mkdir();
                }
                float[] floats = countSperm("/storage/emulated/0/hitester/certify/", "/storage/emulated/0/hitester/certify/ResultImgs/", tem);
                iPictureCallback3.photoPrepared3(floats);
                Log.e("jjjj", "精子总数" + floats[0]);//被检测的精子总数
                Log.e("jjjj", "密度" + floats[1]);//精子密度
                Log.e("jjjj", "检测的活动的精子数" + floats[2]);//被检测的活动的精子数
                Log.e("jjjj", "活动精子密度" + floats[3]);//活动精子密度
                Log.e("jjjj", "活率" + floats[4]);//精子活率
                Log.e("jjjj", "活力" + floats[5]);//精子活力
                Log.e("jjjj", "直线运动精子数" + floats[7]);//直线运动精子数


            }
        }).start();
    }


    static {
        System.loadLibrary("mainSperm");
    }

    private native static float[] countSperm(String pictures, String path, float[] tem);
}
