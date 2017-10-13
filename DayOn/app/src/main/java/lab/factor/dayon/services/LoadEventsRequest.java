package lab.factor.dayon.services;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.octo.android.robospice.request.SpiceRequest;

import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lab.factor.agendaview.models.BaseCalendarEvent;
import lab.factor.dayon.DayOnApplication;
import lab.factor.dayon.R;
import lab.factor.dayon.utils.JsonSerializer;
import lab.factor.dayon.utils.database.Events;
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

        QueryBuilder qb = DayOnApplication.getDaoSession().getEventsDao().queryBuilder();
        //qb.whereOr(EventsDao.Properties.Start_date.ge(mlStartTime), EventsDao.Properties.End_date.le(mlEndTime));

        List<Events> listEvents = qb.list();

        for (Events ev : listEvents){

            DateTime startEventTime = new DateTime(ev.getStart_date(), DateTimeZone.UTC);
            Calendar startTime1 = startEventTime.toCalendar(Locale.getDefault());

            DateTime endEventTime = new DateTime(ev.getEnd_date(), DateTimeZone.UTC);
            Calendar endTime1 = startEventTime.toCalendar(Locale.getDefault());

            String sName = ev.getName();
            String sEventProperty = ev.getProperty();
            String sLocation = "India";
            boolean bAllDay = (ev.getCategory() & Category.ALL_DAY.getValue()) == Category.ALL_DAY.getValue();

            EventProperty oProperty = (EventProperty) JsonSerializer.getInstance().UnserializeToObject(sEventProperty, EventProperty.class);
            String sDesc = oProperty.getDescription();

            CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, Location.class);
            List<Location> listLocations = (List<Location>) JsonSerializer.getInstance().UnserializeToObject(sLocation, typeReference.getRawClass());

            int nColor = ContextCompat.getColor(mContext, R.color.orange_dark);

            for(Location location : listLocations)
            {
                BaseCalendarEvent baseEvent = new BaseCalendarEvent(sName, sDesc,
                        location.getState() + ", " + location.getCountry(), nColor, startTime1, endTime1, bAllDay);
                DayOnApplication.getEventsList().add(baseEvent);
            }
        }

        return true;
    }

}
