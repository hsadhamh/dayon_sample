package io.tact.tech.almanac;

/**
 * Created by hsadhamh on 1/9/2018.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import io.tact.tech.utilities.CalendarUtils;

/**
 * A custom CalendarDate View, in the form of circular {@link ViewPager}
 * that supports month change event and state restoration.
 *
 * The {@link ViewPager} recycles adapter item views as users scroll
 * to first or last item.
 */
public class AlmanacMonthWidget extends ViewPager {

    private MonthViewPagerAdapter mPagerAdapter;
    private OnChangeListener mListener;

    /**
     * Callback interface for calendar view change events
     */
    public interface OnChangeListener {
        /**
         * Fired when selected day has been changed via UI interaction
         * @param dayMillis    selected day in milliseconds
         */
        void onSelectedDayChange(long dayMillis);
    }

    public AlmanacMonthWidget(Context context) {
        this(context, null);
    }

    public AlmanacMonthWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // make this ViewPager's height WRAP_CONTENT
        View child = mPagerAdapter.mViews.get(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int height = child.getMeasuredHeight();
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }

    /**
     * Clears any active data bindings from adapter,
     * resets view state to initial state and triggers rebinding data
     */
    public void reset() {
        init();
    }

    private void init() {
        mPagerAdapter = new MonthViewPagerAdapter();
        setAdapter(mPagerAdapter);
        setCurrentItem(mPagerAdapter.getCount() / 2);

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public boolean mDragging = false; // indicate if page change is from user

            @Override
            public void onPageSelected(int position) {
                if (mDragging) {
                    // sequence: IDLE -> (DRAGGING) -> SETTLING -> onPageSelected -> IDLE
                    // ensures that this will always be triggered before syncPages() for position
                    toFirstDay(position);
                    notifyDayChange(mPagerAdapter.getMonth(position));
                }
                mDragging = false;
                // trigger same scroll state changed logic, which would not be fired if not visible
                if (getVisibility() != VISIBLE) {
                    onPageScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    syncPages(getCurrentItem());
                } else if (state == SCROLL_STATE_DRAGGING) {
                    mDragging = true;
                }
            }
        });
    }

    private void notifyDayChange(long dayMillis) {
        if (mListener != null) {
            mListener.onSelectedDayChange(dayMillis);
        }
    }

    /**
     * shift and recycle pages if we are currently at last or first,
     * ensure that users can peek hidden pages on 2 sides
     * @param position  current item position
     */
    private void syncPages(int position) {
        int first = 0, last = mPagerAdapter.getCount() - 1;
        if (position == last) {
            mPagerAdapter.shiftLeft();
            setCurrentItem(first + 1, false);
        } else if (position == 0) {
            mPagerAdapter.shiftRight();
            setCurrentItem(last - 1, false);
        } else {
            // rebind neighbours due to shifting
            if (position > 0) {
                mPagerAdapter.bind(position - 1);
            }
            if (position < mPagerAdapter.getCount() - 1) {
                mPagerAdapter.bind(position + 1);
            }
        }
    }

    private void toFirstDay(int position) {
        mPagerAdapter.setSelectedDay(position,
                CalendarUtils.monthFirstDay(mPagerAdapter.getMonth(position)), true);
    }

    /**
     * Sets listener to be notified upon calendar view change events
     * @param listener    listener to be notified
     */
    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets selected day, automatically move to next/previous month
     * if given day is not within active month
     * TODO assume that min left month < selectedDay < max right month
     * @param dayMillis   new selected day in milliseconds
     */
    public void setSelectedDay(long dayMillis) {
        // notify active page and its neighbors
        int position = getCurrentItem();
        if (CalendarUtils.monthBefore(dayMillis, mPagerAdapter.mSelectedDayMillis)) {
            mPagerAdapter.setSelectedDay(position - 1, dayMillis, true);
            setCurrentItem(position - 1, true);
        } else if (CalendarUtils.monthAfter(dayMillis, mPagerAdapter.mSelectedDayMillis)) {
            mPagerAdapter.setSelectedDay(position + 1, dayMillis, true);
            setCurrentItem(position + 1, true);
        } else {
            mPagerAdapter.setSelectedDay(position, dayMillis, true);
        }
    }
}