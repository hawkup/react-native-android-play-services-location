package com.tellplus.location;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hhravn on 23/06/2017.
 */

public class PlayServicesUtil {
    public static final Map<Integer, String> status = new HashMap<>();
    static {
        status.put(ConnectionResult.SUCCESS, "OK");
        status.put(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, "Google Play Services needs to be updated.");
        status.put(ConnectionResult.API_UNAVAILABLE, "Google Play Services is required, but was not found.");
        status.put(ConnectionResult.SERVICE_MISSING, "Google Play Services is required, but was not found.");
        status.put(ConnectionResult.RESTRICTED_PROFILE, "Google Play Services cannot be accessed using this user profile.");
    }

    static class Result{
        final int code;
        final String message;

        Result(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    static final Result checkPlayServices(Context context) {
        int result = GoogleApiAvailability
                .getInstance()
                .isGooglePlayServicesAvailable(context);
        return new Result(result, status.get(result));

    }

}
