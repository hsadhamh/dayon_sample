package lab.factor.dayon;

import android.app.Application;

import com.github.hussainderry.securepreferences.SecurePreferences;
import com.github.hussainderry.securepreferences.model.DigestType;
import com.github.hussainderry.securepreferences.model.SecurityConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import lab.factor.agendaview.models.CalendarEvent;
import lab.factor.dayon.utils.database.DaoSession;

/**
 * Created by hassanhussain on 10/8/2017.
 */

public class DayOnApplication extends Application {
    private static String msPassword = "ShLab!17KctAy";
    public static String getDatabasePassword(){ return msPassword; }

    private static String mSecureFile  = "DayOnDefense";
    public static String getFilePassword() { return mSecureFile; }

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;
    public static boolean isEncrypted(){ return ENCRYPTED; }

    private static DaoSession mDaoSession;
    public static DaoSession getDaoSession() { return mDaoSession; }
    public static void setDaoSession(DaoSession dao) { mDaoSession = dao;}

    private static SecurePreferences mPreferences;
    public static SecurePreferences getPrefManager() { return mPreferences; }

    private static List<CalendarEvent> mListEvents = new ArrayList<>();
    public static List<CalendarEvent> getEventsList() { return mListEvents; }

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        initPreferences();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initLogger(){ Logger.init("debug-lab").logLevel(LogLevel.FULL); }

    private void initPreferences(){
        // Full Configurations
        SecurityConfig fullConfig = new SecurityConfig.Builder(msPassword)
                .setAesKeySize(256)
                .setPbkdf2SaltSize(32)
                .setPbkdf2Iterations(24000)
                .setDigestType(DigestType.SHA256)
                .build();

        mPreferences = SecurePreferences.getInstance(DayOnApplication.this, getFilePassword(), fullConfig);
    }


}
