package com.hcl.InstantPickup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcl.InstantPickup.R;

/**
 * Creates a fragment to display message
 * to customer when he/she reaches the store
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class ReadyForPickupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ready_for_pickup, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(view.getContext(), CustomerDashboard.class);
        super.onViewCreated(view, savedInstanceState);
        Button button = getView().findViewById(R.id.complete);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CustomerDashboard dashboard = CustomerDashboard.instance;
                if (dashboard != null) {
                    dashboard.switchFragment(FragmentAcitivityConstants.HomeFragmentId);
                }
            }
        });


    }
}
