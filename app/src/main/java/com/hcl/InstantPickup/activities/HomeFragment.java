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

public class HomeFragment extends Fragment {
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
    }
}
