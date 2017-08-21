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
            return 0;
        else if(o instanceof NoEvents)
            return 1;
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

    private void dummy(){
        int startOffset = 0;
        for(int section = 0; section < 5; section++) {
            Log.d("debug", "dummy : #day - " + "section : " + section );
            DateSection sectionItem = new DateSection();
            sectionItem.setDateString("Test");
            // add section to object list.
            getListItems().add(sectionItem);
            // modify map with offset.
            getSectionMap().put("test", startOffset++);
            // add corresponding no events item now.
            getListItems().add(new NoEvents());
            startOffset++;
        }
    }

    public class DateSection {
        String dateString;
        public String getDateString() { return dateString; }
        public void setDateString(String s){ dateString = s; }
        long timestamp;
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
            if(true)
                events.assemble(start_date, end_date);
            else
                events.dummy();
            return events;
        }
    }


}