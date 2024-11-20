package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.entities.OrderItemEntity;
import java.util.List;

@Dao
public interface OrderDAO {
    @Insert
    long insertOrder(OrderEntity order);

    @Insert
    void insertOrderItems(List<OrderItemEntity> orderItems);

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getAllOrders();

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    LiveData<List<OrderItemEntity>> getOrderItems(int orderId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    LiveData<OrderEntity> getOrder(int orderId);
}