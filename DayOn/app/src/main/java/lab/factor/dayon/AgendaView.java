package lab.factor.dayon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.factor.agendaview.AgendaCalendarView;
import lab.factor.agendaview.CalendarPickerController;
import lab.factor.agendaview.models.CalendarEvent;
import lab.factor.agendaview.models.IDayItem;
import lab.factor.dayon.services.LoadEventsRequest;

;



public class AgendaView extends AppCompatActivity implements CalendarPickerController {

    @BindView(R.id.agenda_calendar_view) AgendaCalendarView mCalendarView;
    SpiceManager mTaskManager = new SpiceManager(UncachedSpiceService.class);
    List<CalendarEvent> eventList = new ArrayList<>();

    Calendar minDate = null;
    Calendar maxDate = null;

    LocalDate mToday = null;
    LocalDate mMinDate = null;
    LocalDate mMaxDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_view);

        ButterKnife.bind(this);

        initCalendar();

        performRequest();

    }

    public void initCalendar()
    {

        mToday = LocalDate.now();

        LocalDate tempDate = mToday.minusMonths(2);
        mMinDate = new LocalDate(tempDate.getYear(), tempDate.getMonthOfYear()-2, 1);

        tempDate = mToday.plusMonths(12);
        mMaxDate = new LocalDate(tempDate.getYear(), tempDate.getMonthOfYear(), 1);

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        minDate = mMinDate.toDateTimeAtCurrentTime().toCalendar(Locale.getDefault());
        maxDate = mMaxDate.toDateTimeAtCurrentTime().toCalendar(Locale.getDefault());

        mCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);

        mCalendarView.enableCalenderView(true);
    }


    @Override
    public void onDaySelected(IDayItem dayItem) {
        
    }

    @Override
    public void onEventSelected(CalendarEvent event) {

    }

    @Override
    public void onScrollToDate(Calendar calendar) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStart() {
        mTaskManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mTaskManager.shouldStop();
        super.onStop();
    }

    private void performRequest() {

        DateTime minDateCon = new DateTime(mMinDate.getYear(), mMinDate.getMonthOfYear(), mMinDate.getDayOfMonth(), 0, 0,0, DateTimeZone.UTC);
        DateTime maxDateCon = new DateTime(mMaxDate.getYear(), mMaxDate.getMonthOfYear(), mMaxDate.getDayOfMonth(), 23, 59,59, DateTimeZone.UTC);

        LoadEventsRequest request = new LoadEventsRequest(getApplicationContext(),
                minDateCon.getMillis()/1000, maxDateCon.getMillis()/1000);
        mTaskManager.execute(request, new LoadEventListener());
    }

    private class LoadEventListener implements RequestListener<Boolean> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(AgendaView.this, "Failed to load.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(Boolean aBoolean) {
            Toast.makeText(AgendaView.this, "Success load.", Toast.LENGTH_SHORT).show();
            mockList();
            initCalendar();
        }
    }

    private void mockList() {
        eventList.clear();
        eventList.addAll(DayOnApplication.getEventsList());
    }
}
