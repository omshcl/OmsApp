package com.hcl.InstantPickup.models.createOrder;

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

//Adapter class to be used with the RecyclerView in CreateOrderFragment
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>{
    private List<Item> dataSet; //dataset for storing items in the order form
    private final TextView totalTV; //textview to update the total field in CreateOrderFragment

    //ViewHolder used to display each row in the order form
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemNameTV; //name of item
        private final TextView itemPriceTV; //price of item
        private final TextView itemQuantityTV; //quantity of item
        private final ImageView subQuantityIV; //button to subtract quantity
        private final ImageView addQuantityIV; //button to add quantity
        private final TextView itemSubtotalTV; //subtotal of item
        private final ImageView removeItemIV; //button to remove item from order form

        public MyViewHolder(View v) {
            super(v);
            itemNameTV = (TextView) v.findViewById(R.id.itemName);
            itemPriceTV = (TextView) v.findViewById(R.id.itemPrice);
            itemQuantityTV = (TextView) v.findViewById(R.id.itemQuantity);
            subQuantityIV = (ImageView) v.findViewById(R.id.subQuantity);
            addQuantityIV = (ImageView) v.findViewById(R.id.addQuantity);
            itemSubtotalTV = (TextView) v.findViewById(R.id.itemSubtotal);
            removeItemIV = (ImageView) v.findViewById(R.id.removeItem);
        }
    }

    //Constructor to pass in total textview and initialize dataset
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
        //retrieve values form dataSet
        String shortDescription = dataSet.get(position).getShortDescription();
        int quantity = dataSet.get(position).getQuantity();
        final int price = dataSet.get(position).getPrice();
        int subtotal = quantity * price;
        //set text fields in viewholder to retreived values
        holder.itemNameTV.setText(shortDescription);
        holder.itemPriceTV.setText("$"+String.valueOf(price));
        holder.itemQuantityTV.setText(String.valueOf(quantity));
        //setOnClickListener for subtracting quantity
        holder.subQuantityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decrement quantity and update total
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
        //setOnClickListener for adding quantity
        holder.addQuantityIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increment quantity and update total
                int curQty = dataSet.get(position).getQuantity();
                dataSet.get(position).setQuantity(curQty+1);
                notifyDataSetChanged();
                int total = Integer.valueOf(totalTV.getText().toString().replace("Total: $",""));
                total += price;
                totalTV.setText("Total: $"+total);
            }
        });
        holder.itemSubtotalTV.setText("$"+String.valueOf(subtotal));
        //setOnclickListener for removing item from order form
        holder.removeItemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove item from dataset and update total
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

    //add item to order form
    public void addItem(Item item) {
        //if the item already exists on the order form, just update the quantity
        for(Item existingItem : dataSet) {
            if(existingItem.getShortDescription().equals(item.getShortDescription())) {
                int curQuantity = existingItem.getQuantity();
                curQuantity += item.getQuantity();
                existingItem.setQuantity(curQuantity);
                notifyDataSetChanged();
                return;
            }
        }
        //else add the item as a new entry to the order form
        dataSet.add(item);
        notifyDataSetChanged();
    }

    //create JsonArray of itemid:price for creating order JsonObject
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

    //create JsonArray of itemid:quantity for creating order JsonObject
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
