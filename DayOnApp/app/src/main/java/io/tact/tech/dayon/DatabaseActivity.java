package io.tact.tech.dayon;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import io.tact.tech.database.EventDatabase;
import io.tact.tech.database.EventItem;

/**
 * Created by hsadhamh on 1/24/2018.
 */

public class DatabaseActivity extends AppCompatActivity {
    static TextView mTxt = null;
    static EventDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_db);

        mTxt = (TextView) findViewById(R.id.txt_show);

        database = Room.databaseBuilder(getApplicationContext(),
                EventDatabase.class, "sample-db1")
                .allowMainThreadQueries()
                .build();

        //new DatabaseAsync().execute();
        addDB();

        new GetDatabaseQuery().execute();
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
    }

    static class GetDatabaseQuery extends AsyncTask<Void, Void, List<EventItem>> {

        private GetDatabaseQuery() {
        }

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected List<EventItem> doInBackground(Void... voids) {
            return database.daoAccess().fetchAllData();
        }

        @Override
        protected void onPostExecute(List<EventItem> cursor) {
            super.onPostExecute(cursor);
            String str = "";
            for (EventItem ev :
                    cursor) {
                str += "\n" + ev.getName() + " - " + ev.getDateTime();
            }
            mTxt.setText(str);
        }
    }
}
