package com.join.greenDaoUtils;

import com.join.app.BaseApplication;

import java.util.List;

/**
 * Created by join on 2017/5/3.
 */

public class OperationDao {
    /**
     * 增加
     *
     * @param storage
     */
    public static void addData(Storage storage) {
        //如果有ID Unique 重复了就替换
        // BaseApplication.getDaoInstant().getStorageDao().insertOrReplace(storage);
        //如果ID unique重复了就报错
        BaseApplication.getDaoInstant().getStorageDao().insert(storage);
    }

    /**
     * 删除
     *
     * @param id
     */
    public static void deleteData(long id) {
        BaseApplication.getDaoInstant().getStorageDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param storage
     */
    public static void updateLove(Storage storage) {
        BaseApplication.getDaoInstant().getStorageDao().update(storage);
    }

    /**
     * 查询条件为Type=TYPE_LOVE的数据
     *
     * @return
     */
    public static List<Storage> queryLove(String type) {
        return BaseApplication.getDaoInstant().getStorageDao().queryBuilder().where(StorageDao.Properties.Type.eq(type)).list();
    }

    /**
     * 查询全部数据
     */
    public static List<Storage> queryAll() {
        return BaseApplication.getDaoInstant().getStorageDao().loadAll();
    }
}
