package lab.factor.dayon.headerlist;

import android.app.Application;

/**
 * Created by hassanhussain on 8/20/2017.
 */

public class CalendarApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        CommonUtils.getInstance().oneTimeInit(getApplicationContext());
    }

}
