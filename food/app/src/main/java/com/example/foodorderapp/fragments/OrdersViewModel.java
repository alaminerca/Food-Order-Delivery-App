package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
    private final OrderRepository repository;

    public OrdersViewModel(Application application) {
        super(application);
        repository = new OrderRepository(application);
    }

    public LiveData<List<OrderEntity>> getUserOrders() {
        // TODO: Get actual user ID from session management
        return repository.getOrdersByUser(1);
    }

    public void simulatePayment(int orderId, OrdersFragment.PaymentCallback callback) {
        repository.updateOrderStatus(orderId, "PENDING", new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // After status update, update payment status
                repository.markOrderAsPaid(orderId, new OrderRepository.OrderCallback() {
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

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}