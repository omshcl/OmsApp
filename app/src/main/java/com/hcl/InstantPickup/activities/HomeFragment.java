package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
    private boolean is_expand = false;
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tl = v.findViewById(R.id.table_layout);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
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

        final Button expand=(Button) getView().findViewById(R.id.expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                if(!is_expand) {
                    TableLayout t1 = view.findViewById(R.id.table_layout);
                    t1.removeAllViews();
                    is_expand = true;
                    expand.setText("SHRINK");
                    getOrders(username);
                }else{
                    TableLayout t1 = view.findViewById(R.id.table_layout);
                    t1.removeAllViews();
                    is_expand = false;
                    expand.setText("EXPAND");
                    getOrders(username);



                }
            }

        });



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

    private void change_demand_type_customer_coming(JsonObject order){
        final JsonObject customerObject = new JsonObject();
        int id = order.get("id").getAsInt();

        customerObject.addProperty("id", id);
        Call<String> call = apiCalls.customercoming(customerObject);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response);
                if (!response.isSuccessful()) {

                    return;
                }

                String status = response.body();

                if (status.equals("success")) {


                } else {
                    Toast.makeText(getActivity(), "Failed to place order", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });
    }

    private void startLocationTracking(){
        startLocationService();
                        //sleep needed for request permission callback to be processed to launch location services
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CustomerDashboard.instance,"Please navigate to " + getString(R.string.store_name) + " and then return to the app",Toast.LENGTH_LONG).show();
                        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=Hcl America Frisco");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
;
    }

    private void createTextView(TableRow tr_head, Integer padding, String text){
        TextView t_v = new TextView(getActivity());
        t_v.setText(text);
        t_v.setTextColor(Color.WHITE);          // part2
        t_v.setPadding(padding, padding, padding, padding);
        tr_head.addView(t_v);// add the column to the table row here
    }

    private void createRowTextView(TableRow tr_head, TextView t_v, Integer padding, Integer rightpadding, String text){
        t_v.setText(text);
        t_v.setTextColor(Color.BLACK);          // part2
        t_v.setPadding(padding, padding, padding+rightpadding, padding);
        t_v.setTextSize(14);
        tr_head.addView(t_v);// add the column to the table row here
    }

    private Button createOnMyWayButton(JsonObject order1){
        final JsonObject order = order1;
        Button btn = new Button(getActivity());
        btn.setText(R.string.on_my_way);
        btn.setWidth(10);
        btn.setHeight(20);
        btn.setTextSize(10);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_demand_type_customer_coming(order);

                startLocationTracking();

            }
        });

        return btn;
    }

    private void generateOrderTable(JsonArray orders1){
        JsonArray orders = sortJsonArray(orders1);

        int padding = 5;

        TableRow tr_head = new TableRow(getActivity());
        tr_head.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        createTextView(tr_head, padding+7, getString(R.string.order_id));
        createTextView(tr_head, padding+15, getString(R.string.demand_type));
        createTextView(tr_head, padding, getString(R.string.total));
        createTextView(tr_head, padding+25, getString(R.string.date));
        createTextView(tr_head, padding+25, getString(R.string.action));

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        tl.addView(tr_head, params);

        TextView[] tv_order_id = new TextView[orders.size()];
        TextView[] tv_demand_type = new TextView[orders.size()];
        TextView[] tv_total = new TextView[orders.size()];
        TextView[] tv_date = new TextView[orders.size()];
        TableRow[] t_r = new TableRow[orders.size()];

        int rows;
        if (is_expand){
            rows = orders.size();
        }else{
            rows = 5;
        }

        for (int i = 0; i < rows; i++) {
            final JsonObject order = orders.get(i).getAsJsonObject();
            String demand_type = order.get("demand_type").getAsString();
            String total = order.get("total").getAsString();
            String date=order.get("date").getAsString();
            String order_id = order.get("id").toString();

            t_r[i] = new TableRow(getActivity());
            t_r[i].setBackgroundColor(Color.WHITE);
            t_r[i].setLayoutParams(params);

            tv_order_id[i] = new TextView(getActivity());
            tv_demand_type[i] = new TextView(getActivity());
            tv_total[i] = new TextView(getActivity());
            tv_date[i] = new TextView(getActivity());
            // Here create the TextView dynamically
            createRowTextView(t_r[i], tv_order_id[i], padding, 0, order_id);
            createRowTextView(t_r[i], tv_demand_type[i] , padding, 25, demand_type);
            createRowTextView(t_r[i], tv_total[i] , padding, 0, total);
            createRowTextView(t_r[i], tv_date[i] , padding, 25, date);

            if (demand_type.equals(R.string.ready_pickup)) {
                Button btn = createOnMyWayButton(order);
                t_r[i].addView(btn);
            }

            tl.addView(t_r[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }



    }


    public void startLocationService() {
        ((CustomerDashboard)getActivity()).requestPermissions();
    }

}
