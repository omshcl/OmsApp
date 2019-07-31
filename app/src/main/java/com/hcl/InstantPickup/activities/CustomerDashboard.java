package com.hcl.InstantPickup.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.location.LocationService;
import com.hcl.InstantPickup.location.LocationTrackingCallback;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.GetOrders;
import com.hcl.InstantPickup.models.Username;
import com.hcl.InstantPickup.models.login.loginPost;
import com.hcl.InstantPickup.models.login.loginStatus;
import com.hcl.InstantPickup.services.apiCalls;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationTrackingCallback,FragmentAcitivityConstants{

    private com.hcl.InstantPickup.services.apiCalls apiCalls;
    private GoogleMap mMap;
    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_dashboard);
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
            currentFragment = savedInstanceState.getInt("activeFragment", HomeFragmentId);
            switchFragment(currentFragment);
        }
        navigationView.setNavigationItemSelectedListener(this);
        createNotificationChannel();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" http://6a9021c1.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(apiCalls.class);

        getOrders();
    }

    private void getOrders(){

        JsonObject username = new JsonObject();
        username.addProperty("username", "pat_abh");
        // Make POST request to /Login
        Call<JsonArray> call = apiCalls.getOrders(username);

        // Async callback and waits for response
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
                    System.out.println("Code: " + response.code());
                    return;
                }

                // Request is successful
                JsonArray orders = response.body();
                for(int i=0;i<orders.size();i++) {
                    JsonObject order = orders.get(i).getAsJsonObject();
                    String demand_type = order.get("demand_type").toString();
                    String total = order.get("total").toString();
                    String order_id = order.get("id").toString();


                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
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

    private void switchFragment(int fragmentID) {
        Fragment fragment = null;
        currentFragment = fragmentID;
        if (fragmentID == HomeFragmentId) {
            fragment = new HomeFragment();
        } else if (fragmentID == CreateOrderFragmentId) {
            fragment = new CreateOrderFragment();
        }
        else if (fragmentID == YourShopFragmentId){
            fragment = new YourStoreFragment();
        }

        if(fragment != null) {
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
        int fragmentId = HomeFragmentId;
        if (id == R.id.nav_home) {
            fragmentId = HomeFragmentId;
        } else if (id == R.id.nav_createorder) {
            fragmentId = CreateOrderFragmentId;
        }
        else if (id == R.id.nav_yourstore) {
            fragmentId = YourShopFragmentId;
        }
        switchFragment(fragmentId);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        private LocationService locationService;
        private boolean bound = false;


        @Override
        protected  void onStart() {
            super.onStart();
        }

        @Override
        protected void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putInt("activeFragment",currentFragment);
        }

        @Override
        protected void onStop() {
            super.onStop();
            if(bound) {
                locationService.setCallbacks(null);
                unbindService(locationServiceConnection);
                bound = false;
            }
        }

        private ServiceConnection locationServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                LocationService.LocalBinder binder = (LocationService.LocalBinder)service;
                locationService = binder.getService();
                bound = true;
                locationService.setCallbacks(CustomerDashboard.this);
                Log.e("location","callbacks set");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                bound = false;
            }
        };

        @SuppressLint("MissingPermission")
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            Intent newIntent = new Intent(this, LocationService.class);
            startService(newIntent);
            Intent intent = new Intent(this,LocationService.class);
            bindService(intent,locationServiceConnection, Context.BIND_AUTO_CREATE);
            Log.i("location","bound location services");
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


        @Override
        public void onEntersShop() {
            Toast.makeText(this,"Entered into location",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onExitShop() {

        }



}
