package lab.factor.combimonthview;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static lab.factor.combimonthview.Constants.SPANS_COUNT;

/**
 * Created by hassanhussain on 11/7/2017.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.CellViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CONTENT = 1;
    private final String[] mWeekdays;
    private final int mStartOffset;
    private final int mDays;
    private final long mBaseTimeMillis;
    @VisibleForTesting
    final Set<Integer> mEvents = new HashSet<>();
    private int mSelectedPosition = -1;

    public GridAdapter(long monthMillis) {
        mWeekdays = DateFormatSymbols.getInstance().getShortWeekdays();
        mBaseTimeMillis = CalendarUtils.monthFirstDay(monthMillis);
        mStartOffset = CalendarUtils.monthFirstDayOffset(mBaseTimeMillis) + SPANS_COUNT;
        mDays = mStartOffset + CalendarUtils.monthSize(monthMillis);
    }

    @Override
    public CellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.grid_item_header, parent, false));
            case VIEW_TYPE_CONTENT:
            default:
                return new ContentViewHolder(inflater.inflate(R.layout.grid_item_content, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CellViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            int index;
            switch (CalendarUtils.sWeekStart) {
                case Calendar.SATURDAY:
                    index = position == 0 ? Calendar.SATURDAY : position;
                    break;
                case Calendar.SUNDAY:
                default:
                    index = position + Calendar.SUNDAY;
                    break;
                case Calendar.MONDAY:
                    index = position + Calendar.MONDAY == mWeekdays.length ?
                            Calendar.SUNDAY : position + Calendar.MONDAY;
                    break;
            }
            ((HeaderViewHolder) holder).textView.setText(mWeekdays[index]);
        } else { // holder instanceof ContentViewHolder
            if (position < mStartOffset) {
                ((ContentViewHolder) holder).textView.setText(null);
            } else {
                final int adapterPosition = holder.getAdapterPosition();
                TextView textView = ((ContentViewHolder) holder).textView;
                int dayIndex = adapterPosition - mStartOffset;
                String dayString = String.valueOf(dayIndex + 1);
                SpannableString spannable = new SpannableString(dayString);
                if (mSelectedPosition == adapterPosition) {
                    spannable.setSpan(new CircleSpan(textView.getContext()), 0,
                            dayString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (mEvents.contains(dayIndex)) {
                    spannable.setSpan(new UnderDotSpan(textView.getContext()),
                            0, dayString.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                textView.setText(spannable, TextView.BufferType.SPANNABLE);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectedPosition(adapterPosition, true);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDays;
    }

    void setSelectedDay(long dayMillis) {
        setSelectedPosition(CalendarUtils.isNotTime(dayMillis) ? -1 :
                mStartOffset + CalendarUtils.dayOfMonth(dayMillis) - 1, false);
    }

    void swapCursor(@NonNull Object cursor) {}

    private void setSelectedPosition(int position, boolean notifyObservers) {
        int last = mSelectedPosition;
        if (position == last) {
            return;
        }
        mSelectedPosition = position;
        if (last >= 0) {
            notifyItemChanged(last);
        }
        if (position >= 0) {
            long timeMillis = mBaseTimeMillis + (mSelectedPosition - mStartOffset) * DateUtils.DAY_IN_MILLIS;
            notifyItemChanged(position, notifyObservers ? new SelectionPayload(timeMillis) : null);
        }
    }

    static abstract class CellViewHolder extends RecyclerView.ViewHolder {
        CellViewHolder(View itemView) { super(itemView); }
    }

    static class HeaderViewHolder extends CellViewHolder {
        final TextView textView;
        HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
    static class ContentViewHolder extends CellViewHolder {
        final TextView textView;
        ContentViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    static class SelectionPayload {
        final long timeMillis;
        public SelectionPayload(long timeMillis) {
            this.timeMillis = timeMillis;
        }
    }
}
