package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by vlad on 18.01.18
 */

@SuppressLint("Registered")
public class GeoLocation {
    private LocationManager locationManager;
    @Nullable
    private OnLocationChangedCallback mLocationChangedCallback;
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

    GeoLocation(LocationManager locationManager, @Nullable OnLocationChangedCallback activity) {
        mLocationChangedCallback = activity;
        this.locationManager = locationManager;
        geoLocation();
    }

    void unsubscribe() {
        locationManager.removeUpdates(locationListener);
        mLocationChangedCallback = null;
    }

    @SuppressLint("MissingPermission")
    private void geoLocation() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 10,
                locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 10,
                locationListener);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) & !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            assert mLocationChangedCallback != null;
            mLocationChangedCallback.geoLocationSetting();
        }
    }

    private void showLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        assert mLocationChangedCallback != null;
        mLocationChangedCallback.onLocationChanged(lat, lon);
        locationManager.removeUpdates(locationListener);
    }

    public interface OnLocationChangedCallback {
        void onLocationChanged(String lat, String lon);

        void geoLocationSetting();
    }
}
