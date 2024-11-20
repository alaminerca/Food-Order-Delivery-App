package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
    private OrderRepository repository;
    private LiveData<List<OrderEntity>> allOrders;

    public OrdersViewModel(Application application) {
        super(application);
        repository = new OrderRepository(application);
        allOrders = repository.getAllOrders();
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return allOrders;
    }
}