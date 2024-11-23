package com.example.foodorderapp.utils;

public class OrderStatus {
    // Initial state when order is created
    public static final String PENDING = "PENDING";

    // Order is confirmed by restaurant/admin
    public static final String ACCEPTED = "ACCEPTED";

    // Order is assigned to delivery person
    public static final String ASSIGNED = "ASSIGNED";

    // Order has been delivered to customer
    public static final String DELIVERED = "DELIVERED";

    // Order was cancelled
    public static final String CANCELLED = "CANCELLED";

    // Private constructor to prevent instantiation
    private OrderStatus() {}
}