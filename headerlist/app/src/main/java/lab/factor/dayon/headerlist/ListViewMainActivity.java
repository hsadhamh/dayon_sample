package lab.factor.dayon.headerlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class ListViewMainActivity extends AppCompatActivity {

    CalendarEventListView list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "On Create ");
        setContentView(R.layout.activity_list_view_main);
        list = (CalendarEventListView) findViewById(R.id.list_view);
        Log.d("debug", "On Create - End");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
