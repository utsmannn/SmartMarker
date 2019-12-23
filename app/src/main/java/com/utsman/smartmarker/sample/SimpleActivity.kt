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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import com.utsman.smartmarker.location.LocationWatcher
import com.utsman.smartmarker.mapbox.MarkerOptions
import com.utsman.smartmarker.mapbox.addMarker
import com.utsman.smartmarker.mapbox.toLatLngMapbox
import kotlinx.android.synthetic.main.activity_simple.*

class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "sk.eyJ1Ijoia3VjaW5nYXBlcyIsImEiOiJjazMxaTMwdnUwNmZhM2RxZnN3MXB3NXVxIn0.SbImlZrtwRkxSpk-1h0h3A")
        setContentView(R.layout.activity_simple)

        val locationWatcher = LocationWatcher(this)
        locationWatcher.getLocation(this) {

            mapbox_view.getMapAsync { mapboxMap ->
                mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->

                    val latlng = it.toLatLngMapbox()

                    val markerOptions = MarkerOptions.Builder()
                        .setId("layer-id")
                        .setIcon(R.drawable.ic_marker_direction_2, true)
                        .setPosition(latlng)
                        .build(this)

                    val marker = mapboxMap.addMarker(markerOptions)

                    btn_test.setOnClickListener {
                        style.removeLayer(marker.getId())
                    }



                    mapboxMap.addOnMapLongClickListener {
                        marker.moveMarkerSmoothly(it)
                        true
                    }
                }
            }
        }
    }
}