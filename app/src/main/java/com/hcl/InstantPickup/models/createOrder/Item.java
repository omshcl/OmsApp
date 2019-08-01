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

    public int getID() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getLongDescription() {
        return this.getLongDescription();
    }

    public int getPrice() {
        return this.price;
    }
}
