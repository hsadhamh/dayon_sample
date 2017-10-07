package lab.factor.agendaview;

import lab.factor.agendaview.models.CalendarEvent;
import lab.factor.agendaview.models.IDayItem;

import java.util.Calendar;

public interface CalendarPickerController {
    void onDaySelected(IDayItem dayItem);

    void onEventSelected(CalendarEvent event);

    void onScrollToDate(Calendar calendar);
}
