package lab.factor.dayon.headerlist;


import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * Created by hassanhussain on 8/19/2017.
 */

public class CommonUtils {
    private  String DATE_FORMAT = "dd-MM-yyyy";
    public String getDateFormat(){ return DATE_FORMAT; }

    private SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    public SimpleDateFormat getDateFormatter() { return DATE_FORMATTER; }

    DateTimeFormatter joda_formatter = DateTimeFormat.forPattern(DATE_FORMAT);
    public DateTimeFormatter getDateFormatterV2() { return joda_formatter; }

    public void oneTimeInit(Context context){
        JodaTimeAndroid.init(context);
    }

    private static CommonUtils singleton = null;
    public static CommonUtils getInstance(){
        if(singleton == null)
            singleton = new CommonUtils();
        return singleton;
    }
}
