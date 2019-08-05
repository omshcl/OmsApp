package com.hcl.InstantPickup.models.createOrder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private final TextView totalTV;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameTV;
        private final TextView itemPriceTV;
        private final TextView itemQuantityTV;
        private final ImageView subQuantityIV;
        private final ImageView addQuantityIV;
        private final TextView itemSubtotalTV;
        private final ImageView removeItemIV;


        public MyViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MVH", "Element " + getAdapterPosition() + " clicked.");
                }
            });
            itemNameTV = (TextView) v.findViewById(R.id.itemName);
            itemPriceTV = (TextView) v.findViewById(R.id.itemPrice);
            itemQuantityTV = (TextView) v.findViewById(R.id.itemQuantity);
            subQuantityIV = (ImageView) v.findViewById(R.id.subQuantity);
            addQuantityIV = (ImageView) v.findViewById(R.id.addQuantity);
            itemSubtotalTV = (TextView) v.findViewById(R.id.itemSubtotal);
            removeItemIV = (ImageView) v.findViewById(R.id.removeItem);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemListAdapter(TextView totalTV) {
        this.totalTV = totalTV;
        dataSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_create_order_item_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String shortDescription = dataSet.get(position).getShortDescription();
        int quantity = dataSet.get(position).getQuantity();
        final int price = dataSet.get(position).getPrice();
        int subtotal = quantity * price;
        holder.itemNameTV.setText(shortDescription);
        holder.itemPriceTV.setText("$"+String.valueOf(price));
        holder.itemQuantityTV.setText(String.valueOf(quantity));
        holder.subQuantityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curQty = dataSet.get(position).getQuantity();
                if(curQty > 1) {
                    dataSet.get(position).setQuantity(curQty - 1);
                    int total = Integer.valueOf(totalTV.getText().toString().replace("Total: $",""));
                    total -= price;
                    totalTV.setText("Total: $"+total);
                }
                notifyDataSetChanged();
            }
        });
        holder.addQuantityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curQty = dataSet.get(position).getQuantity();
                dataSet.get(position).setQuantity(curQty+1);
                notifyDataSetChanged();
                int total = Integer.valueOf(totalTV.getText().toString().replace("Total: $",""));
                total += price;
                totalTV.setText("Total: $"+total);
            }
        });
        holder.itemSubtotalTV.setText("$"+String.valueOf(subtotal));
        holder.removeItemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int subtotal = dataSet.get(position).getPrice() * dataSet.get(position).getQuantity();
                dataSet.remove(position);
                notifyDataSetChanged();
                int total = Integer.valueOf(totalTV.getText().toString().replace("Total: $",""));
                total -= subtotal;
                totalTV.setText("Total: $"+total);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItem(Item item) {
        for(Item existingItem : dataSet) {
            if(existingItem.getShortDescription().equals(item.getShortDescription())) {
                int curQuantity = existingItem.getQuantity();
                curQuantity += item.getQuantity();
                existingItem.setQuantity(curQuantity);
                notifyDataSetChanged();
                return;
            }
        }
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
