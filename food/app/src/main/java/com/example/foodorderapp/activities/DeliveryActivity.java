package com.example.foodorderapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.DeliveryOrderAdapter;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.OrderRepository;
import com.example.foodorderapp.utils.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity implements DeliveryOrderAdapter.DeliveryOrderListener {
    private static final String TAG = "DeliveryActivity";
    private DeliveryViewModel viewModel;
    private DeliveryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Log.d(TAG, "onCreate called");

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.delivery_orders_recycler);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found!");
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeliveryOrderAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "RecyclerView setup complete");
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);
        LiveData<List<OrderEntity>> ordersLiveData = viewModel.getActiveOrders();

        if (ordersLiveData == null) {
            Toast.makeText(this, "Error: Delivery agent not found", Toast.LENGTH_LONG).show();
            finish(); // Return to previous screen
            return;
        }

        ordersLiveData.observe(this, orders -> {
            Log.d(TAG, "Received orders update. Count: " + (orders != null ? orders.size() : "null"));
            if (orders != null && !orders.isEmpty()) {
                adapter.updateOrders(orders);
                findViewById(R.id.empty_view).setVisibility(View.GONE);
            } else {
                findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStartDelivery(OrderEntity order) {
        new AlertDialog.Builder(this)
                .setTitle("Start Delivery")
                .setMessage("Start delivery for Order #" + order.getId() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.updateOrderStatus(order.getId(), OrderStatus.DELIVERING, new OrderRepository.OrderCallback() {
                        @Override
                        public void onSuccess(int orderId) {
                            Toast.makeText(DeliveryActivity.this, "Delivery started", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(DeliveryActivity.this, "Failed to update status: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onMarkDelivered(OrderEntity order) {
        new AlertDialog.Builder(this)
                .setTitle("Complete Delivery")
                .setMessage("Mark Order #" + order.getId() + " as delivered?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.updateOrderStatus(order.getId(), OrderStatus.DELIVERED, new OrderRepository.OrderCallback() {
                        @Override
                        public void onSuccess(int orderId) {
                            Toast.makeText(DeliveryActivity.this, "Order marked as delivered", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(DeliveryActivity.this, "Failed to update status: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onViewDetails(OrderEntity order) {
        new AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setMessage("Order #" + order.getId() + "\n" +
                        "Status: " + order.getStatus() + "\n" +
                        "Amount: $" + order.getTotalAmount())
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // Clear any stored preferences
            getSharedPreferences("delivery_prefs", MODE_PRIVATE).edit().clear().apply();
            // Return to login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("Contact support at:\nEmail: support@foodorder.com\nPhone: +1234567890")
                .setPositiveButton("OK", null)
                .show();
    }
}