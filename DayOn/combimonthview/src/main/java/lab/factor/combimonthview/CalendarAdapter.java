package lab.factor.combimonthview;

/**
 * Created by hassanhussain on 11/8/2017.
 */

public class CalendarAdapter {
    private MonthViewPager mCalendarView;

    void setCalendarView(MonthViewPager calendarView) {
        mCalendarView = calendarView;
    }

    /**
     * Loads events for given month. Should call {@link #bindEvents(long, EventCursor)} on complete
     * @param monthMillis    month in milliseconds
     * @see {@link #bindEvents(long, EventCursor)}
     */
    protected void loadEvents(long monthMillis) {
        // override to load events
    }

    /**
     * Binds events for given month that have been loaded via {@link #loadEvents(long)}
     * @param monthMillis    month in milliseconds
     * @param cursor         {@link android.provider.CalendarContract.Events} cursor wrapper

    public final void bindEvents(long monthMillis, EventCursor cursor) {
        //mCalendarView.swapCursor(monthMillis, cursor);
    }
     */
}
