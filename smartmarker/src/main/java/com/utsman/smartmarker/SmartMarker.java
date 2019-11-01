package com.utsman.smartmarker;

import android.animation.ValueAnimator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.utsman.smartmarker.googlemaps.ExtKt;
import com.utsman.smartmarker.mapbox.MarkerUtil;

public class SmartMarker {
    public static void moveMarkerSmoothly(final Marker marker, LatLng newLatLng) {
        ValueAnimator animator = ExtKt.moveMarkerSmoothly(marker, newLatLng);

        animator.start();
        float f = (float) SmartUtil.getAngle(new SmartLatLon(marker.getPosition().latitude, marker.getPosition().longitude), new SmartLatLon(newLatLng.latitude, newLatLng.longitude));
        ExtKt.rotateMarker(marker, f);
    }

    public static void moveMarkerSmoothly(MarkerUtil.Marker marker, com.mapbox.mapboxsdk.geometry.LatLng newLatLng) {
        marker.moveMarkerAnimation(newLatLng);
    }
}
