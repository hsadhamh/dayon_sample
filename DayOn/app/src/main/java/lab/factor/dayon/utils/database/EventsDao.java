package lab.factor.dayon.utils.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EVENTS".
*/
public class EventsDao extends AbstractDao<Events, Long> {

    public static final String TABLENAME = "EVENTS";

    /**
     * Properties of entity Events.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GUID = new Property(1, String.class, "GUID", false, "GUID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Property = new Property(3, String.class, "property", false, "PROPERTY");
        public final static Property Category = new Property(4, Long.class, "category", false, "CATEGORY");
        public final static Property Sub_category = new Property(5, Long.class, "sub_category", false, "SUB_CATEGORY");
        public final static Property Locations = new Property(6, String.class, "locations", false, "LOCATIONS");
        public final static Property Flags = new Property(7, Long.class, "flags", false, "FLAGS");
        public final static Property Tags = new Property(8, String.class, "tags", false, "TAGS");
        public final static Property Start_date = new Property(9, Long.class, "start_date", false, "START_DATE");
        public final static Property End_date = new Property(10, Long.class, "end_date", false, "END_DATE");
    }


    public EventsDao(DaoConfig config) {
        super(config);
    }
    
    public EventsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EVENTS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"GUID\" TEXT," + // 1: GUID
                "\"NAME\" TEXT," + // 2: name
                "\"PROPERTY\" TEXT," + // 3: property
                "\"CATEGORY\" INTEGER," + // 4: category
                "\"SUB_CATEGORY\" INTEGER," + // 5: sub_category
                "\"LOCATIONS\" TEXT," + // 6: locations
                "\"FLAGS\" INTEGER," + // 7: flags
                "\"TAGS\" TEXT," + // 8: tags
                "\"START_DATE\" INTEGER," + // 9: start_date
                "\"END_DATE\" INTEGER);"); // 10: end_date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EVENTS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Events entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String GUID = entity.getGUID();
        if (GUID != null) {
            stmt.bindString(2, GUID);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String property = entity.getProperty();
        if (property != null) {
            stmt.bindString(4, property);
        }
 
        Long category = entity.getCategory();
        if (category != null) {
            stmt.bindLong(5, category);
        }
 
        Long sub_category = entity.getSub_category();
        if (sub_category != null) {
            stmt.bindLong(6, sub_category);
        }
 
        String locations = entity.getLocations();
        if (locations != null) {
            stmt.bindString(7, locations);
        }
 
        Long flags = entity.getFlags();
        if (flags != null) {
            stmt.bindLong(8, flags);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(9, tags);
        }
 
        Long start_date = entity.getStart_date();
        if (start_date != null) {
            stmt.bindLong(10, start_date);
        }
 
        Long end_date = entity.getEnd_date();
        if (end_date != null) {
            stmt.bindLong(11, end_date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Events entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String GUID = entity.getGUID();
        if (GUID != null) {
            stmt.bindString(2, GUID);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String property = entity.getProperty();
        if (property != null) {
            stmt.bindString(4, property);
        }
 
        Long category = entity.getCategory();
        if (category != null) {
            stmt.bindLong(5, category);
        }
 
        Long sub_category = entity.getSub_category();
        if (sub_category != null) {
            stmt.bindLong(6, sub_category);
        }
 
        String locations = entity.getLocations();
        if (locations != null) {
            stmt.bindString(7, locations);
        }
 
        Long flags = entity.getFlags();
        if (flags != null) {
            stmt.bindLong(8, flags);
        }
 
        String tags = entity.getTags();
        if (tags != null) {
            stmt.bindString(9, tags);
        }
 
        Long start_date = entity.getStart_date();
        if (start_date != null) {
            stmt.bindLong(10, start_date);
        }
 
        Long end_date = entity.getEnd_date();
        if (end_date != null) {
            stmt.bindLong(11, end_date);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Events readEntity(Cursor cursor, int offset) {
        Events entity = new Events( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // GUID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // property
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // category
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // sub_category
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // locations
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // flags
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // tags
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // start_date
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10) // end_date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Events entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGUID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProperty(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCategory(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setSub_category(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setLocations(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFlags(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setTags(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStart_date(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setEnd_date(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Events entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Events entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Events entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}