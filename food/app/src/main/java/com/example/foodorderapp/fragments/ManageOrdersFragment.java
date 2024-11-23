package com.example.foodorderapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.OrderAdapter;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.utils.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageOrdersFragment extends Fragment implements OrderAdapter.OrderActionListener {
    private static final String TAG = "ManageOrdersFragment";
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ManageOrdersViewModel viewModel;
    private RadioGroup filterGroup;
    private List<OrderEntity> allOrders = new ArrayList<>();
    private List<DeliveryAgentEntity> availableAgents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);
        Log.d(TAG, "Creating ManageOrdersFragment view");

        recyclerView = view.findViewById(R.id.orders_recycler_view);
        filterGroup = view.findViewById(R.id.order_filter_group);

        setupRecyclerView();
        setupFilters();
        observeData();

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
                        .filter(order -> OrderStatus.PENDING.equals(order.getStatus()) && order.isPaid())
                        .collect(Collectors.toList());
                adapter.updateOrders(filtered);
            } else if (checkedId == R.id.filter_accepted) {
                List<OrderEntity> filtered = allOrders.stream()
                        .filter(order -> OrderStatus.ACCEPTED.equals(order.getStatus()))
                        .collect(Collectors.toList());
                adapter.updateOrders(filtered);
            }
        });
    }

    private void observeData() {
        // Observe orders
        viewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            Log.d(TAG, "Orders updated: " + orders.size() + " orders");
            allOrders = orders;
            filterGroup.check(filterGroup.getCheckedRadioButtonId());
        });

        // Observe available delivery agents
        viewModel.getAvailableAgents().observe(getViewLifecycleOwner(), agents -> {
            Log.d(TAG, "Available agents updated: " + agents.size() + " agents");
            availableAgents = agents;
        });
    }

    @Override
    public void onAcceptOrder(OrderEntity order) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Accept Order")
                .setMessage("Accept Order #" + order.getId() + "?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    viewModel.updateOrderStatus(order.getId(), OrderStatus.ACCEPTED, new OrderUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Order accepted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onAssignDelivery(OrderEntity order) {
        if (availableAgents.isEmpty()) {
            Toast.makeText(getContext(), "No delivery agents available", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_assign_delivery, null);
        Spinner agentSpinner = dialogView.findViewById(R.id.agent_spinner);

        // Create adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                availableAgents.stream()
                        .map(agent -> agent.getName() + " (" + agent.getPhone() + ")")
                        .collect(Collectors.toList())
        );
        agentSpinner.setAdapter(spinnerAdapter);

        new AlertDialog.Builder(requireContext())
                .setTitle("Assign Delivery Agent")
                .setView(dialogView)
                .setPositiveButton("Assign", (dialog, which) -> {
                    int selectedPosition = agentSpinner.getSelectedItemPosition();
                    if (selectedPosition != -1) {
                        DeliveryAgentEntity selectedAgent = availableAgents.get(selectedPosition);
                        viewModel.assignDeliveryAgent(order.getId(), selectedAgent.getId(), new OrderUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(),
                                        "Order assigned to " + selectedAgent.getName(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(),
                                        "Failed to assign: " + message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Mark as Delivered")
                .setMessage("Mark Order #" + order.getId() + " as delivered?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    viewModel.markDelivered(order.getId(), new OrderUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Order marked as delivered", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onPayOrder(OrderEntity order) {
        // Not used in admin view
    }

    @Override
    public void onCancelOrder(OrderEntity order) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel Order #" + order.getId() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.updateOrderStatus(order.getId(), OrderStatus.CANCELLED, new OrderUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Order cancelled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    public interface OrderUpdateCallback {
        void onSuccess();
        void onError(String message);
    }
}