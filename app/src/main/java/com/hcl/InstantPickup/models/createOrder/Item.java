package com.hcl.InstantPickup.models.createOrder;

//simple Item class for creating orders
public class Item {
    private int id;
    private String shortDescription;
    private String longDescription;
    private int price;
    private int quantity;

    //Constructor for when there is no quantity specified
    public Item(int id, String shortDescription, String longDescription, int price) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.quantity = 0;
    }

    //Constructor for when there is a quantity specified
    public Item(Item baseItem, int quantity) {
        this.id = baseItem.getID();
        this.shortDescription = baseItem.getShortDescription();
        this.longDescription = baseItem.getLongDescription();
        this.price = baseItem.getPrice();
        this.quantity = quantity;
    }

    //Getters and Setters
    public int getID() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getLongDescription() { return this.longDescription; }

    public int getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
