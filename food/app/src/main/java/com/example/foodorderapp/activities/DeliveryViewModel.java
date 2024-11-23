package com.example.foodorderapp.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {
    private static final String TAG = "DeliveryViewModel";
    private OrderRepository orderRepository;
    private SharedPreferences prefs;

    public DeliveryViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        prefs = application.getSharedPreferences("delivery_prefs", Application.MODE_PRIVATE);
    }

    public LiveData<List<OrderEntity>> getActiveOrders() {
        int agentId = prefs.getInt("agent_id", -1);
        String agentEmail = prefs.getString("agent_email", "");
        Log.d(TAG, "Loading orders - Agent ID: " + agentId + ", Email: " + agentEmail);

        return orderRepository.getAgentActiveOrders(agentId);
    }

    public void updateOrderStatus(int orderId, String status, OrderRepository.OrderCallback callback) {
        Log.d(TAG, "Updating order " + orderId + " to status: " + status);
        orderRepository.updateOrderStatus(orderId, status, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                callback.onSuccess(orderId);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}