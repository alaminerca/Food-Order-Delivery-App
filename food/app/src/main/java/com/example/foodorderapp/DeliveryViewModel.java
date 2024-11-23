package com.example.foodorderapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.repositories.OrderRepository;

import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;
    private DeliveryAgentRepository agentRepository;

    public DeliveryViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        agentRepository = new DeliveryAgentRepository(application);
    }

    public LiveData<List<OrderEntity>> getActiveOrders() {
        // TODO: Get current delivery agent ID from preferences/session
        int agentId = getCurrentAgentId();
        return orderRepository.getAgentActiveOrders(agentId);
    }

    private int getCurrentAgentId() {
        // TODO: Implement proper session management
        return 1; // Temporary return for testing
    }
}