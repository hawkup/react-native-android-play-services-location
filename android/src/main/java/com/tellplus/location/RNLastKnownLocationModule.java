package com.tellplus.location;

import android.location.Location;
import android.support.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;


/**
 * Created by hhravn on 20/06/2017.
 */

public class RNLastKnownLocationModule extends ReactContextBaseJavaModule {
    private final LastKnownLocationProvider provider;

    public RNLastKnownLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.provider = new LastKnownLocationProvider(reactContext);
    }

    @Override
    public String getName() {
        return "RNLastKnownLocation";
    }

    @ReactMethod
    public void get(int maxAge, final Promise promise) {
        if (provider.checkPlayServices()) {
            try{
                Location location = this.provider.getLastKnownLocation(maxAge);
                WritableMap result = Arguments.createMap();

                WritableMap coords = Arguments.createMap();
                coords.putDouble("latitude", location.getLatitude());
                coords.putDouble("longitude", location.getLongitude());
                coords.putDouble("altitude", location.getAltitude());
                result.putMap("coords", coords);

                result.putDouble("fixTime", location.getTime());

                promise.resolve(result);
            } catch(Throwable e){
                promise.reject("Could not get latest location", e);
            }
        } else {
            promise.reject("PLAY_SERVICES_ERROR", new Exception("Play services not available."));
        }
    }
}
