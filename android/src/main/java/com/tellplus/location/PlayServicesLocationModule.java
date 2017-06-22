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

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;


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
            this.locationProvider
                    .getLastKnownLocation()
                    .subscribe(new LocationObserver(promise));
        }
    }

    @ReactMethod
    public void getCurrentLocation(final Promise promise) {
        if (!locationProvider.checkPlayServices()) {
            promise.reject("PLAY_SERVICES_ERROR", new Exception(PLAY_SERVICES_ERROR));
        } else {
            this.locationProvider
                    .getCurrentLocation()
                    .subscribe(new LocationObserver(promise));
        }
    }

    class LocationObserver extends DisposableSingleObserver<Location>{

        private final Promise promise;

        public LocationObserver(Promise promise) {
            this.promise = promise;
        }

        @Override
        public void onSuccess(@io.reactivex.annotations.NonNull Location location) {
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
        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
            promise.reject(LOCATION_SERVICES_ERROR, e);
        }
    }
}
