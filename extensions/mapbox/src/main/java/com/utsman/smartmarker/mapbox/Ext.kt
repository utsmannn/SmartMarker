package com.utsman.smartmarker.mapbox

import android.animation.TypeEvaluator
import android.location.Location
import com.mapbox.mapboxsdk.geometry.LatLng

fun Location.toLatLngMapbox() = LatLng(latitude, longitude)

val latLngEvaluator = object : TypeEvaluator<LatLng> {
    val latLng = LatLng()

    override fun evaluate(f: Float, startLatLng: LatLng, endLatlng: LatLng): LatLng {
        latLng.latitude = startLatLng.latitude + (endLatlng.latitude - startLatLng.latitude) * f
        latLng.longitude = startLatLng.longitude + (endLatlng.longitude - startLatLng.longitude) * f
        return latLng
    }
}