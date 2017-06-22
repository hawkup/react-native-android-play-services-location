package com.tellplus.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.tellplus.location.request.CurrentLocationTask;
import com.tellplus.location.request.LocationRequestArgs;
import com.tellplus.location.response.LocationHandler;

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

    public void getCurrentLocation(LocationHandler observer, LocationRequestArgs args) {
        CurrentLocationTask callback = new CurrentLocationTask(observer);
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(args), callback, null);
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
    private LocationRequest getLocationRequest(LocationRequestArgs args) {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(args.maxAge);
        locationRequest.setExpirationTime(args.timeout);
        locationRequest.setExpirationDuration(args.timeout);
        locationRequest.setMaxWaitTime(args.timeout);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

}
