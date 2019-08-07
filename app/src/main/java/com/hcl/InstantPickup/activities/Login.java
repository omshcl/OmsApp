package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.login.LoginPost;
import com.hcl.InstantPickup.models.login.LoginStatus;
import com.hcl.InstantPickup.services.ApiCalls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Creates a Login Activity to enable
 * validation for user
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class Login extends AppCompatActivity {

    private ApiCalls apiCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);
    }

    /** Gets username and password
     * and calls a function for validation
     * @param view Cuurent view of activity
     */
    public void login(View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        loginPost(user, pass);
    }

    /** Makes API call to backend to check for
     * user credentials
     * @param user username
     * @param pass password
     */
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
                    EditText username = findViewById(R.id.username);
                    System.out.println(username.getText().toString());
                    Intent createOrderIntent = new Intent(getApplicationContext(), CustomerDashboard.class);
                    createOrderIntent.putExtra("username", username.getText().toString());
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
