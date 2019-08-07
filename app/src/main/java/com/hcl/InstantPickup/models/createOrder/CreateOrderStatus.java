package com.hcl.InstantPickup.models.createOrder;

/** Used to handle return value after createOrder
 * API call
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class CreateOrderStatus {
    public boolean success;

    public CreateOrderStatus(Boolean success){
        this.success = success;
    }
}
