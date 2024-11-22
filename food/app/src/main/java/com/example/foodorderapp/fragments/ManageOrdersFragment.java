package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.OrderAdapter;
import com.example.foodorderapp.database.entities.OrderEntity;
import java.util.ArrayList;

public class ManageOrdersFragment extends Fragment implements OrderAdapter.OrderActionListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ManageOrdersViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.orders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list and this fragment as the listener
        adapter = new OrderAdapter(new ArrayList<OrderEntity>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageOrdersViewModel.class);

        // Observe orders
        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            adapter.updateOrders(orders);
        });

        return view;
    }

    @Override
    public void onAcceptOrder(OrderEntity order) {
        viewModel.updateOrderStatus(order.getId(), "ACCEPTED");
        Toast.makeText(getContext(), "Order Accepted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAssignDelivery(OrderEntity order) {
        // TODO: Show delivery agent selection dialog
        viewModel.assignDeliveryAgent(order.getId(), "AGENT_ID");
        Toast.makeText(getContext(), "Delivery Agent Assigned", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        viewModel.updateOrderStatus(order.getId(), "DELIVERED");
        Toast.makeText(getContext(), "Order Marked as Delivered", Toast.LENGTH_SHORT).show();
    }
}