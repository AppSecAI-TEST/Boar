package com.join.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.join.greenDaoUtils.DaoMaster;
import com.join.greenDaoUtils.DaoSession;

/**
 * Created by join on 2017/5/3.
 */

public class BaseApplication extends Application {

   private static DaoSession daoSession;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //配置数据库
        setupDatabase();
        context = getApplicationContext();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库shop.db"
       DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "boar.db", null);
        //获取可写数据库
       SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    /**
     * 得到数据库的会话类
     *
     * @return
     */
    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public static Context getContext() {
        return context;
    }
}
