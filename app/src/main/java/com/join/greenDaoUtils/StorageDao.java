package com.join.greenDaoUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STORAGE".
*/
public class StorageDao extends AbstractDao<Storage, Long> {

    public static final String TABLENAME = "STORAGE";

    /**
     * Properties of entity Storage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Color = new Property(1, String.class, "color", false, "COLOR");
        public final static Property Smell = new Property(2, String.class, "smell", false, "SMELL");
        public final static Property Date = new Property(3, String.class, "date", false, "DATE");
        public final static Property Time = new Property(4, String.class, "time", false, "TIME");
        public final static Property CheckoutDate = new Property(5, int.class, "checkoutDate", false, "CHECKOUT_DATE");
        public final static Property CheckoutTime = new Property(6, String.class, "checkoutTime", false, "CHECKOUT_TIME");
        public final static Property Number = new Property(7, String.class, "number", false, "NUMBER");
        public final static Property Operator = new Property(8, String.class, "operator", false, "OPERATOR");
        public final static Property Type = new Property(9, String.class, "type", false, "TYPE");
        public final static Property Density = new Property(10, String.class, "density", false, "DENSITY");
        public final static Property Vitality = new Property(11, String.class, "vitality", false, "VITALITY");
        public final static Property MotilityRate = new Property(12, String.class, "motilityRate", false, "MOTILITY_RATE");
        public final static Property Copies = new Property(13, String.class, "copies", false, "COPIES");
        public final static Property Add = new Property(14, String.class, "add", false, "ADD");
        public final static Property Result = new Property(15, String.class, "result", false, "RESULT");
        public final static Property Capacity = new Property(16, String.class, "capacity", false, "CAPACITY");
        public final static Property Milliliter = new Property(17, String.class, "milliliter", false, "MILLILITER");
        public final static Property MotileSperms = new Property(18, String.class, "motileSperms", false, "MOTILE_SPERMS");
    }


    public StorageDao(DaoConfig config) {
        super(config);
    }
    
    public StorageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STORAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"COLOR\" TEXT," + // 1: color
                "\"SMELL\" TEXT," + // 2: smell
                "\"DATE\" TEXT," + // 3: date
                "\"TIME\" TEXT," + // 4: time
                "\"CHECKOUT_DATE\" INTEGER NOT NULL ," + // 5: checkoutDate
                "\"CHECKOUT_TIME\" TEXT," + // 6: checkoutTime
                "\"NUMBER\" TEXT," + // 7: number
                "\"OPERATOR\" TEXT," + // 8: operator
                "\"TYPE\" TEXT," + // 9: type
                "\"DENSITY\" TEXT," + // 10: density
                "\"VITALITY\" TEXT," + // 11: vitality
                "\"MOTILITY_RATE\" TEXT," + // 12: motilityRate
                "\"COPIES\" TEXT," + // 13: copies
                "\"ADD\" TEXT," + // 14: add
                "\"RESULT\" TEXT," + // 15: result
                "\"CAPACITY\" TEXT," + // 16: capacity
                "\"MILLILITER\" TEXT," + // 17: milliliter
                "\"MOTILE_SPERMS\" TEXT);"); // 18: motileSperms
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STORAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Storage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(2, color);
        }
 
        String smell = entity.getSmell();
        if (smell != null) {
            stmt.bindString(3, smell);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(4, date);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
        stmt.bindLong(6, entity.getCheckoutDate());
 
        String checkoutTime = entity.getCheckoutTime();
        if (checkoutTime != null) {
            stmt.bindString(7, checkoutTime);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(8, number);
        }
 
        String operator = entity.getOperator();
        if (operator != null) {
            stmt.bindString(9, operator);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(10, type);
        }
 
        String density = entity.getDensity();
        if (density != null) {
            stmt.bindString(11, density);
        }
 
        String vitality = entity.getVitality();
        if (vitality != null) {
            stmt.bindString(12, vitality);
        }
 
        String motilityRate = entity.getMotilityRate();
        if (motilityRate != null) {
            stmt.bindString(13, motilityRate);
        }
 
        String copies = entity.getCopies();
        if (copies != null) {
            stmt.bindString(14, copies);
        }
 
        String add = entity.getAdd();
        if (add != null) {
            stmt.bindString(15, add);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(16, result);
        }
 
        String capacity = entity.getCapacity();
        if (capacity != null) {
            stmt.bindString(17, capacity);
        }
 
        String milliliter = entity.getMilliliter();
        if (milliliter != null) {
            stmt.bindString(18, milliliter);
        }
 
        String motileSperms = entity.getMotileSperms();
        if (motileSperms != null) {
            stmt.bindString(19, motileSperms);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Storage entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(2, color);
        }
 
        String smell = entity.getSmell();
        if (smell != null) {
            stmt.bindString(3, smell);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(4, date);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
        stmt.bindLong(6, entity.getCheckoutDate());
 
        String checkoutTime = entity.getCheckoutTime();
        if (checkoutTime != null) {
            stmt.bindString(7, checkoutTime);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(8, number);
        }
 
        String operator = entity.getOperator();
        if (operator != null) {
            stmt.bindString(9, operator);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(10, type);
        }
 
        String density = entity.getDensity();
        if (density != null) {
            stmt.bindString(11, density);
        }
 
        String vitality = entity.getVitality();
        if (vitality != null) {
            stmt.bindString(12, vitality);
        }
 
        String motilityRate = entity.getMotilityRate();
        if (motilityRate != null) {
            stmt.bindString(13, motilityRate);
        }
 
        String copies = entity.getCopies();
        if (copies != null) {
            stmt.bindString(14, copies);
        }
 
        String add = entity.getAdd();
        if (add != null) {
            stmt.bindString(15, add);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(16, result);
        }
 
        String capacity = entity.getCapacity();
        if (capacity != null) {
            stmt.bindString(17, capacity);
        }
 
        String milliliter = entity.getMilliliter();
        if (milliliter != null) {
            stmt.bindString(18, milliliter);
        }
 
        String motileSperms = entity.getMotileSperms();
        if (motileSperms != null) {
            stmt.bindString(19, motileSperms);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Storage readEntity(Cursor cursor, int offset) {
        Storage entity = new Storage( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // color
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // smell
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // date
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // time
            cursor.getInt(offset + 5), // checkoutDate
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // checkoutTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // number
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // operator
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // type
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // density
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // vitality
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // motilityRate
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // copies
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // add
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // result
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // capacity
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // milliliter
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18) // motileSperms
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Storage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setColor(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSmell(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCheckoutDate(cursor.getInt(offset + 5));
        entity.setCheckoutTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setNumber(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setOperator(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setType(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setDensity(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setVitality(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setMotilityRate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCopies(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAdd(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setResult(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCapacity(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setMilliliter(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setMotileSperms(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Storage entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Storage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Storage entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
