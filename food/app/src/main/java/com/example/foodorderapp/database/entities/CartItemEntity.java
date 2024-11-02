package com.example.foodorderapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "cart_items",
        foreignKeys = @ForeignKey(
                entity = MenuItemEntity.class,
                parentColumns = "id",
                childColumns = "menuItemId",
                onDelete = ForeignKey.CASCADE
        ))
public class CartItemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int menuItemId;
    private int quantity;
    private double pricePerItem;
    private String itemName; // Store name for quick access

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

    // Helper method to calculate total price
    public double getTotalPrice() {
        return quantity * pricePerItem;
    }
}