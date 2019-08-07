package com.hcl.InstantPickup.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** Used to handle orders GET request
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class GetOrders {
    public JsonObject orders;

    public GetOrders(JsonObject orders){
        this.orders = orders;
    }
}
