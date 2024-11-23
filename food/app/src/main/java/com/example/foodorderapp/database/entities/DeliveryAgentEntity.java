package com.example.foodorderapp.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "delivery_agents")
public class DeliveryAgentEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String phone;
    private boolean isAvailable;

    public DeliveryAgentEntity(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isAvailable = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}