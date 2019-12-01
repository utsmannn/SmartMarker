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

package com.utsman.smartmarker.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

public class LocationWatcher {

    private Context context;
    private CompositeDisposable compositeDisposable;

    public static class Priority {
        public static long JEDI = 3;
        public static long VERY_HIGH = 30;
        public static long HIGH = 50;
        public static long MEDIUM = 300;
        public static long LOW = 3000;
        public static long VERY_LOW = 8000;
    }

    public LocationWatcher(Context context) {
        compositeDisposable = new CompositeDisposable();
        this.context = context;
    }

    public void stopLocationWatcher() {
        compositeDisposable.dispose();
    }

    public void getLocation(final Activity activity, final LocationListener locationListener) {

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getLocation(locationListener);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        activity.finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void getLocation(final LocationListener locationListener) {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
        Disposable locationDisposable = locationProvider
                .getUpdatedLocation(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        locationListener.location(location);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Log.e("anjay", "cannot update location --> " + throwable.getMessage());

                    }
                });

        compositeDisposable.add(locationDisposable);
    }

    public void getLocationUpdate(final Activity activity, final long priority, final LocationUpdateListener locationUpdateListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getLocationUpdate(priority, locationUpdateListener);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        activity.finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    public void getLocationUpdate(long priority, final LocationUpdateListener locationUpdateListener) {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100);

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
        Disposable locationDisposable = locationProvider
                .getUpdatedLocation(request)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        locationUpdateListener.oldLocation(location);
                    }
                })
                .delay(priority, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {
                        locationUpdateListener.newLocation(location);
                    }
                })

                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        locationUpdateListener.failed(throwable);
                    }
                })
                .subscribe();

        compositeDisposable.add(locationDisposable);
    }
}
