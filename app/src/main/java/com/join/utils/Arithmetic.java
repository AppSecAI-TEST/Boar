package com.join.utils;

import android.util.Log;

/**
 * Created by join on 2017/5/26.
 */

public class Arithmetic {

    private static float[] floats;

    public static float[] getArithmetic() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                float[] tem = {1, 10, 1, 15};
                floats = countSperm("/storage/emulated/0/Pictures/TT-2/", "/storage/emulated/0/Pictures/TT-2/ResultImgs/", tem);
                Log.e("jjjj", "" + floats[0]);//被检测的精子总数
                Log.e("jjjj", "" + floats[1]);//精子密度
                Log.e("jjjj", "" + floats[2]);//被检测的活动的精子数
                Log.e("jjjj", "" + floats[3]);//活动精子密度
                Log.e("jjjj", "" + floats[4]);//精子活率
                Log.e("jjjj", "" + floats[5]);//精子活力
                Log.e("jjjj", "" + floats[6]);//精子活力
                Log.e("jjjj", "" + floats[7]);//直线运动精子数


            }
        }).start();
        return floats;
    }

    static {
        System.loadLibrary("mainSperm");
    }

    public native static float[] countSperm(String pictures, String path, float[] tem);
}
