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

package com.utsman.kemana.maputil

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.utsman.smartmarker.mapbox.MarkerUtil

class MarkerBuilder(private val context: Context, private val style: Style) {

    internal lateinit var jsonSource: GeoJsonSource

    private fun newSymbol(id: String): SymbolLayer {
        return SymbolLayer("layer-$id", "source-$id")
    }

    internal fun newMarker(id: String, latLng: LatLng, @DrawableRes iconVector: Int, vector: Boolean = false): SymbolLayer {
        val symbolLayer = newSymbol(id)
        val markerUtil = MarkerUtil(context)

        jsonSource = GeoJsonSource(
            "source-$id",
            Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude))
        )

        if (vector) {
            style.addImage("marker-$id", markerUtil.markerVector(iconVector))
        } else {
            val markerBitmap =
                BitmapFactory.decodeResource(context.resources, iconVector)
            style.addImage("marker-$id", markerBitmap)
        }


        style.addSource(jsonSource)

        symbolLayer.withProperties(
            PropertyFactory.iconImage("marker-$id"),
            PropertyFactory.iconIgnorePlacement(true),
            PropertyFactory.iconAllowOverlap(true)
        )
        return symbolLayer
    }
}

internal fun Style.addMarker(symbolLayer: SymbolLayer) {
    addLayer(symbolLayer)
}