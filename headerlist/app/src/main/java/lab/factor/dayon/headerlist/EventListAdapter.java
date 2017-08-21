package lab.factor.dayon.headerlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by hassanhussain on 8/20/2017.
 */

public class EventListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    EventsArrayList eventsList = null;
    private EventsArrayList getEventsList() { return eventsList; }

    LayoutInflater mInflater = null;
    private LayoutInflater getInflater() {
        if(mInflater == null)
            mInflater = LayoutInflater.from(getContext());
        return mInflater;
    }

    Context mContext;
    public Context getContext() { return mContext; }

    public EventListAdapter(Context c, EventsArrayList events){
        mContext = c;
        eventsList = events;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        Log.d("debug", "isItemViewTypePinned : " + viewType);
        return viewType == EventsArrayList.DATE_SECTION;
    }

    @Override
    public boolean areAllItemsEnabled() {
        Log.d("debug", "areAllItemsEnabled : ");
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        Log.d("debug", "isEnabled : " + i);
        // Only events item are clickable or selectable.
        return getItemViewType(i) == EventsArrayList.EVENT_ITEM;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        Log.d("debug", "registerDataSetObserver : ");
        // TODO : implement to make view as observer
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        Log.d("debug", "unregisterDataSetObserver : ");
        // TODO : implement to make view as observer
    }

    @Override
    public int getCount() {
        Log.d("debug", "getCount : ");
        if(getEventsList() == null)
            return 0;
        Log.d("debug", "getCount : " + getEventsList().getItemsCount());
        return getEventsList().getItemsCount();
    }

    @Override
    public Object getItem(int i) {
        if(getEventsList() == null)
            return null;
        Log.d("debug", "getItem : " + i);
        return getEventsList().getItem(i);
    }

    @Override
    public long getItemId(int i) {
        Log.d("debug", "getItemId : " + i);
        // for now, there is no unique ID.
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        Log.d("debug", "hasStableIds : " );
        /*
         No stable IDs as there is no unqiue IDs for each item.
         https://stackoverflow.com/questions/18217416/android-what-is-the-meaning-of-stableids
         This will lead to recreation of complete list on data set change.
        */
        return false;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup parent) {
        Log.d("debug", "getView : " + i );
        if(getItemViewType(i) == EventsArrayList.DATE_SECTION)
            return getHeaderView(getItem(i), convertview, parent);
        else if(getItemViewType(i) == EventsArrayList.NO_EVENT_ITEM)
            return getNoEventsView(convertview, parent);
        else
            return null;
    }

    private View getHeaderView(Object data, View convertView, ViewGroup parent){
        Log.d("debug", "Adapter - getHeaderView");
        DateHeaderViewHolder holder;

        if (convertView == null) {
            Log.d("debug", "Adapter - getHeaderView - new videw");
            holder = new DateHeaderViewHolder();
            convertView = getInflater().inflate(R.layout.header, parent, false);
            holder.dateHeader = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            Log.d("debug", "Adapter - getHeaderView - reuse view");
            holder = (DateHeaderViewHolder) convertView.getTag();
        }
        EventsArrayList.DateSection eventDate = (EventsArrayList.DateSection) data;
        Log.d("debug", "Adapter - getHeaderView - date : " + eventDate.getDateString());
        holder.dateHeader.setText(eventDate.getDateString());
        return convertView;
    }

    private View getNoEventsView(View convertView, ViewGroup parent){
        Log.d("debug", "Adapter - getNoEventsView");
        EventViewHolder holder;

        if (convertView == null) {
            Log.d("debug", "Adapter - getView + nnew view ");
            holder = new EventViewHolder();
            convertView = getInflater().inflate(R.layout.event, parent, false);
            holder.eventText = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            Log.d("debug", "Adapter - getView + reuse view ");
            holder = (EventViewHolder) convertView.getTag();
        }
        holder.eventText.setText("NO NO NO NO");
        Log.d("debug", "Adapter - getView + done ");
        return convertView;
    }

    @Override
    public int getItemViewType(int i) {
        Log.d("debug", "Adapter - getItemViewType");
        if(getEventsList() == null)
            return -1;
        Log.d("debug", "Adapter - getItemViewType : " + getEventsList().getItemType(getItem(i)));
        return getEventsList().getItemType(getItem(i));
    }

    @Override
    public int getViewTypeCount() {
        // only 2 view types as of now.
        // 0 : SECTION DATE
        // 1 : ITEM Events - No Events
        Log.d("debug", "Adapter - getViewTypeCount : " + 2);
        return 2;
    }

    @Override
    public boolean isEmpty() {
        Log.d("debug", "Adapter - isEmpty : " + (getEventsList() == null));
        return (getEventsList() == null);
    }

    class DateHeaderViewHolder {
        TextView dateHeader;
    }

    class EventViewHolder {
        TextView eventText;
    }
}
