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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.utsman.kemana.maputil.MarkerBuilder
import com.utsman.kemana.maputil.addMarker

// class FoodOrder private constructor(
//   val bread: String?,
//   val condiments: String?,
//   val meat: String?,
//   val fish: String?) {
//
//     data class Builder(
//       var bread: String? = null,
//       var condiments: String? = null,
//       var meat: String? = null,
//       var fish: String? = null) {
//
//         fun bread(bread: String) = apply { this.bread = bread }
//         fun condiments(condiments: String) = apply { this.condiments = condiments }
//         fun meat(meat: String) = apply { this.meat = meat }
//         fun fish(fish: String) = apply { this.fish = fish }
//         fun build() = FoodOrder(bread, condiments, meat, fish)
//     }
// }

class MarkerOptions private constructor(
    val context: Context?,
    val id: String?,
    val icon: Int?,
    val vector: Boolean?,
    val latLng: LatLng?,
    val symbolLayer: ((SymbolLayer) -> Unit)?) {

    data class Builder(
        private var context: Context? = null,
        private var id: String? = null,
        private var icon: Int = R.drawable.mapbox_marker_icon_default,
        private var vector: Boolean? = false,
        private var latLng: LatLng? = null,
        private var symbolLayer: ((SymbolLayer) -> Unit)? = null
    ) {
        fun setId(id: String) = apply { this.id = id }
        fun addIcon(icon: Int, vector: Boolean? = false) = apply {
            this.icon = icon
            this.vector = vector
        }
        fun addPosition(latLng: LatLng) = apply { this.latLng = latLng }
        fun withSymbolLayer(symbolLayer: ((SymbolLayer) -> Unit)) = apply { this.symbolLayer = symbolLayer }

        fun build(context: Context) = MarkerOptions(context, id, icon, vector, latLng, symbolLayer)
    }




    /*fun Style.addMarker(context: Context, markerOptions: MarkerOptions): Marker {
        val markerBuilder = MarkerBuilder.Builder(context).build()
    }*/

}

