package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.CartAdapter;
import com.example.foodorderapp.database.entities.CartItemEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartItemClickListener {
    private CartViewModel cartViewModel;
    private CartAdapter adapter;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private List<CartItemEntity> currentCartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        adapter = new CartAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize views
        totalPriceTextView = view.findViewById(R.id.total_price);
        checkoutButton = view.findViewById(R.id.checkout_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Observe cart items
        cartViewModel.getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            currentCartItems = cartItems;
            adapter.updateItems(cartItems);
            checkoutButton.setEnabled(!cartItems.isEmpty());
        });

        // Observe total price
        cartViewModel.getCartTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                totalPriceTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
            } else {
                totalPriceTextView.setText("Total: $0.00");
            }
        });

        // Set checkout button click listener
        checkoutButton.setOnClickListener(v -> processCheckout());
    }

    private void processCheckout() {
        if (currentCartItems != null && !currentCartItems.isEmpty()) {
            Double total = cartViewModel.getCartTotal().getValue();
            if (total != null) {
                cartViewModel.processCheckout(currentCartItems, total);
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                // Switch to Orders tab using BottomNavigationView
                BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.navigation_orders);
            }
        }
    }



    // CartAdapter.CartItemClickListener implementations
    @Override
    public void onPlusClick(CartItemEntity item) {
        cartViewModel.updateQuantity(item, item.getQuantity() + 1);
    }

    @Override
    public void onMinusClick(CartItemEntity item) {
        if (item.getQuantity() > 1) {
            cartViewModel.updateQuantity(item, item.getQuantity() - 1);
        } else {
            cartViewModel.removeItem(item);
        }
    }

    @Override
    public void onRemoveClick(CartItemEntity item) {
        cartViewModel.removeItem(item);
    }
}