package com.join.UDisk;

import android.content.Context;
import android.util.Log;

import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;

import java.io.File;
import java.util.List;

/**
 * 把数据转成Excel,然后从sd卡复制到U盘
 */

public class SaveToExcelAndSD {
    private Context context;
    private String TAG = "jjjSaveToExcelAndSD";


    public SaveToExcelAndSD(Context context) {
        this.context = context;
    }

    public boolean saveToExcelAndSD(final String path) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                List<Storage> storages = OperationDao.queryAll();
                int size = storages.size();
                Log.e(TAG, "start:" + size);
                for (int i = 0; i < size; i++) {
                    Storage storage = storages.get(i);
                    String date = storage.getDate();
                    String time = storage.getTime();
                    String operator = storage.getOperator();
                    String type = storage.getType();
                    String density = storage.getDensity();
                    String vitality = storage.getVitality();
                    String motilityRate = storage.getMotilityRate();
                    String result = storage.getResult();
                    String number = storage.getNumber();
                    String capacity = storage.getCapacity();
                    String color = storage.getColor();
                    String smell = storage.getSmell();
                    String milliliter = storage.getMilliliter();
                    int checkoutDate = storage.getCheckoutDate();
                    String checkoutTime = storage.getCheckoutTime();
                    String motileSperms = storage.getMotileSperms();

                    //保存成Excel
                    String resultImage = "/storage/emulated/0/CreateCare" + File.separator + "demo.xls";
                    SaveToExcel saveToExcel = new SaveToExcel(path);
                    if (type.equals("精液原液")) {
                        Log.e(TAG, "run: " + "保存的是精液原液");
                        saveToExcel.writeToExcel(new String[]{checkoutDate + "", checkoutTime, type, operator, date, time,
                                number, milliliter, color, smell, density, vitality, motilityRate, "-", "-", result});

                    } else {
                        Log.e(TAG, "run: " + "保存的是稀释精液");
                        saveToExcel.writeToExcel(new String[]{checkoutDate + "", checkoutTime, type, operator, "-", "-",
                                number, "-", color, smell, density, vitality, motilityRate, motileSperms, capacity, result});

                    }
                }
            }
        }).start();
        return true;
    }

}
