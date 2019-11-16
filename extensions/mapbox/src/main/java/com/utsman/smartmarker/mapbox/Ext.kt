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

import android.animation.TypeEvaluator
import android.location.Location
import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

fun Location.toLatLngMapbox() = LatLng(latitude, longitude)

val latLngEvaluator = object : TypeEvaluator<LatLng> {
    val latLng = LatLng()

    override fun evaluate(f: Float, startLatLng: LatLng, endLatlng: LatLng): LatLng {
        latLng.latitude = startLatLng.latitude + (endLatlng.latitude - startLatLng.latitude) * f
        latLng.longitude = startLatLng.longitude + (endLatlng.longitude - startLatLng.longitude) * f
        return latLng
    }
}

fun MapboxMap.addMarker(vararg markerOptions: MarkerOptions): MarkerLayer {
    //val list: MarkerLayer = mutableListOf<Marker>() as MarkerLayer
    val markerLayer = MarkerLayer()
    markerOptions.map { options ->
        val markerBuilder = MarkerBuilder(options.context!!, style)
        val marker = markerBuilder.newMarker(options.id!!, options.latLng!!, options.icon!!, options.vector!!)
        options.symbolLayer?.invoke(marker)
        style?.addMarker(marker)

        val jsonSource = markerBuilder.jsonSource
        val mark = Marker(options.latLng, this, jsonSource, marker, options.id)
        markerLayer.add(mark)
    }
    return markerLayer
}


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MarkerLayer {
    private val markers: MutableList<Marker> = mutableListOf()

    fun add(marker: Marker) = apply { markers.add(marker) }
    /*fun get(id: String) = try {
        markers.single { id == it.getId() }
    } catch (e: NoSuchElementException) {

    }*/

    fun get(id: String): Marker? {
        return try {
            markers.single { id == it.getId() }
        } catch (e: NoSuchElementException) {
            Log.e("SmartMarker-mapbox", e.message)
            return null
        }
    }
}