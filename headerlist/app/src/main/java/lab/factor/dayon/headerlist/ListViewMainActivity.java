package lab.factor.dayon.headerlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ListViewMainActivity extends AppCompatActivity {

    EventListAdapter locAdapter;
    CalendarEventListView list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "On Create ");
        setContentView(R.layout.activity_list_view_main);
        list = (CalendarEventListView) findViewById(R.id.list_view);
        Log.d("debug", "On Create - End");
    }


}
