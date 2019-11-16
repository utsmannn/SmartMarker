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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.utsman.smartmarker.SmartMarker
import com.utsman.smartmarker.googlemaps.bitmapFromVector
import com.utsman.smartmarker.googlemaps.toLatLngGoogle
import com.utsman.smartmarker.location.LocationUpdateListener
import com.utsman.smartmarker.location.LocationWatcher
import com.utsman.smartmarker.mapbox.addMarker
import com.utsman.smartmarker.mapbox.toLatLngMapbox
import com.utsman.smartmarker.moveMarkerSmoothly
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var locationWatcher: LocationWatcher
    private var googleMarker: Marker? = null
    private var mapboxMarker: com.utsman.smartmarker.mapbox.Marker? = null

    private var googleMap: GoogleMap? = null
    private var mapboxMap: MapboxMap? = null

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "sk.eyJ1Ijoia3VjaW5nYXBlcyIsImEiOiJjazMxaTMwdnUwNmZhM2RxZnN3MXB3NXVxIn0.SbImlZrtwRkxSpk-1h0h3A")
        setContentView(R.layout.activity_main)

        locationWatcher = LocationWatcher(this)
        val googleMapsView = (google_map_view as SupportMapFragment)
        val mapboxMapsView = findViewById<MapView>(R.id.mapbox_view)


        // get location once time
        locationWatcher.getLocation(this) { loc ->

            // google maps async
            googleMapsView.getMapAsync {  map ->
                googleMap = map

                val markerOption = MarkerOptions()
                    .position(loc.toLatLngGoogle())
                    .icon(bitmapFromVector(this@MainActivity, R.drawable.ic_marker_direction_2))

                googleMarker = map.addMarker(markerOption)


                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc.toLatLngGoogle(), 17f))
            }

            // mapbox async
            mapboxMapsView.getMapAsync {  map ->
                mapboxMap = map

                map.setStyle(Style.OUTDOORS) { style ->
                    /**
                     * version 0.0.3
                     * */
                    /*val markerUtil = com.utsman.smartmarker.mapbox.MarkerOptions(this, style)
                    mapboxMarker = markerUtil.addMarker("driver", R.drawable.ic_marker_direction_2, true, loc.toLatLngMapbox())*/
                    /**
                     * End version 0.0.3
                     * */

                    /**
                     * version 1.0.3 use MarkerOptions
                     * */
                    val markerOptions = com.utsman.smartmarker.mapbox.MarkerOptions.Builder()
                        .setId("id")
                        .addIcon(R.drawable.mapbox_marker_icon_default)
                        .addPosition(loc.toLatLngMapbox())
                        .build(this)

                    val bnd = com.mapbox.mapboxsdk.geometry.LatLng(-6.914744, 107.609810)
                    val jgkt = com.mapbox.mapboxsdk.geometry.LatLng(-7.797068, 110.370529)
                    val bks = com.mapbox.mapboxsdk.geometry.LatLng(-6.241586, 106.992416)
                    val btn = com.mapbox.mapboxsdk.geometry.LatLng(-6.120000, 106.150276)

                    val markerOptions1 = com.utsman.smartmarker.mapbox.MarkerOptions.Builder()
                        .setId("jgja")
                        .addIcon(R.drawable.ic_marker_direction_2, true)
                        .addPosition(jgkt)
                        .build(this)

                    val markerOptions2 = com.utsman.smartmarker.mapbox.MarkerOptions.Builder()
                        .setId("bks")
                        .addIcon(R.drawable.ic_marker_direction_2, true)
                        .addPosition(bks)
                        .build(this)

                    val markerOptions3 = com.utsman.smartmarker.mapbox.MarkerOptions.Builder()
                        .setId("btn")
                        .addPosition(btn)
                        .build(this)

                    //val markers = map.addMarkers(markerOptions1, markerOptions2, markerOptions3)

                    val markers = map.addMarker(markerOptions3)

                    val aa = markers.get("")

                    btn_test.setOnClickListener {

                        //markers.getId("btn").moveMarkerSmoothly(bks)
                        markers.get("btn")?.moveMarkerSmoothly(bks)

                        /*markers.filter {
                            it.getId() != "bks"
                        }.apply {
                            map {
                                it.moveMarkerSmoothly(bnd)
                            }
                        }*/
                    }



                    //val bndMarker = map.addMarker(markerOptions1)

                    val position = CameraPosition.Builder()
                        .target(loc.toLatLngMapbox())
                        .zoom(15.0)
                        .build()

                    map.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(position))
                }
            }
        }

        // update camera to marker every 5 second
        timer {
            //googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it.toLatLngGoogle(), 17f))
            val position = CameraPosition.Builder()
                .target(it.toLatLngMapbox())
                .zoom(15.0)
                .build()

            //mapboxMap?.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition(position))
        }

        // update your location
        updateLocation()
    }

    private fun updateLocation() {
        locationWatcher.getLocationUpdate(this, object : LocationUpdateListener {
            override fun oldLocation(oldLocation: Location) {

            }

            override fun newLocation(newLocation: Location) {
                googleMarker?.moveMarkerSmoothly(newLocation.toLatLngGoogle())


                mapboxMarker?.let { marker ->
                    SmartMarker.moveMarkerSmoothly(marker, newLocation.toLatLngMapbox())
                }
            }

            override fun failed(throwable: Throwable?) {
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

        disposable.add(obs)
    }

    override fun onDestroy() {
        locationWatcher.stopLocationWatcher()
        disposable.dispose()
        super.onDestroy()
    }
}
