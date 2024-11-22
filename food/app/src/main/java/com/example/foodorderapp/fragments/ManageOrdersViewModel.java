package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.List;

public class ManageOrdersViewModel extends AndroidViewModel {
    private final OrderRepository repository;

    public ManageOrdersViewModel(Application application) {
        super(application);
        repository = new OrderRepository(application);
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return repository.getAllOrders();
    }

    public void updateOrderStatus(int orderId, String status) {
        repository.updateOrderStatus(orderId, status, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // Status updated successfully
            }

            @Override
            public void onError(String message) {
                // Handle error
            }
        });
    }

    public void assignDeliveryAgent(int orderId, String agentId) {
        repository.assignDeliveryAgent(orderId, agentId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // Agent assigned successfully
            }

            @Override
            public void onError(String message) {
                // Handle error
            }
        });
    }
}