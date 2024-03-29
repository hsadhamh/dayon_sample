package lab.factor.combimonthview;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassanhussain on 11/8/2017.
 */

public class MonthViewPagerAdapter extends PagerAdapter {
    private static final String STATE_FIRST_MONTH_MILLIS = "state:month";
    private static final String STATE_SELECTED_DAY_MILLIS = "state:selectedDay";

    static final int ITEM_COUNT = 5; // buffer, left, active, right, buffer

    @VisibleForTesting
    final List<MonthView> mViews = new ArrayList<>(getCount());
    @VisibleForTesting
    long mSelectedDayMillis = CalendarUtils.today();
    private final List<Long> mMonths = new ArrayList<>(getCount());
    private final IDateChangeListener mListener;



    public MonthViewPagerAdapter(IDateChangeListener listener) {
        mListener = listener;
        int mid = ITEM_COUNT / 2;
        long todayMillis = CalendarUtils.monthFirstDay(CalendarUtils.today());
        for (int i = 0; i < getCount(); i++) {
            mMonths.add(CalendarUtils.addMonths(todayMillis, i - mid));
            mViews.add(null);
            //mCursors.add(null);
        }
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView view = new MonthView(container.getContext());
        view.setLayoutParams(new ViewPager.LayoutParams());
        view.setOnDateChangeListener(mListener);
        mViews.set(position, view);
        container.addView(view); // views are not added in same order as adapter items
        bind(position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((MonthView) object).setOnDateChangeListener(null);
        container.removeView((View) object);
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = new Bundle();
        bundle.putLong(STATE_FIRST_MONTH_MILLIS, mMonths.get(0));
        bundle.putLong(STATE_SELECTED_DAY_MILLIS, mSelectedDayMillis);
        return bundle;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        Bundle savedState = (Bundle) state;
        if (savedState == null) {
            return;
        }
        mSelectedDayMillis = savedState.getLong(STATE_SELECTED_DAY_MILLIS);
        long firstMonthMillis = savedState.getLong(STATE_FIRST_MONTH_MILLIS);
        for (int i = 0; i < getCount(); i++) {
            mMonths.set(i, CalendarUtils.addMonths(firstMonthMillis, i));
        }
    }

    /**
     * Gets month of calendar in given position
     * @param position    adapter position
     * @return  month in milliseconds
     */
    long getMonth(int position) {
        return mMonths.get(position);
    }

    /**
     * Shifts Jan, Feb, Mar, Apr, [May] to Apr, [May], Jun, Jul, Aug
     * Rebinds views in view pool if needed
     */
    void shiftLeft() {
        for (int i = 0; i < getCount() - 2; i++) {
            mMonths.add(CalendarUtils.addMonths(mMonths.remove(0), getCount()));
        }
        // TODO only deactivate non reusable cursors
        for (int i = 0; i < getCount(); i++) {
            //swapCursor(i, null, null);
        }
        // rebind current item (2nd) and 2 adjacent items
        for (int i = 0; i <= 2; i++) {
            bind(i);
        }
    }

    /**
     * Shifts [Jan], Feb, Mar, Apr, May to Oct, Nov, Dec, [Jan], Feb
     * Rebinds views in view pool if needed
     */
    void shiftRight() {
        for (int i = 0; i < getCount() - 2; i++) {
            mMonths.add(0, CalendarUtils.addMonths(mMonths.remove(getCount() - 1), -getCount()));
            //mCursors.add(0, mCursors.remove(getCount() - 1));
        }
        // TODO only deactivate non reusable cursors
        for (int i = 0; i < getCount(); i++) {
            //swapCursor(i, null, null);
        }
        // rebind current item (2nd to last) and 2 adjacent items
        for (int i = 0; i <= 2; i++) {
            bind(getCount() - 1 - i);
        }
    }

    void bind(int position) {
        if (mViews.get(position) != null) {
            mViews.get(position).setCalendar(mMonths.get(position));
        }
        //bindCursor(position);
        bindSelectedDay(position);
    }

    private void bindSelectedDay(int position) {
        if (mViews.get(position) != null) {
            mViews.get(position).setSelectedDay(mSelectedDayMillis);
        }
    }

    /**
     * Sets selected day for page at given position, which either sets new selected day
     * if it falls within that month, or unsets previously selected day if any
     * @param position      page position
     * @param dayMillis     selected day in milliseconds
     * @param notifySelf    true to rebind page at given position, false otherwise
     */
    void setSelectedDay(int position, long dayMillis, boolean notifySelf) {
        mSelectedDayMillis = dayMillis;
        if (notifySelf) {
            bindSelectedDay(position);
        }
        if (position > 0) {
            bindSelectedDay(position - 1);
        }
        if (position < getCount() - 1) {
            bindSelectedDay(position + 1);
        }
    }

    /**
     * Deactivates all previously bound cursors and unregisters their observers
     */
    void deactivate() {
/*        for (EventCursor cursor : mCursors) {
            deactivate(cursor);
        }
*/
    }

    /**
     * Deactivates all previously bound cursors and unregisters their observers,
     * prepares views for new data bindings
     */
    void invalidate() {
     /*   for (int i = 0; i < mCursors.size(); i++) {
            swapCursor(i, null, null);
        }
     */
    }



}
