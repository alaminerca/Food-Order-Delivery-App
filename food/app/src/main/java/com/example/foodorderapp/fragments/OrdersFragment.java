package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
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

        recyclerView = view.findViewById(R.id.orders_recycler_view);
        noOrdersText = view.findViewById(R.id.no_orders_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(new ArrayList<>(), this, false);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

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
    public void onPayOrder(OrderEntity order) {
        // Simulate payment process
        viewModel.simulatePayment(order.getId(), new PaymentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Payment successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Payment failed: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        // Not used in customer view
    }

    public interface PaymentCallback {
        void onSuccess();
        void onError(String message);
    }
}