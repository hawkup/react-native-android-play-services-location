package com.tellplus.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

/**
 * Created by hhravn on 20/06/2017.
 */

public class LocationProvider {
    private final Context context;
    private FusedLocationProviderClient mFusedLocationClient;

    public LocationProvider(Context context) {
        this.context = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this.context);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }

        return true;
    }

    public void getCurrentLocation(LocationHandler observer) {
        CurrentLocationTask callback = new CurrentLocationTask(observer);
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), callback, null);
    }

    public void getLastKnownLocation(final LocationHandler observer){
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        observer.onSuccess(task.getResult());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        observer.onError(e);
                    }
                });
    }

    @NonNull
    private LocationRequest getLocationRequest() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setNumUpdates(1);
        locationRequest.setExpirationTime(TimeUnit.SECONDS.toMillis(5));
        locationRequest.setExpirationDuration(TimeUnit.SECONDS.toMillis(5));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

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

    class CurrentLocationTask extends LocationCallback{

        private LocationHandler handler;

        public CurrentLocationTask(LocationHandler handler) {
            this.handler = handler;
        }

        public void onLocationResult(LocationResult locationResult) {
            handler.onSuccess(locationResult.getLastLocation());
        }

        public void onLocationAvailability(LocationAvailability availability) {
            if(!availability.isLocationAvailable()) {
                handler.onError(new Exception("Location data is not available."));
            } else {
                getLastKnownLocation(this.handler);
            }
        }
    }

    interface LocationHandler {
        void onSuccess(Location listener);
        void onError(Throwable t);
    }
}
