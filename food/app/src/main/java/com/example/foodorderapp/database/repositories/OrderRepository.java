package com.example.foodorderapp.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.OrderDAO;
import com.example.foodorderapp.database.entities.OrderEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDAO orderDAO;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDAO = database.orderDAO();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void createOrder(OrderEntity order, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                long orderId = orderDAO.insert(order);
                mainHandler.post(() -> callback.onSuccess((int) orderId));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public LiveData<List<OrderEntity>> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    public void updateOrderStatus(int orderId, String status, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                orderDAO.updateOrderStatus(orderId, status);
                mainHandler.post(() -> callback.onSuccess(orderId));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void assignDeliveryAgent(int orderId, String agentId, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                orderDAO.assignDeliveryAgent(orderId, agentId);
                mainHandler.post(() -> callback.onSuccess(orderId));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public interface OrderCallback {
        void onSuccess(int orderId);
        void onError(String message);
    }
}