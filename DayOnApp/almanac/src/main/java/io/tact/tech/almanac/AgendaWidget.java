package io.tact.tech.almanac;

/**
 * Created by hsadhamh on 1/11/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import io.tact.tech.utilities.CalendarUtils;
import io.tact.tech.utilities.ViewUtils;


public class AgendaWidget extends RecyclerView {

    private OnDateChangeListener mListener;
    private AgendaAdapter mAdapter;
    // represent top scroll position to be set programmatically
    private int mPendingScrollPosition = NO_POSITION;
    private long mPrevTimeMillis = CalendarUtils.NO_TIME_MILLIS;
    private Bundle mAdapterSavedState;
    private final int[] mColors;

    /**
     * Callback interface for active (top) date change event
     */
    public interface OnDateChangeListener {
        /**
         * Fired when active (top) date has been changed via UI interaction
         * @param dayMillis  new active (top) day in milliseconds
         */
        void onSelectedDayChange(long dayMillis);
    }

    public AgendaWidget(Context context) {
        this(context, null);
    }

    public AgendaWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgendaWidget(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        if (isInEditMode()) {
            mColors = new int[]{ContextCompat.getColor(context, android.R.color.transparent)};
            setAdapter(new AgendaAdapter(context) {});
        } else {
            mColors = ViewUtils.getCalendarColors(context);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return  super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (dy != 0) { // avoid loading more or triggering notification on 1st layout
            loadMore();
            notifyDateChange();
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE && mPendingScrollPosition != NO_POSITION) {
            mPendingScrollPosition = NO_POSITION; // clear pending
            mAdapter.unlockBinding();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null && !(adapter instanceof AgendaAdapter)) {
            throw new IllegalArgumentException("Adapter must be an instance of AgendaAdapter");
        }
        mAdapter = (AgendaAdapter) adapter;
        if (mAdapter != null) {
            mAdapter.append(getContext());
            getLinearLayoutManager().scrollToPosition(mAdapter.getItemCount() / 2);
        }
        super.setAdapter(mAdapter);
    }

    /**
     * Sets listener to be notified when active (top) date in agenda changes
     * @param listener  listener to be notified
     */
    public void setOnDateChangeListener(OnDateChangeListener listener) {
        mListener = listener;
    }

    /**
     * Sets active (top) date to be displayed
     * @param dayMillis  new active (top) date in milliseconds
     */
    public void setSelectedDay(long dayMillis) {
        if (mAdapter == null) {
            return;
        }
        mPendingScrollPosition = mAdapter.getPosition(getContext(), dayMillis);
        if (mPendingScrollPosition >= 0) {
            // lock binding to prevent loading events that might offset scroll position
            mAdapter.lockBinding();
            smoothScrollToPosition(mPendingScrollPosition);
        }
    }

    /**
     * Clears previous bindings if any, resets view to initial state and triggers rebinding data
     */
    public void reset() {
        // clear view state
        mPendingScrollPosition = NO_POSITION;
        mPrevTimeMillis = CalendarUtils.NO_TIME_MILLIS;
        mAdapterSavedState = null;
        if (mAdapter != null) {
            int originalCount = mAdapter.getItemCount();
            mAdapter.lockBinding();
            mAdapter.deactivate();
            mAdapter.notifyItemRangeRemoved(0, originalCount);
            mAdapter.append(getContext());
            mAdapter.notifyItemRangeInserted(0, mAdapter.getItemCount());
            setSelectedDay(CalendarUtils.today());
        }
    }

    /**
     * Clears previous bindings if any, but keeps view state and triggers rebinding data
     */
    public void invalidateData() {
        if (mAdapter != null) {
            mAdapter.invalidate();
        }
    }

    private void init() {
        setHasFixedSize(false);
        setLayoutManager(new AgendaLinearLayoutManager(getContext()));
        addItemDecoration(new DividerItemDecoration(getContext()));
        setItemAnimator(null);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return (LinearLayoutManager) getLayoutManager();
    }

    void loadMore() {
        if (mAdapter == null) {
            return;
        }
        if (getLinearLayoutManager().findFirstVisibleItemPosition() == 0) {
            // once prepended first visible position will no longer be 0
            // which will negate the guard check
            mAdapter.prepend(getContext());
        } else if (getLinearLayoutManager().findLastVisibleItemPosition()
                == mAdapter.getItemCount() - 1) {
            // once appended last visible position will no longer be last adapter position
            // which will negate the guard check
            mAdapter.append(getContext());
        }
    }

    private void notifyDateChange() {
        int position = getLinearLayoutManager().findFirstVisibleItemPosition();
        if (position < 0) {
            return;
        }
        long timeMillis = mAdapter.getAdapterItem(position).mTimeMillis;
        if (mPrevTimeMillis != timeMillis) {
            mPrevTimeMillis = timeMillis;
            // only notify listener if scroll is not triggered programmatically (i.e. no pending)
            if (mPendingScrollPosition == NO_POSITION && mListener != null) {
                mListener.onSelectedDayChange(timeMillis);
            }
        }
    }

    static class DividerItemDecoration extends ItemDecoration {
        private final Paint mPaint;
        private final int mSize;

        public DividerItemDecoration(Context context) {
            mSize = context.getResources().getDimensionPixelSize(R.dimen.divider_size);
            mPaint = new Paint();
            mPaint.setColor(ContextCompat.getColor(context, R.color.colorDivider));
            mPaint.setStrokeWidth(mSize);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            int top, left = 0, right = parent.getMeasuredWidth();
            for (int i = 0; i < parent.getChildCount(); i++) {
                top = parent.getChildAt(i).getTop() - mSize / 2;
                c.drawLine(left, top, right, top, mPaint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            if (parent.getChildAdapterPosition(view) > 0) {
                outRect.top = mSize;
            }
        }
    }

    /**
     * Light extension to {@link LinearLayoutManager} that overrides smooth scroller to
     * always snap to start
     */
    static class AgendaLinearLayoutManager extends LinearLayoutManager {

        public AgendaLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state,
                                           int position) {
            RecyclerView.SmoothScroller smoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return AgendaLinearLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        @Override
                        protected int getVerticalSnapPreference() {
                            return SNAP_TO_START; // override base class behavior
                        }
                    };
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }
}