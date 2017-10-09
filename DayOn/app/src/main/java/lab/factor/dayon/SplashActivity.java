package lab.factor.dayon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.orhanobut.logger.Logger;

import lab.factor.dayon.services.LoadDatabaseFirstTimeRequest;

public class SplashActivity extends AppCompatActivity {

    SpiceManager mTaskManager = new SpiceManager(UncachedSpiceService.class);

    @Override
    protected void onStart() {
        mTaskManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mTaskManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LoadDatabaseSession();
    }

    private void LoadDatabaseSession() {
        LoadDatabaseFirstTimeRequest request = new LoadDatabaseFirstTimeRequest(getApplicationContext());
        mTaskManager.execute(request, new DatabaseLoadRequestListener());
    }

    class DatabaseLoadRequestListener implements RequestListener<Boolean>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Logger.e("Database load failure with exception [%s].", spiceException.getMessage());
        }

        @Override
        public void onRequestSuccess(Boolean aBoolean) {
            if(aBoolean)
                Logger.d("database load successful.");
            else
                Logger.d("database load failed.");
        }
    }
}
