package com.tellplus.location;

import android.location.Location;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by hhravn on 20/06/2017.
 */

public class PlayServicesLocationModule extends ReactContextBaseJavaModule {
    private final LocationProvider locationProvider;
    private final String PLAY_SERVICES_ERROR = "Play services not up to date.";
    private final String LOCATION_SERVICES_ERROR = "Location could not be retrieved.";

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
            promise.reject("PLAY_SERVICES_ERROR", new Exception(PLAY_SERVICES_ERROR));
        } else {
            SingleLocationHandler observer = new SingleLocationHandler(promise);
            this.locationProvider
                    .getLastKnownLocation(observer);
        }
    }

    @ReactMethod
    public void getCurrentLocation(final Promise promise) {
        if (!locationProvider.checkPlayServices()) {
            promise.reject("PLAY_SERVICES_ERROR", new Exception(PLAY_SERVICES_ERROR));
        } else {
            SingleLocationHandler observer = new SingleLocationHandler(promise);
            this.locationProvider
                    .getCurrentLocation(observer);
        }
    }

    class SingleLocationHandler implements LocationProvider.LocationHandler {
        AtomicBoolean resolved = new AtomicBoolean(false);
        private final Promise promise;

        public SingleLocationHandler(Promise promise) {
            this.promise = promise;
        }

        @Override
        public void onSuccess(Location location) {
            if(resolved.getAndSet(true)) return;
            WritableMap result = Arguments.createMap();

            WritableMap coords = Arguments.createMap();
            coords.putDouble("latitude", location.getLatitude());
            coords.putDouble("longitude", location.getLongitude());
            coords.putDouble("altitude", location.getAltitude());
            result.putMap("coords", coords);

            result.putDouble("fixTime", location.getTime());

            promise.resolve(result);
        }

        @Override
        public void onError(Throwable e) {
            if(resolved.getAndSet(true)) return;
            promise.reject(LOCATION_SERVICES_ERROR, e);
        }
    }
}
