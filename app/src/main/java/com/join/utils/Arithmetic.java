package com.join.utils;


import com.join.camera.IPictureCallback3;

import java.io.File;

/**
 * Created by join on 2017/5/26.
 */

public class Arithmetic {

    private IPictureCallback3 iPictureCallback3;
    private int returnState;

    public void setiPictureCallback3(IPictureCallback3 iPictureCallback3) {
        this.iPictureCallback3 = iPictureCallback3;
    }

    public void getArithmetic() {

        new Thread(new Runnable() {


            @Override
            public void run() {

                double[] tem = {0.57143, 10, 80, 15};
                double[] pdTestResult = new double[22];
                for (int i = 0; i < 22; i++) {
                    pdTestResult[i] = 0;
                }
                String resultImage = "/storage/emulated/0/hitester/certify/ResultImgs";
                File file = new File(resultImage);
                if (!file.exists()) {
                    file.mkdir();
                }
                returnState = countSperm("/storage/emulated/0/hitester/certify/", "/storage/emulated/0/hitester/certify/ResultImgs/", tem, pdTestResult);
                iPictureCallback3.photoPrepared3(pdTestResult,returnState);
 /*               Log.e("jjjj", "精子总数" + floats[0]);//被检测的精子总数
                Log.e("jjjj", "密度" + floats[1]);//精子密度
                Log.e("jjjj", "检测的活动的精子数" + floats[2]);//被检测的活动的精子数
                Log.e("jjjj", "活动精子密度" + floats[3]);//活动精子密度
                Log.e("jjjj", "活率" + floats[4]);//精子活率
                Log.e("jjjj", "活力" + floats[5]);//精子活力
                Log.e("jjjj", "有效精子数" + floats[7]);//有效精子数*/


            }
        }).start();
    }


    static {
        System.loadLibrary("mainSperm");
    }

    private native static int countSperm(String pictures, String path, double[] tem, double[] arryout);
}
