package com.example.foodorderapp.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.OrderDAO;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.entities.OrderItemEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private OrderDAO orderDAO;
    private LiveData<List<OrderEntity>> allOrders;
    private ExecutorService executorService;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDAO = database.orderDAO();
        allOrders = orderDAO.getAllOrders();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return allOrders;
    }

    public void insertOrder(OrderEntity order, List<OrderItemEntity> items) {
        executorService.execute(() -> {
            long orderId = orderDAO.insertOrder(order);
            // Update orderId in items
            for (OrderItemEntity item : items) {
                item.setOrderId((int) orderId);
            }
            orderDAO.insertOrderItems(items);
        });
    }

    public LiveData<OrderEntity> getOrder(int orderId) {
        return orderDAO.getOrder(orderId);
    }

    public LiveData<List<OrderItemEntity>> getOrderItems(int orderId) {
        return orderDAO.getOrderItems(orderId);
    }
}