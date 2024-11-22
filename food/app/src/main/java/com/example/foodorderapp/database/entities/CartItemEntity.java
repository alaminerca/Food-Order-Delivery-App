package com.example.foodorderapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int menuItemId;
    private int quantity;
    private double pricePerItem;
    private String itemName;

    public CartItemEntity(int menuItemId, int quantity, double pricePerItem, String itemName) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.itemName = itemName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPricePerItem() { return pricePerItem; }
    public void setPricePerItem(double pricePerItem) { this.pricePerItem = pricePerItem; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
}