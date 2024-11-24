package com.example.foodorderapp.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.repositories.OrderRepository;
import com.example.foodorderapp.utils.OrderStatus;

import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveryViewModel";
    private OrderRepository orderRepository;
    private DeliveryAgentRepository deliveryAgentRepository;
    private SharedPreferences prefs;

    public DeliveryViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        deliveryAgentRepository = new DeliveryAgentRepository(application);
        prefs = application.getSharedPreferences("delivery_prefs", Application.MODE_PRIVATE);
    }

    public LiveData<List<OrderEntity>> getActiveOrders() {
        int agentId = prefs.getInt("agent_id", -1);
        Log.d(TAG, "Loading orders - Agent ID: " + agentId);
        return orderRepository.getAgentActiveOrders(agentId);
    }

    public void updateOrderStatus(int orderId, String status, OrderRepository.OrderCallback callback) {
        Log.d(TAG, "Updating order " + orderId + " to status: " + status);

        if (OrderStatus.DELIVERED.equals(status)) {
            // Get current agent ID
            int agentId = prefs.getInt("agent_id", -1);
            if (agentId != -1) {
                // Update agent availability first
                deliveryAgentRepository.updateAgentAvailability(agentId, true, new DeliveryAgentRepository.OnAgentCallback() {
                    @Override
                    public void onSuccess(int agentId) {
                        // Then update order status
                        orderRepository.updateOrderStatus(orderId, status, callback);
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError("Failed to update agent availability: " + message);
                    }
                });
            } else {
                callback.onError("Agent ID not found");
            }
        } else {
            // For other statuses, just update the order
            orderRepository.updateOrderStatus(orderId, status, callback);
        }
    }
}