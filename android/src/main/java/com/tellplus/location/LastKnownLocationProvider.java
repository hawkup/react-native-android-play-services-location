package com.tellplus.location;

import android.content.Context;
import android.location.Location;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by hhravn on 20/06/2017.
 */

public class LastKnownLocationProvider implements GoogleApiClient.ConnectionCallbacks {
    private final Context context;
    private GoogleApiClient mFusedLocationClient;
    private boolean isConnected;

    public LastKnownLocationProvider(Context context) {
        this.context = context;
        mFusedLocationClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        mFusedLocationClient.connect();
    }

    @Override
    protected void finalize() throws Throwable {
        mFusedLocationClient.disconnect();
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this.context);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }

        return true;
    }

    public Location getLastKnownLocation(int maxAge) {
        return LocationServices.FusedLocationApi.getLastLocation(this.mFusedLocationClient);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.isConnected = false;
    }
}
