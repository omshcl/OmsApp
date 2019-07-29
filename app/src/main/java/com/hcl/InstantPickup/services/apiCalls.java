package com.hcl.omsapplication.services;

import com.google.gson.JsonObject;
import com.hcl.omsapplication.misc.Post;
import com.hcl.omsapplication.models.createOrder.createOrderStatus;
import com.hcl.omsapplication.models.login.loginPost;
import com.hcl.omsapplication.models.login.loginStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiCalls {

    @GET("posts")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @POST("login")
    Call<loginStatus> loginPost(@Body loginPost loginPost);

    @POST("orders/new")
    Call<createOrderStatus> createOrderPost(@Body JsonObject createOrder);
}
