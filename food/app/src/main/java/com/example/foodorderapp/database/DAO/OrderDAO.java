package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.OrderEntity;
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

    @Query("SELECT * FROM orders WHERE id = :orderId")
    OrderEntity getOrderById(int orderId);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateOrderStatus(int orderId, String status);

    @Query("UPDATE orders SET isPaid = :isPaid WHERE id = :orderId")
    void updateOrderPaymentStatus(int orderId, boolean isPaid);

    @Query("UPDATE orders SET deliveryAgentId = :agentId WHERE id = :orderId")
    void assignDeliveryAgent(int orderId, int agentId);

    @Query("SELECT * FROM orders WHERE deliveryAgentId = :agentId AND status != 'DELIVERED' ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getAgentActiveOrders(int agentId);

    @Query("SELECT * FROM orders WHERE deliveryAgentId = :agentId AND status = 'DELIVERED' ORDER BY orderDate DESC")
    LiveData<List<OrderEntity>> getAgentCompletedOrders(int agentId);
}