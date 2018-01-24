package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by vlad on 18.01.18
 */

@SuppressLint("Registered")
class GeoLocation {
    private LocationManager locationManager;

    private OnLocationChangedCallback locationChangedCallback;

    interface  OnLocationChangedCallback {
        void onLocationChanged(String lat,String lon);
    }

    GeoLocation(LocationManager locationManager, OnLocationChangedCallback activity) {
        locationChangedCallback = activity;
        this.locationManager = locationManager;
        geoLocation();

    }


    @SuppressLint("MissingPermission")
    private void geoLocation() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 10,
                locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 10,
                locationListener);
        Log.i("EnabledGPS: ", String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
        Log.i("EnabledNetwork: ", String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }
    };

    private void showLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        Log.i("LocationLAT", lat);
        Log.i("LocationLON", lon);
        locationChangedCallback.onLocationChanged(lat, lon);
        locationManager.removeUpdates(locationListener);
    }
}
