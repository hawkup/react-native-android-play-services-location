package com.tellplus.location;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.tellplus.location.request.LocationRequestArgs;
import com.tellplus.location.response.SingleLocationHandler;


/**
 * Created by hhravn on 20/06/2017.
 */

public class PlayServicesLocationModule extends ReactContextBaseJavaModule {
    private final LocationProvider locationProvider;

    public PlayServicesLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.locationProvider = new LocationProvider(reactContext);
    }

    @Override
    public String getName() {
        return "PlayServicesLocation";
    }

    @ReactMethod
    public void getLastKnownLocation(final Promise promise) {
        if (!locationProvider.checkPlayServices()) {
            promise.reject("PLAY_SERVICES_ERROR", new Exception(Messages.PLAY_SERVICES_ERROR));
        } else {
            SingleLocationHandler observer = new SingleLocationHandler(this, promise);
            this.locationProvider
                    .getLastKnownLocation(observer);
        }
    }

    @ReactMethod
    public void getCurrentLocation(int timeout, int maxAge, final Promise promise) {
        if (!locationProvider.checkPlayServices()) {
            promise.reject("PLAY_SERVICES_ERROR", new Exception(Messages.PLAY_SERVICES_ERROR));
        } else {
            SingleLocationHandler observer = new SingleLocationHandler(this, promise);
            this.locationProvider
                    .getCurrentLocation(observer, new LocationRequestArgs(timeout, maxAge));
        }
    }

}
