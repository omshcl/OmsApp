package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.login.loginPost;
import com.hcl.InstantPickup.models.login.loginStatus;
import com.hcl.InstantPickup.services.apiCalls;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private com.hcl.InstantPickup.services.apiCalls apiCalls;
    private TextView textViewResult;

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
        textViewResult = findViewById(R.id.text_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" http://13bf4aaf.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(apiCalls.class);
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        loginPost(user, pass);
    }

    private void loginPost(String user, String pass) {

        // Create an instance of model class loginPost
        final loginPost login = new loginPost(user, pass);

        // Make POST request to /Login
        Call<loginStatus> call = apiCalls.loginPost(login);

        // Async callback and waits for response
        call.enqueue(new Callback<loginStatus>() {
            @Override
            public void onResponse(Call<loginStatus> call, Response<loginStatus> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                // Request is successful
                loginStatus status = response.body();

                // Go to createOrder page if valid
                if (status.isValid) {
//                    Intent createOrderIntent = new Intent(getApplicationContext(), createOrder.class);
//                    textViewResult.setText("Success");
//                    createOrderIntent.putExtra("Username", "user");
//                    startActivity(createOrderIntent);
                } else
                    textViewResult.setText("Failure");

            }

            @Override
            public void onFailure(Call<loginStatus> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
