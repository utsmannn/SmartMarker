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

const val kotlin_version = "1.3.50"

object Core {
    val kotlin              = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    val appCompat           = "androidx.appcompat:appcompat:1.0.2"
    val ktx                 = "androidx.core:core-ktx:1.0.2"
    val permission          = "com.karumi:dexter:5.0.0"
}

object Maps {
    val mapbox              = "com.mapbox.mapboxsdk:mapbox-android-sdk:8.4.0"
    val googleMap           = "com.google.android.gms:play-services-maps:17.0.0"
    val gmsLocationService  = "com.google.android.gms:play-services-location:17.0.0"
    val rxLocation          = "pl.charmas.android:android-reactive-location2:2.1@aar"
}

object Rx {
    val rxJava              = "io.reactivex.rxjava2:rxjava:2.2.9"
    val rxAndroid           = "io.reactivex.rxjava2:rxandroid:2.1.1"
}

object Module {
    val smartMarker         = ":smartmarker"
    val extGoogleMaps       = ":extensions:googlemaps"
    val extMapbox           = ":extensions:mapbox"
}