package com.tellplus.location.request;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.tellplus.location.response.LocationHandler;
import com.tellplus.location.Messages;

/**
 * When isLocationAvailable() returns false you can assume that location will not be returned
 * in onLocationResult(LocationResult) until something changes in the device's settings or
 * environment. Even when isLocationAvailable() returns true the onLocationResult(LocationResult)
 * may not always be called regularly, however the device location is known and both the most
 * recently delivered location and getLastLocation(GoogleApiClient) will be reasonably up to
 * date given the hints specified by the active LocationRequests.
 *
 * source: https://developers.google.com/android/reference/com/google/android/gms/location/LocationCallback.html#onLocationAvailability(com.google.android.gms.location.LocationAvailability)
 */

public class CurrentLocationTask extends LocationCallback {

    private LocationHandler handler;

    public CurrentLocationTask(LocationHandler handler) {
        this.handler = handler;
    }

    public void onLocationResult(LocationResult locationResult) {
        handler.onSuccess(locationResult.getLastLocation());
    }

    public void onLocationAvailability(LocationAvailability availability) {
        if(!availability.isLocationAvailable()) {
            handler.onError(new Exception(Messages.LOCATION_NOT_AVAILABLE));
        }
    }
}
