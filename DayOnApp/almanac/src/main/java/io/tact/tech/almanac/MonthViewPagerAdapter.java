package io.tact.tech.almanac;

/**
 * Created by hsadhamh on 1/9/2018.
 */

import android.support.annotation.VisibleForTesting;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.tact.tech.utilities.CalendarUtils;

/**
 * A circular {@link PagerAdapter}, with a view pool of 5 items:
 * buffer, left, [active], right, buffer
 * Upon user scrolling to a buffer view, {@link ViewPager#setCurrentItem(int)}
 * should be called to wrap around and shift active view to the next non-buffer
 */
class MonthViewPagerAdapter extends PagerAdapter {
    static final int ITEM_COUNT = 5; // buffer, left, active, right, buffer

    @VisibleForTesting final List<MonthView> mViews = new ArrayList<>(getCount());
    @VisibleForTesting long mSelectedDayMillis = CalendarUtils.today();
    private final List<Long> mMonths = new ArrayList<>(getCount());

    public MonthViewPagerAdapter() {
        int mid = ITEM_COUNT / 2;
        long todayMillis = CalendarUtils.monthFirstDay(CalendarUtils.today());
        for (int i = 0; i < getCount(); i++) {
            mMonths.add(CalendarUtils.addMonths(todayMillis, i - mid));
            mViews.add(null);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView view = new MonthView(container.getContext());
        view.setLayoutParams(new ViewPager.LayoutParams());
        mViews.set(position, view);
        container.addView(view); // views are not added in same order as adapter items
        bind(position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
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
        }
        // rebind current item (2nd to last) and 2 adjacent items
        for (int i = 0; i <= 2; i++) {
            bind(getCount() - 1 - i);
        }
    }

    /**
     * Rebinds month, events and selected day for calendar at given position
     * @param position    adapter position
     */
    void bind(int position) {
        if (mViews.get(position) != null) {
            mViews.get(position).setCalendar(mMonths.get(position));
        }
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
}