package lab.factor.dayon.headerlist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.text.ParseException;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by hassanhussain on 8/19/2017.
 */
public class CalendarEventListView extends LinearLayout {
    private PinnedSectionListView mListHeaderView = null;
    public void setListView(PinnedSectionListView list){ mListHeaderView = list; }
    public PinnedSectionListView getListView(){ return mListHeaderView; }

    public CalendarEventListView(Context context) {
        super(context);
    }

    public CalendarEventListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("debug", "On Constructor 1");
        initControl(context,attrs);
    }

    public CalendarEventListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("debug", "On Constructor 2");
        initControl(context,attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        Log.d("debug", "Init Control");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_events_list, this);
        Log.d("debug", "Bind Views");
        bindViews();
    }

    private void bindViews(){
        Log.d("debug", "Init Views");
        setListView((PinnedSectionListView) findViewById(R.id.id_event_list_headers));
        Log.d("debug", "Update List");
        updateViews();
    }

    public void setAdapter(EventListAdapter locAdapter){
        getListView().setAdapter(locAdapter);
    }

    public void updateViews(){
        try {
            Log.d("debug", "Get Events List");
            EventsArrayList events = EventsArrayList.Builder.getInstance()
                    .setStart("01-01-2016")
                    .setEnd("01-01-2017")
                    .build();
            EventListAdapter locAdapter = new EventListAdapter(getContext(), events);
            Log.d("debug", "Set Adpater");
            setAdapter(locAdapter);
        }
        catch(ParseException ex){
            // log.
            Log.d("debug", "Exception : Update - " + ex.getMessage());
        }
    }

    public void addEvents(EventData ed){
        if(getListView().getAdapter() == null)
            return;

        EventListAdapter adapter = (EventListAdapter) getListView().getAdapter();
        adapter.getEventsList().addEventForTheDay(ed);
    }

    public void notifyDataSetChanged(){
        if(getListView().getAdapter() == null)
            return;

        //getListView().getAdapter().notify();
    }
}
