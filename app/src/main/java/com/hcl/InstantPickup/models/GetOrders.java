package com.hcl.InstantPickup.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetOrders {
    public JsonObject orders;

    public GetOrders(JsonObject orders){
        this.orders = orders;
    }
}
