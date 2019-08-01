package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.createOrder.CreateOrderStatus;
import com.hcl.InstantPickup.services.ApiCalls;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateOrderFragment extends Fragment implements OnItemSelectedListener {
    ApiCalls apiCalls;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(view.getContext(), CustomerDashboard.class);
        super.onViewCreated(view, savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiCalls = retrofit.create(ApiCalls.class);

        Spinner spinner = (Spinner) view.findViewById(R.id.itemSpinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> items = getItems();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        view.findViewById(R.id.createOrderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                final JsonObject paramObject = createOrderForm();

                Call<CreateOrderStatus> call = apiCalls.createOrderPost(paramObject);


                call.enqueue(new Callback<CreateOrderStatus>() {
                    @Override
                    public void onResponse(Call<CreateOrderStatus> call, Response<CreateOrderStatus> response) {
                        System.out.println(response);
                        if (!response.isSuccessful()) {

                            return;
                        }

                        CreateOrderStatus status = response.body();

                        if (status.success) {

                            Toast.makeText(getActivity(),"Order Placed",Toast.LENGTH_LONG).show();
                            HomeFragment homefragment = new HomeFragment();
                            Bundle b = new Bundle();
                            b.putString("address", paramObject.get("address").getAsString());
                            b.putString("shipnode",paramObject.get("shipnode").getAsString());
                            b.putString("total",paramObject.get("total").getAsString());
                            homefragment.setArguments(b);

                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), homefragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();


                        } else
                            Toast.makeText(getActivity(),"Failed to place order",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<CreateOrderStatus> call, Throwable t) {

                    }

                });
            }
        });


    }

    private List<String> getItems() {
        Call<JsonArray> call = apiCalls.getItems();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    Log.e("COF:getItems","Response Code"+response.code());
                    return;
                }
                // Request is successful
                JsonArray itemList = response.body();
                for(int i=0;i<itemList.size();i++) {
                    JsonObject order = itemList.get(i).getAsJsonObject();
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
        return null;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

