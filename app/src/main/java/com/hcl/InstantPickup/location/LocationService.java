package com.hcl.InstantPickup.location;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.activities.CustomerDashboard;

public class LocationService extends Service {


    private final IBinder binder = new LocalBinder();
    private LocationTrackingCallback serviceCallbacks;

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        return super.onStartCommand(intent,flags,startId);
    }

    public void setCallbacks(LocationTrackingCallback callbacks) {
        serviceCallbacks = callbacks;
        Log.i("location","set location callbacks");
    }
    /**
     * We can suppress  the missing permission warning because the service is only started when permissions are approved.
     */
    @SuppressLint("MissingPermission")
    private void startForeground() {
        Toast.makeText(this,"service started",Toast.LENGTH_LONG).show();
        Intent notificationIntent = new Intent(this, CustomerDashboard.class);
        notificationIntent.setAction("action");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        startForeground(1, new NotificationCompat.Builder(this,getString(R.string.channel_id))
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running in the background")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.googleg_disabled_color_18,"Cancel Pickup",pendingIntent)
                .build());

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    public void enteredShop() {
        if(serviceCallbacks != null) {
            serviceCallbacks.onEntersShop();
        }
    }

    public void exitShop() {
        if(serviceCallbacks != null) {
            serviceCallbacks.onExitShop();
        }
    }
}