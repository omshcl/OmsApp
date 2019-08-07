package com.hcl.InstantPickup.location;

import com.google.android.gms.maps.model.LatLng;

/** Stores constant values used
 * for location tracking
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class LocationConstants {
    public final static double myShopLat = 33.09948150944979;
    public final static double myShopLong = -96.8288957057522;
    public final static LatLng shopLatLng = new LatLng(myShopLat,myShopLong);
    public final static int shopZoomLevel = 14;
    public final static int shopBearing = 0;
    public final static int shopTilt = 45;
    public final static int shopAnimationTime =2000;
    public final static String shopIconTitle = "Our Store";
    public final static double geoFenceRange = 0.1;

}
