package lab.factor.dayon.services;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.octo.android.robospice.request.SpiceRequest;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lab.factor.agendaview.models.BaseCalendarEvent;
import lab.factor.dayon.DayOnApplication;
import lab.factor.dayon.R;
import lab.factor.dayon.utils.JsonSerializer;
import lab.factor.dayon.utils.database.Events;
import lab.factor.dayon.utils.database.EventsDao;
import lab.factor.dayon.utils.json.EventCategory.Category;
import lab.factor.dayon.utils.json.EventProperty;
import lab.factor.dayon.utils.json.Location;

/**
 * Created by hassanhussain on 10/10/2017.
 */

public class LoadEventsRequest extends SpiceRequest<Boolean> {
    long mlStartTime = 0;
    long mlEndTime = 0;
    Context mContext = null;

    public LoadEventsRequest(Context context, long lStartTime, long lEndTime) {
        super(Boolean.class);
        mlStartTime = lStartTime;
        mlEndTime = lEndTime;
        mContext = context;
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {

        DayOnApplication.getEventsList().clear();

        QueryBuilder<Events> qb = DayOnApplication.getDaoSession().getEventsDao().queryBuilder();
        qb.where(EventsDao.Properties.Start_date.ge(mlStartTime), EventsDao.Properties.End_date.le(mlEndTime));

        List<Events> listEvents = qb.list();

        for (Events ev : listEvents) {

            Calendar startDate = Calendar.getInstance();
            startDate.setTimeInMillis(ev.getStart_date()*1000);

            Calendar endDate = Calendar.getInstance();
            endDate.setTimeInMillis(ev.getEnd_date()*1000);

            String sName = ev.getName();
            String sEventProperty = ev.getProperty();
            String sLocation = "India";
            boolean bAllDay = (ev.getCategory() & Category.ALL_DAY.getValue()) == Category.ALL_DAY.getValue();

            EventProperty oProperty = (EventProperty) JsonSerializer.getInstance().UnserializeToObject(sEventProperty, EventProperty.class);
            String sDesc = oProperty.getDescription();

            List<Location> listEventLocations = new ArrayList<>();
            Object listLocations = JsonSerializer.getInstance().ConvertToObjectList(ev.getLocations(), Location.class);
            if (listLocations != null) {
                listEventLocations = (List<Location>)listLocations;
            }

            int nColor = ContextCompat.getColor(mContext, R.color.orange_dark);

            for(Location location : listEventLocations)
            {
                BaseCalendarEvent baseEvent = new BaseCalendarEvent(sName, sDesc,
                        location.getState() + ", " + location.getCountry(), nColor, startDate, endDate, bAllDay);
                DayOnApplication.getEventsList().add(baseEvent);
            }
        }

        return true;
    }

}
