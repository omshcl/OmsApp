package com.hcl.InstantPickup.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//import com.hcl.InstantPickup.models.createOrder.createOrderStatus;
import com.hcl.InstantPickup.models.GetOrders;
import com.hcl.InstantPickup.models.Username;
import com.hcl.InstantPickup.models.createOrder.createOrderStatus;
import com.hcl.InstantPickup.models.login.loginPost;
import com.hcl.InstantPickup.models.login.loginStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface apiCalls {

    @POST("customer/login")
    Call<loginStatus> loginPost(@Body loginPost loginPost);

    @POST("orders/new")
    Call<createOrderStatus> createOrderPost(@Body JsonObject createOrder);

    @POST("orders/customer_orders")
    Call<JsonArray> getOrders(@Body JsonObject username);
}
