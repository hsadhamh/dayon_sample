package io.tact.tech.dayon;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.tact.tech.almanac.AgendaAdapter;
import io.tact.tech.almanac.AgendaWidget;
import io.tact.tech.database.EventCursor;
import io.tact.tech.database.EventDatabase;
import io.tact.tech.database.EventItem;

/**
 * Created by hsadhamh on 1/11/2018.
 */

public class SimpleEventsListView extends AppCompatActivity {

    static EventDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_agenda);

        database = Room.databaseBuilder(getApplicationContext(),
                EventDatabase.class, "sample-db")
                .allowMainThreadQueries()
                .build();

        //new DatabaseAsync().execute();
        addDB();
    }

    public void addDB(){
        long startDate = 1514764800000L;
        long endDate = 1546257599000L;
        long increment = 86400000;
        int noEvents = 0;
        do {

            if(noEvents == 3){
                startDate += increment;
                noEvents = 0;
                continue;
            }

            //Let's add some dummy data to the database.
            EventItem event = new EventItem();
            event.setName("Test Event");
            event.setDateTime(startDate);

            //Now access all the methods defined in DaoAccess with sampleDatabase object
            database.daoAccess().insertOnlySingleRecord(event);

            startDate += increment;
            noEvents++;
        } while(startDate < endDate);

        //To after addition operation here.
        AgendaWidget listview = (AgendaWidget)findViewById(R.id.agenda_view);
        listview.setAdapter(new AgendaCursorAdapter(getApplicationContext()));
    }

    /*private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Void doInBackground(Void... voids) {
            long startDate = 1514764800;
            long endDate = 1546257599;
            long increment = 86400;
            int noEvents = 0;
            do {

                if(noEvents == 3){
                    startDate += increment;
                    noEvents = 0;
                    continue;
                }

                //Let's add some dummy data to the database.
                EventItem event = new EventItem();
                event.setName("Test Event");
                event.setDateTime(startDate);

                //Now access all the methods defined in DaoAccess with sampleDatabase object
                database.daoAccess().insertOnlySingleRecord(event);

                startDate += increment;
                noEvents++;
            } while(startDate < endDate);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //To after addition operation here.
            AgendaWidget listview = (AgendaWidget)findViewById(R.id.agenda_view);
            listview.setAdapter(new AgendaCursorAdapter(getApplicationContext()));
        }
    }*/

    static class GetDatabaseQuery extends AsyncTask<Void, Void, Cursor> {

        private final AgendaCursorAdapter mAgendaCursorAdapter;
        private final long mTimeMillis;

        private GetDatabaseQuery(AgendaCursorAdapter adapter, long timeMillis) {
            mAgendaCursorAdapter = adapter;
            mTimeMillis = timeMillis;
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return database.daoAccess().getCursorByDate(mTimeMillis);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mAgendaCursorAdapter.bindEvents((Long) mTimeMillis, new EventCursor(cursor));
        }
    }

    static class AgendaCursorAdapter extends AgendaAdapter {

        public AgendaCursorAdapter(Context context) {
            super(context);
        }

        @Override
        protected void loadEvents(long timeMillis) {
            //bindEvents((Long) timeMillis, new EventCursor(database.daoAccess().getCursorByDate(timeMillis)));
             new GetDatabaseQuery(this, timeMillis).execute();
        }
    }
}
