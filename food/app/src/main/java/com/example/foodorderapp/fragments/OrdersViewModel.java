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
}