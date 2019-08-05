package com.hcl.InstantPickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.location.LocationService;
import com.hcl.InstantPickup.location.LocationTrackingCallback;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.SingletonClass;
import com.hcl.InstantPickup.services.ApiCalls;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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


public class CustomerDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private GoogleMap map;
    private ApiCalls apiCalls;
    private int currentFragment;
    public static CustomerDashboard instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        Intent i = getIntent();
        String username = i.getStringExtra("Username");
        System.out.println(username);
        SingletonClass.getInstance().setName(username);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);
        setVariables(username); //set SingletonClass variables
        updateFBApiKey(username); //update Firebase API Key with backend

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt("activeFragment", FragmentAcitivityConstants.HomeFragmentId);
            switchFragment(currentFragment);
        }
        navigationView.setNavigationItemSelectedListener(this);
        createNotificationChannel();
        instance = this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchFragment(int fragmentID) {
        Fragment fragment = null;
        currentFragment = fragmentID;
        if (fragmentID == FragmentAcitivityConstants.HomeFragmentId) {
            fragment = new HomeFragment();
        } else if (fragmentID == FragmentAcitivityConstants.CreateOrderFragmentId) {
            fragment = new CreateOrderFragment();
        } else if (fragmentID == FragmentAcitivityConstants.YourShopFragmentId){
            fragment = new YourStoreFragment();
        } else if (fragmentID == FragmentAcitivityConstants.ReadyForPickupFragment) {
            fragment  = new ReadyForPickupFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screen_area, fragment);
            fragmentTransaction.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        int fragmentId = FragmentAcitivityConstants.HomeFragmentId;
        if (id == R.id.nav_home) {
            fragmentId = FragmentAcitivityConstants.HomeFragmentId;
        } else if (id == R.id.nav_createorder) {
            fragmentId = FragmentAcitivityConstants.CreateOrderFragmentId;
        }
        else if (id == R.id.nav_yourstore) {
            fragmentId = FragmentAcitivityConstants.YourShopFragmentId;
        }
        switchFragment(fragmentId);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("activeFragment", currentFragment);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public void setVariables(String username) {
        JsonObject usernameObject = new JsonObject();
        usernameObject.addProperty("username", username);
        Call<JsonObject> call = apiCalls.getCustomerInfo(usernameObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.e("CD:setVariables()","Response Code"+response.code());
                    return;
                }
                // Request is successful
                JsonObject userInfo = response.body();
                String firstname = userInfo.get("firstname").getAsString();
                SingletonClass.getInstance().setFirstName(firstname);
                String lastname = userInfo.get("lastname").getAsString();
                SingletonClass.getInstance().setLastName(lastname);
                String address = userInfo.get("address").getAsString();
                SingletonClass.getInstance().setAddress(address);
                String city = userInfo.get("city").getAsString();
                SingletonClass.getInstance().setCity(city);
                String state = userInfo.get("state").getAsString();
                SingletonClass.getInstance().setState(state);
                String zip = userInfo.get("zip").getAsString();
                SingletonClass.getInstance().setZip(zip);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateFBApiKey(String username) {
        final String un = username;
        final String[] apiKey = new String[1];
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        apiKey[0] = task.getResult().getToken();
                        Log.e("API Key", apiKey[0]);
                        JsonObject usernameObject = new JsonObject();
                        usernameObject.addProperty("username", un);
                        usernameObject.addProperty("fbapikey", apiKey[0]);
                        //System.out.println(usernameObject.toString());
                        Call<JsonObject> call = apiCalls.updateFBApiKey(usernameObject);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (!response.isSuccessful()) {
                                    Log.e("CD:updateFBApiKey()","Response Code"+response.code());
                                    return;
                                }
                                // Request is successful
                                System.out.println(response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                                System.out.println(t.getMessage());
                            }
                        });
                    }
                });

    }

    public void onPickedup() {
        Log.i("customer","onpicked up called");
        Intent notificationIntent = new Intent(this, CustomerDashboard.class);
        notificationIntent.setAction("action");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Welcome to " + getString(R.string.store_name)+ "  an attendant will be with you shortly")
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.channel_id,builder.build());
        Intent stopServiceIntent = new Intent(this, LocationService.class);
        stopService(stopServiceIntent);
        switchFragment(FragmentAcitivityConstants.ReadyForPickupFragment);
    }


    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Intent newIntent = new Intent(this, LocationService.class);
        startService(newIntent);
    }
}
