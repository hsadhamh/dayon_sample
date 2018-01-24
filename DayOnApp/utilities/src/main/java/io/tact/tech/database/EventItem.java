package io.tact.tech.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by hsadhamh on 1/23/2018.
 */
@Entity
public class EventItem {

    @PrimaryKey(autoGenerate = true)
    private int slNo;
    private String name;
    private long dateTime;

    public int getSlNo() { return slNo; }
    public void setSlNo(int slNo) { this.slNo = slNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getDateTime() { return dateTime; }
    public void setDateTime(long date) { this.dateTime = date; }

}
