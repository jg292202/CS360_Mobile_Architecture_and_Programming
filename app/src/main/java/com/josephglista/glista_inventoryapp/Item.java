package com.josephglista.glista_inventoryapp;

/*
    Represents a single item in the inventory.
    Contains name, SKU, category, and quantity.
 */
public class Item {
    public long id;
    public String name;
    public String sku;
    public String category;
    public int quantity;

    /*
        Constructs a new Item object.
     */
    public Item(long id, String name, String sku, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.category = category;
        this.quantity = quantity;
    }
}
