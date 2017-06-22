package com.tellplus.location.request;

/**
 * Created by hhravn on 22/06/2017.
 */
public class LocationRequestArgs {
    public final int timeout;
    public final int maxAge;

    public LocationRequestArgs(int timeout, int maxAge) {
        this.timeout = timeout;
        this.maxAge = maxAge;
    }
}
