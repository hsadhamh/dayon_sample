package io.tact.tech.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by hsadhamh on 1/23/2018.
 */
@Database(entities = {EventItem.class}, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao daoAccess();
}
