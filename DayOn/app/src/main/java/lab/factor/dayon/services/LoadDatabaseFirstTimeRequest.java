package lab.factor.dayon.services;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import lab.factor.dayon.utils.GenericUtils;

/**
 * Created by hassanhussain on 10/8/2017.
 */

public class LoadDatabaseFirstTimeRequest extends SpiceRequest<Boolean> {

    private Context mApplicationContext;
    private Context getApplicationContext() { return mApplicationContext; }

    public LoadDatabaseFirstTimeRequest(Context context) {
        super(Boolean.class);
        mApplicationContext = context;
    }

    @Override
    public Boolean loadDataFromNetwork() throws Exception {
        return GenericUtils.initDatabaseV2(getApplicationContext());
    }
}
