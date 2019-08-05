package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.SingletonClass;
import com.hcl.InstantPickup.services.ApiCalls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ApiCalls apiCalls;
    TableLayout tl;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tl = v.findViewById(R.id.table_layout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String username = SingletonClass.getInstance().getName();
        TextView textView = view.findViewById(R.id.textHomeWelcome);

        textView.setText("Welcome" + " " + username);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.backend_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);

        getOrders(username);

    }

    private void getOrders(String name) {

        final JsonObject username = new JsonObject();
        username.addProperty("username", name);

        // Make POST request to /Login
        Call<JsonArray> call = apiCalls.getOrders(username);

        // Async callback and waits for response
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                JsonArray orders = response.body();

                generateOrderTable(orders);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }

    private JsonArray sortJsonArray(JsonArray orders){
        JsonArray sortedJsonArray = new JsonArray();
        List<JsonObject> jsonList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            jsonList.add(orders.get(i).getAsJsonObject());
        }

        Collections.sort( jsonList, new Comparator<JsonObject>() {

            public int compare(JsonObject a, JsonObject b) {
                String valA = new String();
                String valB = new String();

                valA =  a.get("date").getAsString();
                valB =  b.get("date").getAsString();

                return valA.compareTo(valB);
            }
        });

        for (int i = orders.size() -1; i >= 0; i--) {
            sortedJsonArray.add(jsonList.get(i));
            System.out.println(jsonList.get(i));
        }

        return sortedJsonArray;
    }

    private void generateOrderTable(JsonArray orders1){

        // Sort JsonArray by date of orders
        JsonArray orders = sortJsonArray(orders1);

        // Request is successful
        int padding = 20;
        int id = 1;
        TableRow tr_head = new TableRow(getActivity());
        tr_head.setId(id);
        tr_head.setBackgroundColor(Color.BLUE);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView t_order_id = new TextView(getActivity());
        t_order_id.setId(id+1);
        t_order_id.setText(getString(R.string.order_id));
        t_order_id.setTextColor(Color.WHITE);          // part2
        t_order_id.setPadding(padding, padding, padding, padding);
        tr_head.addView(t_order_id);// add the column to the table row here

        TextView t_demand_type = new TextView(getActivity());    // part3
        t_demand_type.setId(id + 2);// define id that must be unique
        t_demand_type.setText(getString(R.string.demand_type)); // set the text for the header
        t_demand_type.setTextColor(Color.WHITE); // set the color
        t_demand_type.setPadding(padding, padding, padding, padding); // set the padding (if required)
        tr_head.addView(t_demand_type);

        TextView t_total = new TextView(getActivity());    // part3
        t_total.setId(id + 3);// define id that must be unique
        t_total.setText(getString(R.string.total)); // set the text for the header
        t_total.setTextColor(Color.WHITE); // set the color
        t_total.setPadding(padding, padding, padding, padding); // set the padding (if required)
        tr_head.addView(t_total);

        TextView t_action = new TextView(getActivity());    // part3
        t_action.setId(id + 4);// define id that must be unique
        t_action.setText(getString(R.string.action)); // set the text for the header
        t_action.setTextColor(Color.WHITE); // set the color
        t_action.setPadding(padding, padding, padding, padding); // set the padding (if required)
        tr_head.addView(t_action);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                TableLayout.LayoutParams.WRAP_CONTENT));


        TextView[] colOrderId = new TextView[orders.size()];
        TextView[] colDemandType = new TextView[orders.size()];
        TextView[] colTotal = new TextView[orders.size()];

        TableRow[] order_row = new TableRow[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            JsonObject order = orders.get(i).getAsJsonObject();
            String demand_type = order.get("demand_type").getAsString();
            String total = order.get("total").toString();
            String order_id = order.get("id").toString();
            order_row[i] = new TableRow(getActivity());
            order_row[i].setId(i + 5);
            order_row[i].setBackgroundColor(Color.GRAY);
            order_row[i].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Here create the TextView dynamically

            colOrderId[i] = new TextView(getActivity());
            colOrderId[i].setId(i + 6);
            colOrderId[i].setText(order_id);
            colOrderId[i].setTextColor(Color.WHITE);
            colOrderId[i].setPadding(padding, padding, padding, padding);
            order_row[i].addView(colOrderId[i]);


            colDemandType[i] = new TextView(getActivity());
            colDemandType[i].setId(i + 7);
            colDemandType[i].setText(demand_type);
            colDemandType[i].setTextColor(Color.WHITE);
            colDemandType[i].setPadding(padding, padding, padding, padding);
            order_row[i].addView(colDemandType[i]);


            colTotal[i] = new TextView(getActivity());
            colTotal[i].setId(i + 8);
            colTotal[i].setText(total);
            colTotal[i].setTextColor(Color.WHITE);
            colTotal[i].setPadding(padding, padding, padding, padding);
            order_row[i].addView(colTotal[i]);

            System.out.println(demand_type);
            if (demand_type.equals(getString(R.string.ready_pickup))) {
                Button btn = new Button(getActivity());
                btn.setText(getString(R.string.on_my_way));
                btn.setId(id + 4);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        YourStoreFragment yourStoreFragment = new YourStoreFragment();
                        startLocationService();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), yourStoreFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                });
                order_row[i].addView(btn);
            }


            tl.addView(order_row[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }


    }

    public void startLocationService() {
        ((CustomerDashboard)getActivity()).requestPermissions();
    }
}
