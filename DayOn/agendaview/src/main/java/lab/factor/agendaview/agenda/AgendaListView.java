package lab.factor.agendaview.agenda;

import lab.factor.agendaview.CalendarManager;
import lab.factor.agendaview.models.CalendarEvent;
import lab.factor.agendaview.utils.DateHelper;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * StickyListHeadersListView to scroll chronologically through events.
 */
public class AgendaListView extends StickyListHeadersListView {

    // region Constructors

    public AgendaListView(Context context) {
        super(context);
    }

    public AgendaListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AgendaListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // endregion

    // region Public methods

    public void scrollToCurrentDate(Calendar today) {
        List<CalendarEvent> events = CalendarManager.getInstance().getEvents();

        int toIndex = 0;
        for (int i = 0; i < events.size(); i++) {
            if (DateHelper.sameDate(today, events.get(i).getInstanceDay())) {
                toIndex = i;
                break;
            }
        }

        final int finalToIndex = toIndex;
        post(()->setSelection(finalToIndex));
    }

    // endregion
}
