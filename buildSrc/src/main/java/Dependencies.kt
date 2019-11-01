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