package tysheng.sxbus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import tysheng.sxbus.bean.Star;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STAR".
*/
public class StarDao extends AbstractDao<Star, Long> {

    public static final String TABLENAME = "STAR";

    /**
     * Properties of entity Star.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MainId = new Property(0, Long.class, "mainId", true, "_id");
        public final static Property TableName = new Property(1, String.class, "tableName", false, "TABLE_NAME");
        public final static Property IsStar = new Property(2, Boolean.class, "isStar", false, "IS_STAR");
        public final static Property SortId = new Property(3, Long.class, "sortId", false, "SORT_ID");
        public final static Property Id = new Property(4, String.class, "id", false, "ID");
        public final static Property LocalLineId = new Property(5, String.class, "localLineId", false, "LOCAL_LINE_ID");
        public final static Property EndStationName = new Property(6, String.class, "endStationName", false, "END_STATION_NAME");
        public final static Property LineName = new Property(7, String.class, "lineName", false, "LINE_NAME");
        public final static Property StartStationName = new Property(8, String.class, "startStationName", false, "START_STATION_NAME");
        public final static Property UpdateTime = new Property(9, String.class, "updateTime", false, "UPDATE_TIME");
    }


    public StarDao(DaoConfig config) {
        super(config);
    }
    
    public StarDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STAR\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: mainId
                "\"TABLE_NAME\" TEXT," + // 1: tableName
                "\"IS_STAR\" INTEGER," + // 2: isStar
                "\"SORT_ID\" INTEGER," + // 3: sortId
                "\"ID\" TEXT," + // 4: id
                "\"LOCAL_LINE_ID\" TEXT," + // 5: localLineId
                "\"END_STATION_NAME\" TEXT," + // 6: endStationName
                "\"LINE_NAME\" TEXT," + // 7: lineName
                "\"START_STATION_NAME\" TEXT," + // 8: startStationName
                "\"UPDATE_TIME\" TEXT);"); // 9: updateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STAR\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Star entity) {
        stmt.clearBindings();
 
        Long mainId = entity.getMainId();
        if (mainId != null) {
            stmt.bindLong(1, mainId);
        }
 
        String tableName = entity.getTableName();
        if (tableName != null) {
            stmt.bindString(2, tableName);
        }
 
        Boolean isStar = entity.getIsStar();
        if (isStar != null) {
            stmt.bindLong(3, isStar ? 1L: 0L);
        }
 
        Long sortId = entity.getSortId();
        if (sortId != null) {
            stmt.bindLong(4, sortId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(5, id);
        }
 
        String localLineId = entity.getLocalLineId();
        if (localLineId != null) {
            stmt.bindString(6, localLineId);
        }
 
        String endStationName = entity.getEndStationName();
        if (endStationName != null) {
            stmt.bindString(7, endStationName);
        }
 
        String lineName = entity.getLineName();
        if (lineName != null) {
            stmt.bindString(8, lineName);
        }
 
        String startStationName = entity.getStartStationName();
        if (startStationName != null) {
            stmt.bindString(9, startStationName);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(10, updateTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Star entity) {
        stmt.clearBindings();
 
        Long mainId = entity.getMainId();
        if (mainId != null) {
            stmt.bindLong(1, mainId);
        }
 
        String tableName = entity.getTableName();
        if (tableName != null) {
            stmt.bindString(2, tableName);
        }
 
        Boolean isStar = entity.getIsStar();
        if (isStar != null) {
            stmt.bindLong(3, isStar ? 1L: 0L);
        }
 
        Long sortId = entity.getSortId();
        if (sortId != null) {
            stmt.bindLong(4, sortId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(5, id);
        }
 
        String localLineId = entity.getLocalLineId();
        if (localLineId != null) {
            stmt.bindString(6, localLineId);
        }
 
        String endStationName = entity.getEndStationName();
        if (endStationName != null) {
            stmt.bindString(7, endStationName);
        }
 
        String lineName = entity.getLineName();
        if (lineName != null) {
            stmt.bindString(8, lineName);
        }
 
        String startStationName = entity.getStartStationName();
        if (startStationName != null) {
            stmt.bindString(9, startStationName);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(10, updateTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Star readEntity(Cursor cursor, int offset) {
        Star entity = new Star( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // mainId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tableName
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // isStar
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // sortId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // localLineId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // endStationName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // lineName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // startStationName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // updateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Star entity, int offset) {
        entity.setMainId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTableName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIsStar(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setSortId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLocalLineId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEndStationName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLineName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStartStationName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUpdateTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Star entity, long rowId) {
        entity.setMainId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Star entity) {
        if(entity != null) {
            return entity.getMainId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Star entity) {
        return entity.getMainId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
