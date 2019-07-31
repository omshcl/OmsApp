package com.hcl.InstantPickup.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener, LocationConstants {

    private Context mContext;
    private static final double R = 6372.8; // In kilometers



    public MyLocationListener(Context context) {
        mContext = context;

    }
    @Override
    public void onLocationChanged(Location location) {
        double latitude= location.getLatitude();
        double longitude = location.getLatitude();
        double distance = haversine(myShopLat, myShopLong, latitude, longitude);
        if(distance < geoFenceRange) {
            ((LocationService)mContext).enteredShop();
        } else {
            ((LocationService)mContext).exitShop();
        }
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
