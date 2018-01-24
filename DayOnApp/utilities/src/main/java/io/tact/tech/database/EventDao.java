package io.tact.tech.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

/**
 * Created by hsadhamh on 1/23/2018.
 */
@Dao
public interface EventDao {

    @Insert
    void insertMultipleRecord(EventItem... events);

    @Insert
    void insertMultipleListRecord(List<EventItem> events);

    @Insert
    void insertOnlySingleRecord(EventItem event);

    @Query("SELECT * FROM EventItem")
    List<EventItem> fetchAllData();

    @Query("SELECT * FROM EventItem WHERE slNo =:slID")
    EventItem getSingleRecord(int slID);

    @Query("SELECT * FROM EventItem WHERE dateTime = :date")
    List<EventItem> fetchAllDataByDate(long date);

    @Query("SELECT * FROM EventItem WHERE dateTime = :date")
    Cursor getCursorByDate(long date);

    @Update
    void updateRecord(EventItem university);

    @Delete
    void deleteRecord(EventItem university);
}