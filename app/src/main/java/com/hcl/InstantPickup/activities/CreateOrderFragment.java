package com.hcl.InstantPickup.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.services.ApiCalls;
import com.hcl.InstantPickup.models.SingletonClass;
import com.hcl.InstantPickup.models.createOrder.CreateOrderStatus;
import com.hcl.InstantPickup.models.createOrder.Item;
import com.hcl.InstantPickup.models.createOrder.ItemListAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/** Creates Create Dashboard of the application
 * Customer can create new orders here
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class CreateOrderFragment extends Fragment {
    private ApiCalls apiCalls; // instance of ApiCalls class to make REST GET/POST requests
    private Map<String, Item> itemMap; // map of available items queried from the backend
    private Spinner spinner; // spinner element used to select item to add to cart
    private TextView qtyTextView; // textview for user to input quantity
    private TextView totalTextView; // textview that maintains a running total for the order
    private RecyclerView recyclerView; // recyclerview that displays the current items in our cart
    private ItemListAdapter mAdapter; // adapter used for storing data to populate the recyclerview
    private RecyclerView.LayoutManager layoutManager; // layoutmanager for the recyclerviewer

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_order, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create()).build();

        apiCalls = retrofit.create(ApiCalls.class);
        // retrieve list of items from backend
        getItems(view);
        // initialize recyclerview
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // initialize textviews
        qtyTextView = (TextView) view.findViewById(R.id.quantity);
        totalTextView = (TextView) view.findViewById(R.id.total);
        mAdapter = new ItemListAdapter(totalTextView);
        // set an adapter for our recylerview
        recyclerView.setAdapter(mAdapter);
        // initialize add item button and set onclicklistener
        Button addItemButton = (Button) view.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
        // set onclicklistener for create order button
        view.findViewById(R.id.createOrderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // if cart is empty, do not post order and notify user
                if (mAdapter.getItemCount() == 0) {
                    Toast toast = Toast.makeText(getActivity(), "Please add items to your cart", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                // else create orderform object and post order
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
                            Toast toast = Toast.makeText(getActivity(), "Order Placed", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            CustomerDashboard dashboard = CustomerDashboard.instance;
                            if (dashboard != null) {
                                dashboard.switchFragment(FragmentAcitivityConstants.HomeFragmentId);
                            }
                        } else
                            Toast.makeText(getActivity(), "Failed to place order", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<CreateOrderStatus> call, Throwable t) {

                    }

                });
            }
        });
    }

    /** Gets Items available list from backend
     * @param view Current Fragement view
     */
    // get list of items from backend to create spinner
    private void getItems(final View view) {
        Call<JsonArray> call = apiCalls.getItems();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    Log.e("COF:getItems", "Response Code" + response.code());
                    return;
                }
                // Request is successful
                itemMap = new HashMap<>();
                JsonArray items = response.body();
                for (int i = 0; i < items.size(); i++) {
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
                System.out.println(t.getMessage());
            }
        });
    }

    /** Displays the list received from backend
     * in a dropdown menu
     * @param view Current Fragement view
     */
    // create spinner element to select items
    private void createSpinner(View view) {
        spinner = (Spinner) view.findViewById(R.id.itemSpinner);
        // Spinner Drop down elements
        List<String> items = new ArrayList<>();
        for (String key : itemMap.keySet()) {
            items.add(key);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, items);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    /** Creates option to increment or decrement
     * item quantity
     * @param view Current Fragement view
     */
    // add item to the shopping cart
    public void addItem(View view) {
        // make sure quantity field is not empty
        if (!qtyTextView.getText().toString().isEmpty()) {
            int quantity = Integer.valueOf(qtyTextView.getText().toString());
            String itemName = spinner.getSelectedItem().toString();
            Item baseItem = itemMap.get(itemName);
            Item newItem = new Item(baseItem, quantity);
            int total = Integer.valueOf(totalTextView.getText().toString().replace("Total: $", ""));
            total += newItem.getPrice() * newItem.getQuantity();
            // update displayed total
            totalTextView.setText("Total: $" + total);
            // add the item to our ItemListAdapter
            mAdapter.addItem(newItem);
        }
    }

    /** Generates hardcoded orderform to be sent
     * to backend to store a new order
     * @param
     */
    // create a JsonObject to send in our post request to create a new order
    private JsonObject createOrderForm() {
        JsonObject orderFormObject = new JsonObject();
        JsonArray quantityList = mAdapter.getQuantities();
        JsonArray priceList = mAdapter.getPrices();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String curDate = simpleDateFormat.format(new Date());
        int total = Integer.valueOf(totalTextView.getText().toString().replace("Total: $", ""));
        // retrieve customer info that has previously been saved to the SingletonClass
        orderFormObject.addProperty("username", SingletonClass.getInstance().getName());
        orderFormObject.addProperty("firstname", SingletonClass.getInstance().getFirstName());
        orderFormObject.addProperty("lastname", SingletonClass.getInstance().getLastName());
        orderFormObject.addProperty("address", SingletonClass.getInstance().getAddress());
        orderFormObject.addProperty("city", SingletonClass.getInstance().getCity());
        orderFormObject.addProperty("state", SingletonClass.getInstance().getState());
        orderFormObject.addProperty("zip", SingletonClass.getInstance().getZip());
        orderFormObject.addProperty("date", curDate);
        orderFormObject.addProperty("channel", "Online");
        orderFormObject.addProperty("ordertype", "Pickup");
        orderFormObject.addProperty("shipnode", "Frisco");
        orderFormObject.addProperty("payment", "Credit");
        orderFormObject.add("quantity", quantityList);
        orderFormObject.add("price", priceList);
        orderFormObject.addProperty("total", total);
        System.out.println(orderFormObject);
        return orderFormObject;
    }
}
