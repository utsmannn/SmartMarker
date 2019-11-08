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

package com.utsman.smartmarker.googlemaps

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

fun Location.toLatLngGoogle() = LatLng(latitude, longitude)

fun rotateMarker(marker: Marker, toRotation: Float, rotate: Boolean? = true) {

    if (rotate != null && rotate) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val duration: Long = 300

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = LinearInterpolator().getInterpolation(elapsed.toFloat() / duration)

                val rot = t * toRotation + (1 - t) * startRotation

                marker.rotation = if (-rot > 180) rot / 2 else rot
                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }
}

fun bitmapFromVector(context: Context, @DrawableRes icon: Int): BitmapDescriptor {

    val background = ContextCompat.getDrawable(context, icon)
    background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)

    val bitmap = Bitmap.createBitmap(background.intrinsicWidth, background.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    background.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun moveMarkerSmoothly(marker: Marker, newLatLng: LatLng) : ValueAnimator {
    val animator = ValueAnimator.ofFloat(0f, 100f)

    val deltaLatitude = doubleArrayOf(newLatLng.latitude - marker.position.latitude)
    val deltaLongitude = newLatLng.longitude - marker.position.longitude
    val prevStep = floatArrayOf(0f)
    animator.duration = 1500

    animator.addUpdateListener { animation ->
        val deltaStep = (animation.animatedValue as Float - prevStep[0]).toDouble()
        prevStep[0] = animation.animatedValue as Float
        val latLng = LatLng(marker.position.latitude + deltaLatitude[0] * deltaStep * 1.0 / 100, marker.position.longitude + deltaStep * deltaLongitude * 1.0 / 100)
        marker.setPosition(latLng)
    }

    return animator
}