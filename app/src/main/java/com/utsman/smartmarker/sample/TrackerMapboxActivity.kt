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

package com.utsman.smartmarker.sample

import android.location.Location
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.gestures.RotateGestureDetector
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.utsman.smartmarker.location.LocationUpdateListener
import com.utsman.smartmarker.location.LocationWatcher
import com.utsman.smartmarker.mapbox.Marker
import com.utsman.smartmarker.mapbox.MarkerOptions
import com.utsman.smartmarker.mapbox.addMarker
import com.utsman.smartmarker.mapbox.toLatLngMapbox
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tracker_mapbox.*
import java.util.concurrent.TimeUnit

class TrackerMapboxActivity : AppCompatActivity() {

    private val locationWatcher by lazy {
        LocationWatcher(this)
    }

    private val compositeDisposable = CompositeDisposable()

    private var marker1: Marker? = null
    private var marker2: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "sk.eyJ1Ijoia3VjaW5nYXBlcyIsImEiOiJjazMxaTMwdnUwNmZhM2RxZnN3MXB3NXVxIn0.SbImlZrtwRkxSpk-1h0h3A")
        setContentView(R.layout.activity_tracker_mapbox)

        mapbox_view_1.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) { style ->

                val updatePosition = CameraPosition.Builder()
                    .zoom(17.0)
                    .build()

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(updatePosition))

                val locationComponentOption = LocationComponentOptions.builder(this)
                    .bearingTintColor(R.color.colorPrimary)
                    //.accuracyAlpha(0.8f) // 0.0f - 1.0f
                    .build()

                val locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(this, style)
                    .locationComponentOptions(locationComponentOption)
                    .useDefaultLocationEngine(true)
                    .build()

                val locationComponent = mapboxMap.locationComponent
                locationComponent.activateLocationComponent(locationComponentActivationOptions)

                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING_GPS
                locationComponent.renderMode = RenderMode.GPS


                timer {
                    moveCamera(it, mapboxMap)
                }
            }
        }

        mapbox_view_2.getMapAsync { mapboxMap ->

            mapboxMap.setStyle(Style.OUTDOORS) { style ->

                locationWatcher.getLocation(this) { location ->
                    val markerOptions = MarkerOptions.Builder()
                        .setId("id")
                        .addIcon(R.drawable.ic_marker_direction_2, true)
                        .addPosition(location.toLatLngMapbox())
                        .build(this)

                    marker1 = mapboxMap.addMarker(markerOptions).get("id")

                    moveCamera(location, mapboxMap)
                    updateLocation()

                    mapboxMap.addOnRotateListener(object : MapboxMap.OnRotateListener {
                        override fun onRotate(detector: RotateGestureDetector) {

                        }

                        override fun onRotateEnd(detector: RotateGestureDetector) {
                            toast("rotation --> ${mapboxMap.cameraPosition.bearing}")
                            logi("rotation --> ${mapboxMap.cameraPosition.bearing}")
                        }

                        override fun onRotateBegin(detector: RotateGestureDetector) {

                        }

                    })

                    timer {
                        moveCamera(it, mapboxMap)
                    }
                }
            }
        }

        mapbox_view_3.getMapAsync {  mapboxMap ->
            mapboxMap.setStyle(Style.OUTDOORS) { style ->

                locationWatcher.getLocation(this) { location ->
                    val markerOptions = MarkerOptions.Builder()
                        .setId("id")
                        .addIcon(R.drawable.ic_marker_direction_2, true)
                        .addPosition(location.toLatLngMapbox())
                        .build(this)

                    marker2 = mapboxMap.addMarker(markerOptions).get("id")

                    moveCamera(location, mapboxMap)
                    updateLocation()

                    mapboxMap.addOnRotateListener(object : MapboxMap.OnRotateListener {
                        override fun onRotate(detector: RotateGestureDetector) {

                        }

                        override fun onRotateEnd(detector: RotateGestureDetector) {
                            toast("rotation --> ${mapboxMap.cameraPosition.bearing}")
                            logi("rotation --> ${mapboxMap.cameraPosition.bearing}")
                        }

                        override fun onRotateBegin(detector: RotateGestureDetector) {

                        }

                    })

                    val updatePosition = CameraPosition.Builder()
                        .zoom(17.0)
                        .build()

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(updatePosition))

                    val locationComponentOption = LocationComponentOptions.builder(this)
                        .bearingTintColor(R.color.colorPrimary)
                        //.accuracyAlpha(0.8f) // 0.0f - 1.0f
                        .build()

                    val locationComponentActivationOptions = LocationComponentActivationOptions
                        .builder(this, style)
                        .locationComponentOptions(locationComponentOption)
                        .useDefaultLocationEngine(true)
                        .build()

                    val locationComponent = mapboxMap.locationComponent
                    locationComponent.activateLocationComponent(locationComponentActivationOptions)

                    locationComponent.isLocationComponentEnabled = true
                    locationComponent.cameraMode = CameraMode.TRACKING_GPS
                    locationComponent.renderMode = RenderMode.GPS

                    timer {
                        moveCamera(it, mapboxMap)
                    }
                }
            }

        }
    }

    private fun moveCamera(it: Location, mapboxMap: MapboxMap) {
        val updatePosition = CameraPosition.Builder()
            .target(it.toLatLngMapbox())
            .zoom(17.0)
            .build()

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(updatePosition))
    }

    private fun updateLocation() {
        locationWatcher.getLocationUpdate(LocationWatcher.Priority.JEDI, object : LocationUpdateListener {
            override fun oldLocation(oldLocation: Location) {

            }

            override fun newLocation(newLocation: Location) {
                marker1?.moveMarkerSmoothly(newLocation.toLatLngMapbox())
                marker2?.moveMarkerSmoothly(newLocation.toLatLngMapbox())
            }

            override fun failed(throwable: Throwable) {

            }

        })
    }

    private fun timer(ready: (Location) -> Unit) {
        val obs = Observable.interval(5000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                locationWatcher.getLocation(this) {
                    ready.invoke(it)
                }
            }

        compositeDisposable.add(obs)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        locationWatcher.stopLocationWatcher()
    }
}