package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.login.LoginPost;
import com.hcl.InstantPickup.models.login.LoginStatus;
import com.hcl.InstantPickup.services.ApiCalls;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private ApiCalls apiCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = task.getResult().getToken();
                        Log.e("API Key", token);
                    }
                });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        loginPost(user, pass);
    }

    private void loginPost(final String user, String pass) {

        // Create an instance of model class LoginPost
        final LoginPost login = new LoginPost(user, pass);

        // Make POST request to /Login
        Call<LoginStatus> call = apiCalls.loginPost(login);

        // Async callback and waits for response
        call.enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {

                if (!response.isSuccessful()) {
                    Toast toast = Toast.makeText(Login.this, "Response Error", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }

                // Request is successful
                LoginStatus status = response.body();

                // Go to createOrder page if valid
                if (status.isValid) {
                    EditText username = (EditText) findViewById(R.id.username);
                    System.out.println(username.getText().toString());
                    Intent createOrderIntent = new Intent(getApplicationContext(), CustomerDashboard.class);
                    createOrderIntent.putExtra("Username", username.getText().toString());
                    startActivity(createOrderIntent);
                    Toast toast = Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<LoginStatus> call, Throwable t) {
                Toast toast = Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });
    }
}
