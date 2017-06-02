package com.join.utils;

import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;

/**
 * 增加到数据库的帮助类
 */

public class DaoUtil {
    /**
     * 精液原液
     */
    public static void sD2(Storage storage, String number, String operator, String capacity, String date, String time, String copies, String add
            , String density, String vitality, String motilityRate, String smell, String color, String type, String result) {
        storage.setNumber(number);
        storage.setOperator(operator);
        storage.setCapacity(capacity);
        storage.setDate(date);
        storage.setTime(time);
        storage.setCopies(copies);
        storage.setAdd(add);
        storage.setDensity(density);
        storage.setVitality(vitality);
        storage.setMotilityRate(motilityRate);
        storage.setSmell(smell);
        storage.setColor(color);
        storage.setType(type);
        storage.setResult(result);
        OperationDao.addData(storage);
    }

    /**
     * 稀释精液
     */
    public static void sD22(Storage storage, String density, String motilityRate, String date, String time, String smell,
                            String color, String vitality, String motileSperms, String capacity, String operator, String number, String type, String result) {
        storage.setNumber(number);
        storage.setOperator(operator);
        storage.setCapacity(capacity);
        storage.setDate(date);
        storage.setTime(time);
        storage.setDensity(density);
        storage.setVitality(vitality);
        storage.setMotilityRate(motilityRate);
        storage.setSmell(smell);
        storage.setColor(color);
        storage.setMotileSperms(motileSperms);
        storage.setType(type);
        storage.setResult(result);
        OperationDao.addData(storage);

    }
}
