package com.hcl.InstantPickup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.login.createOrderStatus;
import com.hcl.InstantPickup.services.apiCalls;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateOrderActivity extends AppCompatActivity {




    private apiCalls apiCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_create_order);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(apiCalls.class);
        Button startLocation = (Button) findViewById(R.id.createOrderButton);
        startLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JsonObject paramObject = createOrderForm();

                Call<createOrderStatus> call = apiCalls.createOrderPost(paramObject);


                call.enqueue(new Callback<createOrderStatus>() {
                    @Override
                    public void onResponse(Call<createOrderStatus> call, Response<createOrderStatus> response) {
                        System.out.println(response);
                        if (!response.isSuccessful()) {

                            return;
                        }

                        createOrderStatus status = response.body();

                        if (status.success) {
                            Toast.makeText(getApplicationContext(),"Order Placed",Toast.LENGTH_LONG).show();


                        } else
                            Toast.makeText(getApplicationContext(),"Failed to place order",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<createOrderStatus> call, Throwable t) {

                    }

                });;
            }
        });


    }

    private JsonObject createOrderForm() {
        JsonObject paramObject = new JsonObject();
        JsonObject items = new JsonObject();
        JsonObject quantity = new JsonObject();
        JsonObject price = new JsonObject();
        JsonArray itemList = new JsonArray();
        items.addProperty("itemid", 1);
        items.addProperty("subtotal", 149);
        itemList.add(items);

        JsonArray quantityList = new JsonArray();
        quantity.addProperty("itemid", 1);
        quantity.addProperty("quantity", 2);
        quantityList.add(quantity);

        JsonArray priceList = new JsonArray();
        price.addProperty("itemid", 1);
        price.addProperty("price", 2);
        priceList.add(price);
        paramObject.addProperty("address", "123 Main St");
        paramObject.addProperty("channel", "asasas");
        paramObject.addProperty("city", "Frisco");
        paramObject.addProperty("username", "pat_abh");
        paramObject.addProperty("date", "2019-07-23T18:12:48.422Z");
        paramObject.addProperty("firstname", "Abhi");
        paramObject.add("items", itemList);
        paramObject.addProperty("lastname", "Patil");
        paramObject.addProperty("ordertype", "Austin");
        paramObject.addProperty("payment", "Credit");
        paramObject.add("quantity", quantityList);
        paramObject.add("price", priceList);
        paramObject.addProperty("shipnode", "Austin");
        paramObject.addProperty("state", "Texas");
        paramObject.addProperty("total", 200);
        paramObject.addProperty("zip", "75080");

        System.out.println(paramObject);

        return paramObject;
    }





}