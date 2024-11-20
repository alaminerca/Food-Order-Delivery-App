package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.OrderAdapter;
import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private OrdersViewModel viewModel;
    private OrderAdapter adapter;
    private TextView noOrdersText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.orders_recycler_view);
        noOrdersText = view.findViewById(R.id.no_orders_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null && !orders.isEmpty()) {
                adapter.updateOrders(orders);
                noOrdersText.setVisibility(View.GONE);
            } else {
                noOrdersText.setVisibility(View.VISIBLE);
            }
        });
    }
}