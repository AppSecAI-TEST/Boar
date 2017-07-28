package com.join.utils;


import android.util.Log;

import com.join.camera.IPictureCallback3;
import com.join.camera.IPictureCallback4;

import java.io.File;

/**
 * Created by join on 2017/5/26.
 */

public class Arithmetic {
    private String TAG = "jjjArithmetic";
    private IPictureCallback3 iPictureCallback3;
    private IPictureCallback4 iPictureCallback4;
    private int returnState;

    public void setiPictureCallback3(IPictureCallback3 iPictureCallback3) {
        this.iPictureCallback3 = iPictureCallback3;
    }

    public void setiPictureCallback4(IPictureCallback4 iPictureCallback4) {
        this.iPictureCallback4 = iPictureCallback4;
    }

    public boolean getArithmetic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                double[] tem = {0.6472, 10, 80, 15};
                double[] pdTestResult = new double[22];
                for (int i = 0; i < 22; i++) {
                    pdTestResult[i] = 0;
                }
                String resultImage = "/storage/emulated/0/CreateCare/ResultImgs";
                String pictures = iPictureCallback4.photoPrepared4();
                File file = new File(resultImage);
                if (!file.exists()) {
                    file.mkdir();
                }
                returnState = countSperm(pictures + "/", resultImage + "/", tem, pdTestResult);
                Log.e(TAG, "run: " + pictures);
                if (pdTestResult != null) {
                    iPictureCallback3.photoPrepared3(pdTestResult, returnState);

                }
                Log.e(TAG, "精子总数" + returnState);
                Log.e(TAG, "精子总数" + pdTestResult[0]);//被检测的精子总数
                Log.e(TAG, "密度" + pdTestResult[1]);//精子密度
                Log.e(TAG, "检测的活动的精子数" + pdTestResult[2]);//被检测的活动的精子数
                Log.e(TAG, "活动精子密度" + pdTestResult[3]);//活动精子密度
                Log.e(TAG, "活率" + pdTestResult[4]);//精子活率
                Log.e(TAG, "活力" + pdTestResult[5]);//精子活力
                Log.e(TAG, "有效精子数" + pdTestResult[7]);//有效精子数


            }
        }).start();
        return true;
    }

    static {
        System.loadLibrary("mainSperm");
    }

    private native static int countSperm(String pictures, String path, double[] tem, double[] arryout);
}
