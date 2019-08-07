package com.hcl.InstantPickup.models.createOrder;

/** Used to handle Items list
 * API call
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class Item {
    private int id;
    private String shortDescription;
    private String longDescription;
    private int price;
    private int quantity;

    public Item(int id, String shortDescription, String longDescription, int price) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.quantity = 0;
    }

    public Item(Item baseItem, int quantity) {
        this.id = baseItem.getID();
        this.shortDescription = baseItem.getShortDescription();
        this.longDescription = baseItem.getLongDescription();
        this.price = baseItem.getPrice();
        this.quantity = quantity;
    }

    public int getID() {
        return this.id;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

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
