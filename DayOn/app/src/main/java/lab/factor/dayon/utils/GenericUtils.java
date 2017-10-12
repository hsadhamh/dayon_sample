package lab.factor.dayon.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;

import lab.factor.dayon.DayOnApplication;
import lab.factor.dayon.utils.database.DaoMaster;
import lab.factor.dayon.utils.database.Events;
import lab.factor.dayon.utils.database.EventsDao;
import lab.factor.dayon.utils.json.Event;
import lab.factor.dayon.utils.json.EventList;

/**
 * Created by hassanhussain on 4/29/2017.
 */

public class GenericUtils {

    public static boolean initDatabaseV2(Context context)
    {
        //  Check if DB exists already. If not, we need to run DB initialize code.
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
                DayOnApplication.isEncrypted() ? "events-db-encrypted" : "events-db");
        Database db = DayOnApplication.isEncrypted()?
                helper.getEncryptedWritableDb(DayOnApplication.getDatabasePassword()) : helper.getWritableDb();
        DayOnApplication.setDaoSession(new DaoMaster(db).newSession());

        if(CheckIfDBNeedInitialize())
            GenericUtils.startAsyncDbSynchronization(context);
        return true;
    }

    private static boolean CheckIfDBNeedInitialize()
    {
        //  Now check for shared preference, if set, no need to run init code.
        //  Now verify if rows found in DB, if found. no need to run init code.
        //  TODO: Run init code on USER actions.
        /*
        if(!DayOnApplication.getPrefManager().getBoolean("DbInitSuccess", false))
            return true;
        */
        Logger.d("No intitialize check.");
        if((DayOnApplication.getDaoSession().getEventsDao().count() <= 0))
            return true;
        Logger.d("No intitialize false.");
        //  By default no sync required.
        return false;
    }

    private static boolean startAsyncDbSynchronization(final Context context)
    {
        try
        {
            String sJsonData = FileReader.readData(context, FileReader.FILE_DATA_ENCRYPTED);
            String sJsonDecrypted = Defense.getInstance().decryptData(sJsonData);
            EventList listEvents = (EventList) JsonSerializer.getInstance().UnserializeToObject(sJsonDecrypted, EventList.class);
            Thread.sleep(1000);
            EventsDao newEvent = DayOnApplication.getDaoSession().getEventsDao();

            for(Event event : listEvents.events){
                //  TODO: insert to table.
                Events eventInfo = new Events();
                eventInfo.setGUID(event.getEuid());
                eventInfo.setCategory(event.getCategory());
                eventInfo.setFlags(event.getFlags());

                eventInfo.setLocations(JsonSerializer.getInstance().SerializeToString(event.getLocations(), false));

                eventInfo.setName(event.getName());
                eventInfo.setProperty(JsonSerializer.getInstance().SerializeToString(event.getProperty(), false));
                eventInfo.setSub_category(event.getSub_category());
                eventInfo.setTags("");

                newEvent.insert(eventInfo);
                Logger.d("Inserted information [%s].", eventInfo.getName());
                Logger.d(eventInfo);
            }
            return true;
        }
        catch(Exception ex)
        {
            Logger.e("Failed to create the database with exception [%s].", ex.getMessage());
            return false;
        }
    }
}
