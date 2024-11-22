package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.OrderAdapter;
import com.example.foodorderapp.database.entities.OrderEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageOrdersFragment extends Fragment implements OrderAdapter.OrderActionListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ManageOrdersViewModel viewModel;
    private RadioGroup filterGroup;
    private List<OrderEntity> allOrders = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);

        recyclerView = view.findViewById(R.id.orders_recycler_view);
        filterGroup = view.findViewById(R.id.order_filter_group);

        setupRecyclerView();
        setupFilters();
        observeOrders();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(new ArrayList<>(), this, true);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(ManageOrdersViewModel.class);
    }

    private void setupFilters() {
        filterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.filter_all) {
                adapter.updateOrders(allOrders);
            } else if (checkedId == R.id.filter_pending) {
                List<OrderEntity> filtered = allOrders.stream()
                        .filter(order -> "PENDING".equals(order.getStatus()) && order.isPaid())
                        .collect(Collectors.toList());
                adapter.updateOrders(filtered);
            } else if (checkedId == R.id.filter_accepted) {
                List<OrderEntity> filtered = allOrders.stream()
                        .filter(order -> "ACCEPTED".equals(order.getStatus()))
                        .collect(Collectors.toList());
                adapter.updateOrders(filtered);
            }
        });
    }

    private void observeOrders() {
        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            allOrders = orders;
            // Apply current filter
            filterGroup.check(filterGroup.getCheckedRadioButtonId());
        });
    }

    @Override
    public void onAcceptOrder(OrderEntity order) {
        viewModel.updateOrderStatus(order.getId(), "ACCEPTED", new OrderUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Order accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPayOrder(OrderEntity order) {
        // Not used in admin view
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        viewModel.updateOrderStatus(order.getId(), "DELIVERED", new OrderUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Order marked as delivered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OrderUpdateCallback {
        void onSuccess();
        void onError(String message);
    }
}