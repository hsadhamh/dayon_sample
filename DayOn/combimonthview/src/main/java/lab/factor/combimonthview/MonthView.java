package lab.factor.combimonthview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by hassanhussain on 11/7/2017.
 */

public class MonthView extends RecyclerView {

    @VisibleForTesting
    long mMonthMillis;
    private GridAdapter mAdapter;
    private IDateChangeListener mListener;

    public MonthView(Context context) {
        super(context);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), Constants.SPANS_COUNT));
        setHasFixedSize(true);
        setCalendar(CalendarUtils.today());
    }

    /**
     * Sets listener to be notified when day selection changes
     * @param listener  listener to be notified
     */
    void setOnDateChangeListener(IDateChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets month to display
     * @param monthMillis  month to display in milliseconds
     */
    void setCalendar(long monthMillis) {
        if (CalendarUtils.isNotTime(monthMillis)) {
            throw new IllegalArgumentException("Invalid timestamp value");
        }
        if (CalendarUtils.sameMonth(mMonthMillis, monthMillis)) {
            return;
        }
        mMonthMillis = monthMillis;
        mAdapter = new GridAdapter(monthMillis);
        mAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                if (mListener == null) {
                    return;
                }
                if (payload instanceof GridAdapter.SelectionPayload) {
                    mListener.onSelectedDayChange(((GridAdapter.SelectionPayload) payload).timeMillis);
                }
            }
        });
        setAdapter(mAdapter);
    }
}
