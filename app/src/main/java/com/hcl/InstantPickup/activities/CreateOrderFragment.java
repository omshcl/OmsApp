package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.SingletonClass;
import com.hcl.InstantPickup.models.createOrder.CreateOrderStatus;
import com.hcl.InstantPickup.models.createOrder.Item;
import com.hcl.InstantPickup.models.createOrder.ItemListAdapter;
import com.hcl.InstantPickup.services.ApiCalls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateOrderFragment extends Fragment {
    private ApiCalls apiCalls;
    private Map<String, Item> itemMap;
    private Spinner spinner;
    private TextView qtyTextView;
    private TextView totalTextView;
    private int total;
    private RecyclerView recyclerView;
    private ItemListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        getItems(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ItemListAdapter();
        recyclerView.setAdapter(mAdapter);
        qtyTextView = (TextView) view.findViewById(R.id.quantity);
        totalTextView = (TextView) view.findViewById(R.id.total);
        total = 0;
        Button addItemButton = (Button) view.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
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

    private void getItems(final View view) {
        Call<JsonArray> call = apiCalls.getItems();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    Log.e("COF:getItems","Response Code"+response.code());
                    return;
                }
                // Request is successful
                itemMap = new HashMap<>();
                JsonArray items = response.body();
                for(int i=0;i<items.size();i++) {
                    JsonObject curItem = items.get(i).getAsJsonObject();
                    int id = curItem.get("itemid").getAsInt();
                    String shortDescription = curItem.get("shortdescription").getAsString();
                    String longDescription = curItem.get("itemdescription").getAsString();
                    int price = curItem.get("price").getAsInt();
                    System.out.println(id + shortDescription + longDescription + price);
                    itemMap.put(shortDescription, new Item(id, shortDescription, longDescription, price));
                }
                createSpinner(view);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }

    private void createSpinner(View view) {
        spinner = (Spinner) view.findViewById(R.id.itemSpinner);
        // Spinner click listener
        // Spinner Drop down elements
        List<String> items = new ArrayList<>();
        for(String key : itemMap.keySet()) {
            items.add(key);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void addItem(View view) {
        String itemName = spinner.getSelectedItem().toString();
        Item baseItem = itemMap.get(itemName);
        Item newItem = new Item(baseItem, Integer.valueOf(qtyTextView.getText().toString()));
        total += newItem.getPrice() * newItem.getQuantity();
        totalTextView.setText("Total: $"+total);
        mAdapter.addItem(newItem);
    }

    private JsonObject createOrderForm() {
        String s= SingletonClass.getInstance().getName();
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
        paramObject.addProperty("username", s);
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

