package io.tact.tech.database;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by hsadhamh on 1/23/2018.
 */
public class EventCursor extends CursorWrapper {

    /**
     * {@link android.provider.CalendarContract.Events} query projection
     */
    private static final int PROJECTION_INDEX_ID = 0;
    private static final int PROJECTION_INDEX_TITLE = 1;
    private static final int PROJECTION_INDEX_DATE = 2;

    public EventCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Gets event ID
     * @return  event ID
     */
    public long getId() {
        return getLong(PROJECTION_INDEX_ID);
    }

    /**
     * Gets event title
     * @return  event title
     */
    public String getTitle() {
        return getString(PROJECTION_INDEX_TITLE);
    }

    public long getDate(){
        return getLong(PROJECTION_INDEX_DATE);
    }
}
