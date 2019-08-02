package com.hcl.InstantPickup.location;

import com.google.android.gms.maps.model.LatLng;

public class LocationConstants {
    double myShopLat = 33.09948150944979;
    double myShopLong = -96.8288957057522;
    LatLng shopLatLng = new LatLng(myShopLat,myShopLong);
    int shopZoomLevel = 14;
    int shopBearing = 0;
    int shopTilt = 45;
    int shopAnimationTime =2000;
    String shopIconTitle = "Our Store";
    double geoFenceRange = 0.1;

}
