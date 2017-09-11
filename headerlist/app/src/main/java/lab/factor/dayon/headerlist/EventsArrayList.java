package lab.factor.dayon.headerlist;

import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hassanhussain on 8/19/2017.
 */

public class EventsArrayList {
    public static int DATE_SECTION = 0;
    public static int NO_EVENT_ITEM = 1;
    public static int EVENT_ITEM = 2;

    private List<Object> listItems = new ArrayList<>();
    private HashMap<String, Integer> sectionMap = new HashMap<>();

    public List<Object> getListItems() { return listItems; }
    public HashMap<String, Integer> getSectionMap() { return sectionMap; }

    public int getItemsCount(){ return getListItems().size(); }
    public Object getItem(int position) {
        if(position < 0 && listItems.size() > 0)
            return listItems.get(0);

        if(position >= listItems.size())
            return listItems.get(listItems.size() - 1);

        return listItems.get(position);
    }
    public int getItemType(Object o){
        if(o == null)
            return -1;
        else  if(o instanceof DateSection)
            return DATE_SECTION;
        else if(o instanceof NoEvents)
            return NO_EVENT_ITEM;
        else if (o instanceof EventData)
            return EVENT_ITEM;
        else
            throw new UnknownError("Invalid object found.");
    }

    private void assemble(LocalDate sd, LocalDate ed){
        int countDays = Days.daysBetween(sd, ed).getDays();
        Log.d("debug", "assemble : #days - " + countDays );
        int startOffset = 0;
        LocalDate locDate = sd;

        for(int section = 0; section < countDays; section++) {
            Log.d("debug", "assemble : #day - " + locDate.toString() + " section : " + section );
            DateSection sectionItem = new DateSection();
            String sDate = locDate.toString(CommonUtils.getInstance().getDateFormatterV2());
            sectionItem.setDateString(sDate);
            // add section to object list.
            getListItems().add(sectionItem);
            // modify map with offset.
            getSectionMap().put(sDate, startOffset++);
            // add corresponding no events item now.
            getListItems().add(new NoEvents());
            startOffset++;
            // add one day
            locDate = locDate.plusDays(1);
        }
    }

    public void addEventForTheDay(EventData ed){
        // find offset.
        Integer iOffset = getSectionMap().get(ed.getDate());
        int offset = 0;
        if(iOffset != null)
            offset += iOffset;

        // check if it is date section
        Object dateSection = getListItems().get(offset);
        if(dateSection instanceof DateSection)
        {
            int eventsCount  = ((DateSection) dateSection).countEvents;
            int offsetToInsert;
            if(eventsCount > 0)
                offsetToInsert = offset + eventsCount;
            else {
                // 1) remove the no events object
                // 2) then add the first object.
                offsetToInsert = offset + 1;
                getListItems().remove(offsetToInsert);
            }

            if(offsetToInsert > 0) {
                getListItems().add(offsetToInsert, ed);
                ((DateSection) dateSection).countEvents += 1;
            }
        }
        else
            Log.d("debug", "addEventToDay - wrong instance offset");
    }

    public void removeEventsForTheDay(EventData ed){
        // find offset.
        int offset = getSectionMap().get(ed.getDate());
        // check if it is date section
        Object dateSection = getListItems().get(offset);
        if(dateSection instanceof DateSection) {
        }
        else{
        }
    }

    public class DateSection {
        String dateString;
        public String getDateString() { return dateString; }
        public void setDateString(String s){ dateString = s; }
        long timestamp;
        int countEvents;
    }

    public class EventItem {
        String dateString;
        long TimeStamp;
        String eventName;
    }

    public class NoEvents{}

    public static class Builder {
        EventsArrayList events;

        private LocalDate start_date;
        private String sStartDate;
        private void setStartDate(String date) throws ParseException {
            sStartDate=date;
            start_date = CommonUtils.getInstance().getDateFormatterV2().parseLocalDate(sStartDate);
        }

        private LocalDate end_date;
        private String sEndDate;
        private void setEndDate(String date) throws ParseException {
            sEndDate=date;
            end_date = CommonUtils.getInstance().getDateFormatterV2().parseLocalDate(sEndDate);
        }

        Builder(){ events = new EventsArrayList(); }

        static EventsArrayList.Builder getInstance(){ return new EventsArrayList.Builder(); }
        EventsArrayList.Builder setStart(String s) throws ParseException {
            Log.d("debug", "set start: " + s);
            setStartDate(s);
            return this;
        }
        EventsArrayList.Builder setEnd(String s) throws ParseException{
            Log.d("debug", "set end: " + s);
            setEndDate(s);
            return this;
        }
        EventsArrayList build(){
            Log.d("debug", "build : " + start_date.toString() + " end: " + end_date.toString());
            events.assemble(start_date, end_date);
            return events;
        }
    }


}