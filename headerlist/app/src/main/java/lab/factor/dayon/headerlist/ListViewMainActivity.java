package lab.factor.dayon.headerlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ListViewMainActivity extends AppCompatActivity {

    CalendarEventListView list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "On Create ");
        setContentView(R.layout.activity_list_view_main);
        list = (CalendarEventListView) findViewById(R.id.list_view);
        Log.d("debug", "On Create - End");
        addEvents();
        Log.d("debug", "onCreate - added events");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void addEvents(){

        EventData data1 = new EventData();
        data1.setName("Test 1");
        data1.setName("01-01-2016");

        list.addEvents(data1);

        EventData data2 = new EventData();
        data2.setName("Test 2");
        data2.setName("01-01-2016");
        list.addEvents(data2);

        EventData data3 = new EventData();
        data3.setName("Test 3");
        data3.setName("01-01-2016");
        list.addEvents(data3);

        EventData data4 = new EventData();
        data4.setName("Test 1");
        data4.setName("01-05-2016");
        list.addEvents(data4);

        EventData data5 = new EventData();
        data5.setName("Test 1");
        data5.setName("01-08-2016");
        list.addEvents(data5);

        EventData data6 = new EventData();
        data6.setName("Test 1");
        data6.setName("08-12-2016");
        list.addEvents(data6);
    }
}
