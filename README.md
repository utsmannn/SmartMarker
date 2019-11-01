# Smart Marker For Google Maps and Mapbox
### This library for helper movement marker in your maps

<p align="center">
  <img src="https://i.ibb.co/Wspktd7/ezgif-com-gif-maker-1.gif"/>
</p>

## Download
```groovy

// the core library
implementation 'com.utsman.smartmarker:core:0.0.1@aar'

// extension for google maps
implementation 'com.utsman.smartmarker:ext-googlemaps:0.0.1'

// extension for Mapbox
implementation 'com.utsman.smartmarker:ext-mapbox:0.0.1' 

// extension for easy get location every second
implementation 'com.utsman.smartmarker:ext-location:0.0.1'

```
For extensions, you don't need to add mapbox extensions if you don't use the sdk mapbox. As well as the google map sdk.

## Use

### Google Maps
```kotlin
SmartMarker.moveMarkerSmoothly(marker, location)
```

### Mapbox
For Mapbox, adding marker is little hard, so I create helper for it

```kotlin
// Define marker util
val markerUtil = MarkerUtil(context)

// Define mapbox marker
var mapboxMarker: MarkerUtil.Marker? = null

// add your marker
mapboxMarker = markerUtil.addMarker("marker-id", style, R.drawable.ic_marker, true, latlng) // if marker is not vector, use 'false'

// and move your marker smoothly
SmartMarker.moveMarkerSmoothly(mapboxMarker, location)

```

### Location Extension
I create location extensions for get your location every second with old location and new location (delay 30 millisecond)
```kotlin
// define location watcher
val locationWatcher: LocationWatcher = LocationWatcher(context)

// get location once time
locationWatcher.getLocation { location ->
    // your location result
}

// get location once time with permission helper
locationWatcher.getLocation(context) { location ->
    // your location result
}

// get location update every second
locationWatcher.getLocationUpdate(object : LocationUpdateListener {
    override fun oldLocation(oldLocation: Location?) {
        // your location realtime result
    }
    
    override fun newLocation(newLocation: Location?) {
        // your location past with delay 30 millisecond (0.03 second)
    }

    override fun failed(throwable: Throwable?) {
        // if location failed
    }
})

// get location update every second with permission helper
locationWatcher.getLocationUpdate(this, object : LocationUpdateListener {
    override fun oldLocation(oldLocation: Location?) {
        // your location realtime result
    }
    
    override fun newLocation(newLocation: Location?) {
        // your location past with delay 30 millisecond (0.03 second)
    }

    override fun failed(throwable: Throwable?) {
        // if location failed
    }
})

// stop your watcher in onStop activity
override fun onStop() {
    locationWatcher.stopLocationWatcher()
    super.onStop()
}

```

## Other Extensions
```kotlin
// Convert Location to LatLng for Google Maps
location.toLatLngGoogle()

// Convert Location to LatLng for Mapbox
location.toLatLngMapbox()

// use marker as vector for Google Maps
bitmapFromVector(context, R.drawable.marker_vector)

```

## Simple Example
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var locationWatcher: LocationWatcher
    private var googleMarker: Marker? = null
    private var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationWatcher = LocationWatcher(this)
        val googleMapsView = (google_map_view as SupportMapFragment)

        locationWatcher.getLocation(this) { loc ->
            googleMapsView.getMapAsync {  map ->
                googleMap = map
                val markerOption = MarkerOptions()
                    .position(loc.toLatLngGoogle())
                    .icon(bitmapFromVector(this@MainActivity, R.drawable.ic_marker_direction_2))

                googleMarker = map.addMarker(markerOption)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc.toLatLngGoogle(), 17f))
            }
        }

        locationWatcher.getLocationUpdate(this, object : LocationUpdateListener {
            override fun oldLocation(oldLocation: Location) {

            }

            override fun newLocation(newLocation: Location) {
                googleMarker?.let { marker ->
                    SmartMarker.moveMarkerSmoothly(marker, newLocation.toLatLngGoogle())
                }
            }

            override fun failed(throwable: Throwable?) {
            }
        })
    }
}
```

Sample [MainActivity.kt](https://github.com/utsmannn/SmartMarker/blob/master/app/src/main/java/com/utsman/smartmarker/sample/MainActivity.kt)

---
```
Copyright 2019 Muhammad Utsman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
---
makasih