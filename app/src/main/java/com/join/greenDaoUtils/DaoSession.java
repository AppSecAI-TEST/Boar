package com.join.greenDaoUtils;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.join.greenDaoUtils.Storage;

import com.join.greenDaoUtils.StorageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig storageDaoConfig;

    private final StorageDao storageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        storageDaoConfig = daoConfigMap.get(StorageDao.class).clone();
        storageDaoConfig.initIdentityScope(type);

        storageDao = new StorageDao(storageDaoConfig, this);

        registerDao(Storage.class, storageDao);
    }
    
    public void clear() {
        storageDaoConfig.clearIdentityScope();
    }

    public StorageDao getStorageDao() {
        return storageDao;
    }

}
