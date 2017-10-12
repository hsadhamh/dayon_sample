package lab.factor.dayon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

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
// minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        minDate = Calendar.getInstance();
        maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

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
        LoadEventsRequest request = new LoadEventsRequest(getApplicationContext(), minDate.getTimeInMillis(), maxDate.getTimeInMillis());
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
