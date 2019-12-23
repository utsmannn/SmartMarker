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

package com.utsman.smartmarker.mapbox

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.lifecycle.MutableLiveData
import com.mapbox.android.gestures.RotateGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class Marker(private var currentLatLng: LatLng,
             private val mapboxMap: MapboxMap,
             private val jsonSource: GeoJsonSource,
             private val symbolLayer: SymbolLayer,
             private val idMarker: String) {

    private val liveRotate = MutableLiveData<Float>()
    private var animator: ValueAnimator? = null
    private var heading = 0.0
    fun getId() = idMarker
    fun getRotation() = heading

    private val liveVisibility = MutableLiveData<Boolean>()

    init {
        liveRotate.postValue(0f)
        liveRotate.observeForever {
            symbolLayer.withProperties(PropertyFactory.iconRotate(it))
        }

        liveVisibility.observeForever {
            symbolLayer.withProperties(PropertyFactory.iconRotate(160f))
        }
    }

    internal fun rotateMarker(rotate: Double) {
        rotateMarker(symbolLayer, rotate.toFloat())
    }

    fun moveMarkerSmoothly(newLatLng: LatLng, @Nullable rotate: Boolean? = true) {
        if (animator != null && animator!!.isStarted) {
            currentLatLng = animator!!.animatedValue as LatLng
            animator!!.cancel()
        }

        animator = ObjectAnimator.ofObject(latLngEvaluator, currentLatLng, newLatLng).apply {
            duration = 2000
            addUpdateListener(animatorUpdateListener(jsonSource))
        }

        animator?.start()

        if (rotate != null && rotate) {
            mapboxMap.addOnRotateListener(object : MapboxMap.OnRotateListener {
                override fun onRotate(detector: RotateGestureDetector) {

                }

                override fun onRotateEnd(detector: RotateGestureDetector) {

                }

                override fun onRotateBegin(detector: RotateGestureDetector) {

                }

            })

            rotateMarker(symbolLayer, (getAngle(currentLatLng, newLatLng)).toFloat())
        }
    }

    fun getLatLng() = currentLatLng

    fun getSource() = jsonSource

    private fun animatorUpdateListener(jsonSource: GeoJsonSource) : ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener { value ->
            val animatedLatLng = value.animatedValue as LatLng
            jsonSource.setGeoJson(Point.fromLngLat(animatedLatLng.longitude, animatedLatLng.latitude))
        }
    }

    private fun rotateMarker(symbolLayer: SymbolLayer?, toRotation: Float) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        var startRotation = symbolLayer?.iconRotate?.value ?: 90f
        val duration: Long = 200

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = LinearInterpolator().getInterpolation( elapsed.toFloat() / duration)

                try {
                    val rot = t * toRotation + (1 - t) * startRotation

                    val rotation = if (-rot > 180) rot / 2 else rot
                    liveRotate.postValue(rotation)
                    currentLatLng = animator!!.animatedValue as LatLng
                    startRotation = rotation
                    if (t < 1.0) {
                        handler.postDelayed(this, 100)
                    }
                } catch (e: IllegalStateException) {
                    Log.e("smartmarker-mapbox", "set rotation failed")
                }

            }
        })
    }

    private fun getAngle(fromLatLng: LatLng, toLatLng: LatLng) : Double {
        if (fromLatLng != toLatLng) {
            val mapRot = mapboxMap.cameraPosition.bearing

            heading = MathUtil.computeHeading(fromLatLng, toLatLng, mapRot)

            logi("angle is --> $heading -- $mapRot")
        }

        return heading
    }

    private fun logi(msg: String?) = Log.i("anjay", msg)
}
