package com.example.foodorderapp.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.OrderDAO;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.utils.OrderNotificationService;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDAO orderDAO;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final OrderNotificationService notificationService;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDAO = database.orderDAO();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        notificationService = new OrderNotificationService(application);
    }

    public void createOrder(OrderEntity order, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                long orderId = orderDAO.insert(order);
                mainHandler.post(() -> {
                    notificationService.showOrderStatusNotification(
                            "Order Created",
                            "Your order #" + orderId + " has been placed"
                    );
                    callback.onSuccess((int) orderId);
                });
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
                mainHandler.post(() -> {
                    String message = "Order #" + orderId + " is now " + status;
                    notificationService.showOrderStatusNotification("Order Update", message);
                    callback.onSuccess(orderId);
                });
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void markOrderAsPaid(int orderId, OrderCallback callback) {
        executorService.execute(() -> {
            try {
                orderDAO.updateOrderPaymentStatus(orderId, true);
                mainHandler.post(() -> {
                    notificationService.showOrderStatusNotification(
                            "Payment Confirmed",
                            "Payment received for order #" + orderId
                    );
                    callback.onSuccess(orderId);
                });
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