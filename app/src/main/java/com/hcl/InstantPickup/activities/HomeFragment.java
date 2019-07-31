package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hcl.InstantPickup.R;
import com.hcl.InstantPickup.models.GetOrders;
import com.hcl.InstantPickup.models.Username;
import com.hcl.InstantPickup.services.apiCalls;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private com.hcl.InstantPickup.services.apiCalls apiCalls;
    
    TextView address;
    TextView shipnode;
    TextView total;
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        address = (TextView) v.findViewById(R.id.address);
        shipnode=(TextView)v.findViewById(R.id.shipnode);
        total=(TextView)v.findViewById(R.id.total);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setVisibility(View.GONE);
        final Bundle bundle=this.getArguments();
        if(bundle!=null) {
            view.setVisibility(View.VISIBLE);


            view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s=bundle.get("address").toString();
                    String s1=bundle.get("shipnode").toString();
                    String s2=bundle.get("total").toString();
                    address.setText(s);
                    shipnode.setText(s1);
                    total.setText(s2);


                }
            });
        }

        //WRITE ACTIVITY CODE HERE
        // ex. view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() ....
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" http://6a9021c1.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(com.hcl.InstantPickup.services.apiCalls.class);

//        getOrders();
    }

    private void getOrders(){

        final Username username = new Username("pat_abh");

        // Make POST request to /Login
        Call<GetOrders> call = apiCalls.getOrders(username);

        // Async callback and waits for response
        call.enqueue(new Callback<GetOrders>() {
            @Override
            public void onResponse(Call<GetOrders> call, Response<GetOrders> response) {

                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
                    System.out.println("Code: " + response.code());
                    return;
                }

                // Request is successful
                GetOrders orders = response.body();

                System.out.println(orders.orders);

            }

            @Override
            public void onFailure(Call<GetOrders> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }
}
