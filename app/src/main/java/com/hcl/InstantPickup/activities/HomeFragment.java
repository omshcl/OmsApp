package com.hcl.InstantPickup.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.SingletonClass;
import com.hcl.InstantPickup.services.ApiCalls;

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

        tl=(TableLayout)v.findViewById(R.id.table_layout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String username=SingletonClass.getInstance().getName();
        TextView textView=(TextView)view.findViewById(R.id.textHomeWelcome);

        textView.setText("Welcome"+" "+username);
        view.setVisibility(View.GONE);
        final Bundle bundle=this.getArguments();
        if(bundle!=null) {
            view.setVisibility(View.VISIBLE);


            view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getString(R.string.backend_url))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    apiCalls = retrofit.create(ApiCalls.class);

                    getOrders(username);
                }
            });
        }

        //WRITE ACTIVITY CODE HERE
        // ex. view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() ....

    }

    private void getOrders(String name){

        final JsonObject username = new JsonObject();
        username.addProperty("username", name);

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
                TableRow tr_head=new TableRow(getActivity());
                tr_head.setId(0);
                tr_head.setBackgroundColor(Color.BLUE);
                tr_head.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                TextView label_hello = new TextView(getActivity());
                label_hello.setId(0);
                label_hello.setText("ORDER ID");
                label_hello.setTextColor(Color.WHITE);          // part2
                label_hello.setPadding(70, 70, 70, 70);
                tr_head.addView(label_hello);// add the column to the table row here
                int j=1;
                TextView label_android = new TextView(getActivity());    // part3
                label_android.setId(j);// define id that must be unique
                label_android.setText("DEMAND TYPE"); // set the text for the header
                label_android.setTextColor(Color.WHITE); // set the color
                label_android.setPadding(70, 70, 70, 70); // set the padding (if required)
                tr_head.addView(label_android);


                TextView label_d = new TextView(getActivity());    // part3
                label_d.setId(j+1);// define id that must be unique
                label_d.setText("TOTAL"); // set the text for the header
                label_d.setTextColor(Color.WHITE); // set the color
                label_d.setPadding(70, 70, 70, 70); // set the padding (if required)
                tr_head.addView(label_d);

                tl.addView(tr_head, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                        TableLayout.LayoutParams.WRAP_CONTENT));


                TextView[] textArray = new TextView[orders.size()];
                TextView[] textArray1 = new TextView[orders.size()];
                TextView[] textArray2 = new TextView[orders.size()];
                TableRow[] tr_head1 = new TableRow[orders.size()];

                for(int i=0;i<orders.size();i++) {
                    JsonObject order = orders.get(i).getAsJsonObject();
                    String demand_type = order.get("demand_type").toString();
                    String total = order.get("total").toString();
                    String order_id = order.get("id").toString();
                    tr_head1[i] = new TableRow(getActivity());
                    tr_head1[i].setId(i+1);
                    tr_head1[i].setBackgroundColor(Color.GRAY);
                    tr_head1[i].setLayoutParams(new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    // Here create the TextView dynamically

                    textArray[i] = new TextView(getActivity());
                    textArray[i].setId(i+111);
                    textArray[i].setText(order_id);
                    textArray[i].setTextColor(Color.WHITE);
                    textArray[i].setPadding(70, 70, 70, 70);
                    tr_head1[i].addView(textArray[i]);


                    textArray1[i] = new TextView(getActivity());
                    textArray1[i].setId(i+111);
                    textArray1[i].setText(demand_type);
                    textArray1[i].setTextColor(Color.WHITE);
                    textArray1[i].setPadding(70, 70, 70, 70);
                    tr_head1[i].addView(textArray1[i]);


                    textArray2[i] = new TextView(getActivity());
                    textArray2[i].setId(i+111);
                    textArray2[i].setText(total);
                    textArray2[i].setTextColor(Color.WHITE);
                    textArray2[i].setPadding(70, 70, 70, 70);
                    tr_head1[i].addView(textArray2[i]);

                    tl.addView(tr_head1[i], new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));





                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }
}
