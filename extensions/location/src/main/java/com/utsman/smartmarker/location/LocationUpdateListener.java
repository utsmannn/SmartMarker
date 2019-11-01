package com.utsman.smartmarker.location;

import android.location.Location;

public interface LocationUpdateListener {
    void oldLocation(Location oldLocation);
    void newLocation(Location newLocation);
    void failed(Throwable throwable);
}