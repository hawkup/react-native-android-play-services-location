package com.tellplus.location.response;

import android.location.Location;

/**
 * Created by hhravn on 22/06/2017.
 */
public interface LocationHandler {
    void onSuccess(Location listener);

    void onError(Throwable t);
}
