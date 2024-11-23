package com.example.foodorderapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private double totalAmount;
    private long orderDate;
    private String status;
    private boolean isPaid;
    private int deliveryAgentId;  // Consistently using int

    public OrderEntity(int userId, double totalAmount, String status) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = System.currentTimeMillis();
        this.status = status;
        this.isPaid = false;
        this.deliveryAgentId = 0;  // Default value when no agent is assigned
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public int getDeliveryAgentId() { return deliveryAgentId; }
    public void setDeliveryAgentId(int agentId) { this.deliveryAgentId = agentId; }
}