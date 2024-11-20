package com.example.foodorderapp.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
        foreignKeys = @ForeignKey(
                entity = OrderEntity.class,
                parentColumns = "id",
                childColumns = "orderId",
                onDelete = ForeignKey.CASCADE))
public class OrderItemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int orderId;
    private String itemName;
    private int quantity;
    private double pricePerItem;

    public OrderItemEntity(int orderId, String itemName, int quantity, double pricePerItem) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPricePerItem() { return pricePerItem; }
    public void setPricePerItem(double pricePerItem) { this.pricePerItem = pricePerItem; }
}