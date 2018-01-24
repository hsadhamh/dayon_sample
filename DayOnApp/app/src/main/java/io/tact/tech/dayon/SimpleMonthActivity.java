package io.tact.tech.dayon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.tact.tech.almanac.AlmanacMonthWidget;
import io.tact.tech.utilities.CalendarUtils;

/**
 * Created by hsadhamh on 1/9/2018.
 */

public class SimpleMonthActivity extends AppCompatActivity {

    private final Coordinator mCoordinator = new Coordinator();
    private AlmanacMonthWidget mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_month_view);

        mCalendarView = (AlmanacMonthWidget) findViewById(R.id.calendar_view);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCoordinator.restoreState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mCoordinator.coordinate(null, mCalendarView, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCoordinator.saveState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Coordinator utility that synchronizes widgets as selected date changes
     */
    static class Coordinator {
        private static final String STATE_SELECTED_DATE = "state:selectedDate";

        private final AlmanacMonthWidget.OnChangeListener mCalendarListener
                = new AlmanacMonthWidget.OnChangeListener() {
            @Override
            public void onSelectedDayChange(long calendarDate) {
                sync(calendarDate, mCalendarView);
            }
        };

        private TextView mTextView;
        private AlmanacMonthWidget mCalendarView;
        private long mSelectedDayMillis = CalendarUtils.NO_TIME_MILLIS;

        /**
         * Set up widgets to be synchronized
         * @param textView      title
         * @param calendarView  calendar view
         * @param agendaView    agenda view
         */
        public void coordinate(@NonNull TextView textView,
                               @NonNull AlmanacMonthWidget calendarView,
                               @NonNull Object agendaView) {
            if (mCalendarView != null) {
                mCalendarView.setOnChangeListener(null);
            }

            mTextView = textView;
            mCalendarView = calendarView;
            if (mSelectedDayMillis < 0) {
                mSelectedDayMillis = CalendarUtils.today();
            }
            mCalendarView.setSelectedDay(mSelectedDayMillis);
            updateTitle(mSelectedDayMillis);
            calendarView.setOnChangeListener(mCalendarListener);
        }

        void saveState(Bundle outState) {
            outState.putLong(STATE_SELECTED_DATE, mSelectedDayMillis);
        }

        void restoreState(Bundle savedState) {
            mSelectedDayMillis = savedState.getLong(STATE_SELECTED_DATE,
                    CalendarUtils.NO_TIME_MILLIS);
        }

        void reset() {
            mSelectedDayMillis = CalendarUtils.today();
            if (mCalendarView != null) {
                mCalendarView.reset();
            }
            updateTitle(mSelectedDayMillis);
        }

        private void sync(long dayMillis, View originator) {
            mSelectedDayMillis = dayMillis;
            if (originator != mCalendarView) {
                mCalendarView.setSelectedDay(dayMillis);
            }
            updateTitle(dayMillis);
        }

        private void updateTitle(long dayMillis) {
            Toast.makeText(mCalendarView.getContext(), CalendarUtils.toMonthString(mCalendarView.getContext(), dayMillis),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
