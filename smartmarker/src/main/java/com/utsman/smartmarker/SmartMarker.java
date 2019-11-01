/*
 * Copyright 2019 Muhammad Utsman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
