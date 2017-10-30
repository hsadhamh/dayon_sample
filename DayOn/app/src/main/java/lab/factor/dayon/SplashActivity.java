package lab.factor.dayon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.orhanobut.logger.Logger;

import lab.factor.dayon.services.LoadDatabaseFirstTimeRequest;

public class SplashActivity extends AppCompatActivity {

    SpiceManager mTaskManager = new SpiceManager(UncachedSpiceService.class);
    CircularFillableLoaders circularFillableLoaders;


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
         circularFillableLoaders = (CircularFillableLoaders)findViewById(R.id.circularFillableLoaders);
        LoadDatabaseSession();
    }

    private void LoadDatabaseSession() {
        LoadDatabaseFirstTimeRequest request = new LoadDatabaseFirstTimeRequest(getApplicationContext());
        mTaskManager.execute(request, new DatabaseLoadRequestListener());
    }

    class DatabaseLoadRequestListener implements RequestListener<Boolean>, RequestProgressListener {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Logger.e("Database load failure with exception [%s].", spiceException.getMessage());
        }

        @Override
        public void onRequestSuccess(Boolean aBoolean) {
            if(aBoolean) {
                circularFillableLoaders.setProgress(100);
                Toast.makeText(getApplicationContext(), "DB load complete",
                        Toast.LENGTH_SHORT).show();
                Logger.d("database load successful.");
            }
            else
                Logger.d("database load failed.");
        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {
            Logger.d("Progress update: " + progress.getProgress());

        }
    }
}
