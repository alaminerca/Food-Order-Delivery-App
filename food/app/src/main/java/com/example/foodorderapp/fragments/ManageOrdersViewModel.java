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

    public void updateOrderStatus(int orderId, String status, ManageOrdersFragment.OrderUpdateCallback callback) {
        repository.updateOrderStatus(orderId, status, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}