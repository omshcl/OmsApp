package com.hcl.InstantPickup.models;

import com.google.gson.JsonArray;

public class GetOrders {
    public JsonArray orders;

    public GetOrders(JsonArray orders){
        this.orders = orders;
    }
}
