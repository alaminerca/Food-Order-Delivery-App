package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import com.example.foodorderapp.utils.OrderStatus;
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
        // First mark the order as paid
        repository.markOrderAsPaid(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // After successful payment, update status to PENDING for admin review
                updateOrderStatus(orderId, OrderStatus.PENDING, new OrdersFragment.OrderUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError("Status update failed: " + message);
                    }
                });
            }

            @Override
            public void onError(String message) {
                callback.onError("Payment failed: " + message);
            }
        });
    }

    public void updateOrderStatus(int orderId, String status, OrdersFragment.OrderUpdateCallback callback) {
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