package com.tellplus.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;

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

    public SingleSource<Location> getCurrentLocation() {
        CurrentLocationCallback callback = new CurrentLocationCallback();
        mFusedLocationClient.requestLocationUpdates(getLocationRequest(), callback, null);
        return callback;
    }

    public SingleSource<Location> getLastKnownLocation(){
        return new LocationTaskSource(mFusedLocationClient.getLastLocation());
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

    private class LocationTaskSource implements SingleSource<Location>, OnCompleteListener<Location>, OnFailureListener {
        private SingleObserver<? super Location> observer;

        LocationTaskSource(Task<Location> task){
            task.addOnCompleteListener(this);
            task.addOnFailureListener(this);
        }

        @Override
        public void subscribe(@io.reactivex.annotations.NonNull SingleObserver<? super Location> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Location> task) {
            if(this.observer != null)
                this.observer.onSuccess(task.getResult());
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            if(this.observer != null)
                this.observer.onError(e);

        }
    }

    private class CurrentLocationCallback extends LocationCallback implements SingleSource<Location>
    {
        private SingleObserver<? super Location> observer;

        public void onLocationResult(LocationResult locationResult) {
            if(this.observer != null)
                this.observer.onSuccess(locationResult.getLastLocation());
        }

        @Override
        public void subscribe(@io.reactivex.annotations.NonNull SingleObserver<? super Location> observer) {
            this.observer = observer;
        }
    }
}
