package com.hcl.InstantPickup.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
//import com.hcl.InstantPickup.models.createOrder.CreateOrderStatus;
import com.hcl.InstantPickup.models.createOrder.CreateOrderStatus;
import com.hcl.InstantPickup.models.login.LoginPost;
import com.hcl.InstantPickup.models.login.LoginStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiCalls {

    @POST("customer/login")
    Call<LoginStatus> loginPost(@Body LoginPost loginPost);

    @POST("orders/new")
    Call<CreateOrderStatus> createOrderPost(@Body JsonObject createOrder);

    @POST("orders/customer_orders")
    Call<JsonArray> getOrders(@Body JsonObject username);

    @GET("items/list")
    Call<JsonArray> getItems();
}
