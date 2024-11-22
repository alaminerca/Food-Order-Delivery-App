package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.entities.OrderItemEntity;
import java.util.List;


@Dao
public interface OrderDAO {
    @Insert
    long insert(OrderEntity order);

    @Update
    void update(OrderEntity order);

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getAllOrders();

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getOrdersByUser(int userId);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateOrderStatus(int orderId, String status);

    @Query("UPDATE orders SET deliveryAgentId = :agentId, status = 'ASSIGNED' WHERE id = :orderId")
    void assignDeliveryAgent(int orderId, String agentId);
}