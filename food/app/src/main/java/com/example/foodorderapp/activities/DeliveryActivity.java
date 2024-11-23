package com.example.foodorderapp.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.foodorderapp.DeliveryViewModel;
import com.example.foodorderapp.R;

public class DeliveryActivity extends AppCompatActivity {
    private static final String TAG = "DeliveryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        RecyclerView ordersRecyclerView = findViewById(R.id.delivery_orders_recycler);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DeliveryViewModel viewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);

        // Observe active orders assigned to this delivery agent
        viewModel.getActiveOrders().observe(this, orders -> {
            // Update UI with orders
            // TODO: Create DeliveryOrderAdapter
        });
    }
}