package com.hcl.InstantPickup.models.createOrder;

public class Item {
    private int id;
    private String shortDescription;
    private String longDescription;
    private int price;

    public Item(int id, String shortDescription, String longDescription, int price) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
    }
}
