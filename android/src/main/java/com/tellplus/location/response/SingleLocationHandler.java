package com.tellplus.location.response;

import android.location.Location;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.tellplus.location.Messages;
import com.tellplus.location.PlayServicesLocationModule;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hhravn on 22/06/2017.
 */
public class SingleLocationHandler implements LocationHandler {
    private PlayServicesLocationModule playServicesLocationModule;
    AtomicBoolean resolved = new AtomicBoolean(false);
    private final Promise promise;

    public SingleLocationHandler(PlayServicesLocationModule playServicesLocationModule, Promise promise) {
        this.playServicesLocationModule = playServicesLocationModule;
        this.promise = promise;
    }

    @Override
    public void onSuccess(Location location) {
        if (resolved.getAndSet(true)) return;
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
        if (resolved.getAndSet(true)) return;
        promise.reject(Messages.LOCATION_SERVICES_ERROR, e);
    }
}
