package com.hcl.InstantPickup.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.location.LocationConstants;

/** Creates a fragment to display maps activity
 * with store location
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class YourStoreFragment extends Fragment  {
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(LocationConstants.shopLatLng)
                        .zoom(LocationConstants.shopZoomLevel)
                        .bearing(LocationConstants.shopBearing)
                        .tilt(LocationConstants.shopTilt)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), LocationConstants.shopAnimationTime, null);

                mMap.addMarker(new MarkerOptions()
                        .position(LocationConstants.shopLatLng)
                        .title(LocationConstants.shopIconTitle)
                        .snippet(getString(R.string.store_name)+": Frisco location "));
            }
        });

        return rootView;


    }
}
