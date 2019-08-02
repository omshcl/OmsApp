package com.hcl.InstantPickup.models.createOrder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.InstantPickup.R;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>{
    private List<Item> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public MyViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MVH", "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemListAdapter() {
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        FrameLayout v = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String shortDescription = dataSet.get(position).getShortDescription();
        int quantity = dataSet.get(position).getQuantity();
        int price = dataSet.get(position).getPrice();
        int subtotal = quantity * price;
        holder.textView.setText(shortDescription+"     "+quantity+"     "+price+"     "+subtotal);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItem(Item item) {
        dataSet.add(item);
        notifyDataSetChanged();
    }

    public JsonArray getPrices() {
        JsonArray priceList = new JsonArray();
        for(Item curItem: dataSet) {
            JsonObject newItem = new JsonObject();
            newItem.addProperty("itemid", curItem.getID());
            newItem.addProperty("price", curItem.getPrice());
            priceList.add(newItem);
        }
        return priceList;
    }

    public JsonArray getQuantities() {
        JsonArray quantityList = new JsonArray();
        for(Item curItem: dataSet) {
            JsonObject newItem = new JsonObject();
            newItem.addProperty("itemid", curItem.getID());
            newItem.addProperty("quantity", curItem.getQuantity());
            quantityList.add(newItem);
        }
        return quantityList;
    }
}
