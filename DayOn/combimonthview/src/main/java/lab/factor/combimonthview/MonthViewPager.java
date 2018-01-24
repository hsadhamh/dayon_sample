package lab.factor.combimonthview;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hassanhussain on 11/8/2017.
 */

public class MonthViewPager extends ViewPager {

    private MonthViewPagerAdapter mPagerAdapter;
    private IChangeListener mListener;
    private CalendarAdapter mCalendarAdapter;

    private final IDateChangeListener mDateChangeListener = new IDateChangeListener() {
                @Override
                public void onSelectedDayChange(long dayMillis) {
                    // this should come from a page, only notify its neighbors
                    mPagerAdapter.setSelectedDay(getCurrentItem(), dayMillis, false);
                    notifyDayChange(dayMillis);
                }
            };

    public MonthViewPager(Context context) {
        super(context, null);
    }

    public MonthViewPager(Context context, AttributeSet attrs) {
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
     * Sets listener to be notified upon calendar view change events
     * @param listener    listener to be notified
     */
    public void setOnChangeListener(IChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets adapter for calendar events
     * {@link #deactivate()} should be called when appropriate
     * to deactivate active data bindings
     * @param adapter    calendar events adapter
     * @see {@link #deactivate()}
     */
    public void setCalendarAdapter(@NonNull CalendarAdapter adapter) {
        mCalendarAdapter = adapter;
        mCalendarAdapter.setCalendarView(this);
        loadEvents(getCurrentItem());
    }

    /**
     * Clears any active data bindings from adapter,
     * but keeps view state and triggers rebinding data
     */
    public void invalidateData() {
        mPagerAdapter.invalidate();
        loadEvents(getCurrentItem());
    }

    /**
     * Clears any active data bindings from adapter,
     * resets view state to initial state and triggers rebinding data
     */
    public void reset() {
        deactivate();
        init();
        loadEvents(getCurrentItem());
    }

    private void notifyDayChange(long dayMillis) {
        if (mListener != null) {
            mListener.onSelectedDayChange(dayMillis);
        }
    }

    private void init() {
        mPagerAdapter = new MonthViewPagerAdapter(mDateChangeListener);
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
                    loadEvents(getCurrentItem());
                } else if (state == SCROLL_STATE_DRAGGING) {
                    mDragging = true;
                }
            }
        });
    }

    private void toFirstDay(int position) {
        mPagerAdapter.setSelectedDay(position,
                CalendarUtils.monthFirstDay(mPagerAdapter.getMonth(position)), true);
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

    private void loadEvents(int position) {
       /* if (mCalendarAdapter != null && mPagerAdapter.getCursor(position) == null) {
            mCalendarAdapter.loadEvents(mPagerAdapter.getMonth(position));
        }*/
    }

    /**
     * Clears any active data bindings from adapter
     * @see {@link #setCalendarAdapter(CalendarAdapter)}
     */
    public void deactivate() {
        mPagerAdapter.deactivate();
    }

    /*
    private void swapCursor(long monthMillis, EventCursor cursor) {
        mPagerAdapter.swapCursor(monthMillis, cursor, new PagerContentObserver(monthMillis));
    }
    */
    class PagerContentObserver extends ContentObserver {

        private final long monthMillis;

        public PagerContentObserver(long monthMillis) {
            super(new Handler());
            this.monthMillis = monthMillis;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            // invalidate previous cursor for given month
           // mPagerAdapter.swapCursor(monthMillis, null, null);
            // reload events if given month is active month
            // hidden months will be reloaded upon being swiped to
            if (CalendarUtils.sameMonth(monthMillis, mPagerAdapter.getMonth(getCurrentItem()))) {
                loadEvents(getCurrentItem());
            }
        }
    }
}
