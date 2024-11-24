package com.example.foodorderapp.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.OrderDAO;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.utils.OrderNotificationService;
import android.content.Context;
import com.example.foodorderapp.utils.OrderStatus;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private static final String TAG = "OrderRepository";
    private final OrderDAO orderDAO;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final OrderNotificationService notificationService;  // Add this field
    private final Context context;  // Add this field

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDAO = database.orderDAO();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        context = application.getApplicationContext();  // Store context
        notificationService = new OrderNotificationService(context);  // Initialize notification service
    }

    public void createOrder(OrderEntity order, OrderCallback callback) {
        Log.d(TAG, "Creating new order");
        executorService.execute(() -> {
            try {
                long orderId = orderDAO.insert(order);
                Log.d(TAG, "Order created successfully with ID: " + orderId);
                mainHandler.post(() -> callback.onSuccess((int) orderId));
            } catch (Exception e) {
                Log.e(TAG, "Error creating order: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        Log.d(TAG, "Fetching all orders");
        return orderDAO.getAllOrders();
    }

    public LiveData<List<OrderEntity>> getOrdersByUser(int userId) {
        Log.d(TAG, "Fetching orders for user ID: " + userId);
        return orderDAO.getOrdersByUser(userId);
    }

    public void getOrderById(int orderId, OrderCallback callback) {
        Log.d(TAG, "Fetching order by ID: " + orderId);
        executorService.execute(() -> {
            try {
                OrderEntity order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    Log.d(TAG, "Order found: " + orderId);
                    mainHandler.post(() -> callback.onSuccess(order.getDeliveryAgentId()));
                } else {
                    Log.e(TAG, "Order not found: " + orderId);
                    mainHandler.post(() -> callback.onError("Order not found"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching order: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void updateOrderStatus(int orderId, String status, OrderCallback callback) {
        Log.d(TAG, "Updating order status: " + orderId + " to " + status);
        executorService.execute(() -> {
            try {
                orderDAO.updateOrderStatus(orderId, status);
                // Get user ID for the order to check if it's a customer order
                OrderEntity order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    // Send notification
                    mainHandler.post(() -> {
                        Log.d(TAG, "Sending notification for order: " + orderId);
                        notificationService.showOrderStatusNotification(orderId, status);
                    });
                }
                mainHandler.post(() -> callback.onSuccess(orderId));
            } catch (Exception e) {
                Log.e(TAG, "Error updating order status: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void assignDeliveryAgent(int orderId, int agentId, OrderCallback callback) {
        Log.d(TAG, "Assigning order " + orderId + " to agent " + agentId);
        executorService.execute(() -> {
            try {
                // Update order with agent ID and status
                orderDAO.assignDeliveryAgent(orderId, agentId);
                orderDAO.updateOrderStatus(orderId, OrderStatus.ASSIGNED);
                Log.d(TAG, "Successfully assigned order " + orderId + " to agent " + agentId);
                mainHandler.post(() -> callback.onSuccess(orderId));
            } catch (Exception e) {
                Log.e(TAG, "Error assigning order: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }



    public void markOrderAsPaid(int orderId, OrderCallback callback) {
        Log.d(TAG, "Marking order as paid: " + orderId);
        executorService.execute(() -> {
            try {
                orderDAO.updateOrderPaymentStatus(orderId, true);
                Log.d(TAG, "Order marked as paid successfully");
                mainHandler.post(() -> callback.onSuccess(orderId));
            } catch (Exception e) {
                Log.e(TAG, "Error marking order as paid: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public LiveData<List<OrderEntity>> getAgentActiveOrders(int agentId) {
        Log.d(TAG, "Getting active orders for agent: " + agentId);
        return orderDAO.getAgentActiveOrders(agentId);
    }

    public interface OrderCallback {
        void onSuccess(int orderId);
        void onError(String message);
    }


}