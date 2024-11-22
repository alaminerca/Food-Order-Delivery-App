package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.OrderAdapter;
import com.example.foodorderapp.database.entities.OrderEntity;
import java.util.ArrayList;

public class OrdersFragment extends Fragment implements OrderAdapter.OrderActionListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private OrdersViewModel viewModel;
    private TextView noOrdersText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.orders_recycler_view);
        noOrdersText = view.findViewById(R.id.no_orders_text);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(new ArrayList<OrderEntity>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        // Observe orders
        viewModel.getUserOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null && !orders.isEmpty()) {
                adapter.updateOrders(orders);
                recyclerView.setVisibility(View.VISIBLE);
                noOrdersText.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                noOrdersText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onAcceptOrder(OrderEntity order) {
        // Not used in customer view
    }

    @Override
    public void onAssignDelivery(OrderEntity order) {
        // Not used in customer view
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        // Not used in customer view
    }
}