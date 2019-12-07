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
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.style.layers.SymbolLayer

class MarkerOptions private constructor(
    val context: Context?,
    val id: String?,
    val icon: Int?,
    val vector: Boolean?,
    val latLng: LatLng?,
    val rotation: Double?,
    val symbolLayer: ((SymbolLayer) -> Unit)?) {

    data class Builder(
        private var context: Context? = null,
        private var id: String? = null,
        private var icon: Int = R.drawable.mapbox_marker_icon_default,
        private var vector: Boolean? = false,
        private var latLng: LatLng? = null,
        private var rotation: Double? = null,
        private var symbolLayer: ((SymbolLayer) -> Unit)? = null
    ) {
        fun setId(id: String) = apply { this.id = id }
        fun setIcon(icon: Int, vector: Boolean? = false) = apply {
            this.icon = icon
            this.vector = vector
        }
        fun setPosition(latLng: LatLng) = apply { this.latLng = latLng }
        fun setRotation(rotation: Double?) = apply { this.rotation = rotation }
        fun withSymbolLayer(symbolLayer: ((SymbolLayer) -> Unit)) = apply { this.symbolLayer = symbolLayer }

        fun build(context: Context) = MarkerOptions(context, id, icon, vector, latLng, rotation, symbolLayer)
    }

}

