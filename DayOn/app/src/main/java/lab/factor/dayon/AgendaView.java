package lab.factor.dayon;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lab.factor.agendaview.AgendaCalendarView;
import lab.factor.agendaview.CalendarPickerController;
import lab.factor.agendaview.models.BaseCalendarEvent;
import lab.factor.agendaview.models.CalendarEvent;
import lab.factor.agendaview.models.IDayItem;

;



public class AgendaView extends AppCompatActivity implements CalendarPickerController {

    @BindView(R.id.agenda_calendar_view) AgendaCalendarView mCalendarView;
    SpiceManager mTaskManager = new SpiceManager(UncachedSpiceService.class);
    List<CalendarEvent> eventList = new ArrayList<>();

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
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

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
        LoadEvents request = new LoadEvents();
        mTaskManager.execute(request, new LoadEventListener());
    }

    static class LoadEvents extends SpiceRequest<String>{

        LoadEvents() {
            super(String.class);
        }
        @Override
        public String loadDataFromNetwork() throws Exception {
            Thread.sleep(20*1000);
            return "done";
        }
    }

    class LoadEventListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(AgendaView.this, "Failed to load.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(String str) {
            Toast.makeText(AgendaView.this, "Success load.", Toast.LENGTH_SHORT).show();
            mockList();
            initCalendar();
        }
    }

    private void mockList() {
        Calendar startTime1 = Calendar.getInstance();
        Calendar endTime1 = Calendar.getInstance();
        endTime1.add(Calendar.MONTH, 1);
        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
                ContextCompat.getColor(this, R.color.orange_dark), startTime1, endTime1, true);
        eventList.add(event1);

        Calendar startTime2 = Calendar.getInstance();
        startTime2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endTime2 = Calendar.getInstance();
        endTime2.add(Calendar.DAY_OF_YEAR, 3);
        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
                ContextCompat.getColor(this, R.color.yellow), startTime2, endTime2, true);
        eventList.add(event2);

        // Example on how to provide your own layout
        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();
        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event3 = new DrawableCalendarEvent("Visit of Harpa", "", "Dalvík",
                ContextCompat.getColor(this, R.color.blue_dark), startTime3, endTime3, false, R.drawable.agenda_day_circle);
        eventList.add(event3);
    }

}
