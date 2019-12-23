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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils

class MarkerBuilder(private val context: Context, private val style: Style?) {

    lateinit var jsonSource: GeoJsonSource
    lateinit var symbolLayer: SymbolLayer

    private fun newSymbol(id: String): SymbolLayer {
        return SymbolLayer(id, "source-$id")
    }

    internal fun newMarker(id: String, latLng: LatLng, @DrawableRes iconVector: Int, vector: Boolean = false): SymbolLayer {
        symbolLayer = newSymbol(id)

        jsonSource = GeoJsonSource(
            "source-$id",
            Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude))
        )

        if (vector) {
            style?.addImage("marker-$id", markerVector(context, iconVector))
        } else {
            val markerBitmap =
                BitmapFactory.decodeResource(context.resources, iconVector)
            try {
                style?.addImage("marker-$id", markerBitmap)
            } catch (e: NullPointerException) {
                Log.e("SmartMarker-mapbox", "Cannot process bitmap, maybe your marker is vector, please add 'true' in 'addIcon'")
            }
        }


        style?.addSource(jsonSource)

        symbolLayer.withProperties(
            PropertyFactory.iconImage("marker-$id"),
            PropertyFactory.iconIgnorePlacement(true),
            PropertyFactory.iconAllowOverlap(true)
        )

        Log.i("symbol layer id is --> ", id)
        return symbolLayer
    }
}

internal fun Style.addMarker(symbolLayer: SymbolLayer) {
    addLayer(symbolLayer)
}

internal fun markerVector(context: Context, @DrawableRes marker: Int): Bitmap {
    val drawable = ResourcesCompat.getDrawable(context.resources,
        marker, null)
    return BitmapUtils.getBitmapFromDrawable(drawable) ?: BitmapFactory.decodeResource(context.resources,
        R.drawable.mapbox_marker_icon_default
    )
}