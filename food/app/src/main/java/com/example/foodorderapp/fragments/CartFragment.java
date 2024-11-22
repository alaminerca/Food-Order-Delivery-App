package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.CartAdapter;
import com.example.foodorderapp.database.entities.CartItemEntity;
import java.util.ArrayList;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartItemClickListener {
    private CartViewModel viewModel;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView totalTextView;
    private Button checkoutButton;
    private TextView emptyCartText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        totalTextView = view.findViewById(R.id.total_price);
        checkoutButton = view.findViewById(R.id.checkout_button);
        emptyCartText = view.findViewById(R.id.empty_cart_text);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Observe cart items
        viewModel.getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                adapter.updateItems(cartItems);
                recyclerView.setVisibility(View.VISIBLE);
                emptyCartText.setVisibility(View.GONE);
                checkoutButton.setEnabled(true);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyCartText.setVisibility(View.VISIBLE);
                checkoutButton.setEnabled(false);
            }
        });

        // Observe total price
        viewModel.getCartTotal().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                totalTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
            } else {
                totalTextView.setText("Total: $0.00");
            }
        });

        // Setup checkout button
        checkoutButton.setOnClickListener(v -> {
            viewModel.processCheckout(adapter.getItems(), viewModel.getCartTotal().getValue() != null ?
                    viewModel.getCartTotal().getValue() : 0.0);
        });

        return view;
    }

    @Override
    public void onUpdateQuantity(CartItemEntity item, int newQuantity) {
        viewModel.updateQuantity(item, newQuantity);
    }

    @Override
    public void onRemoveItem(CartItemEntity item) {
        viewModel.removeItem(item);
    }
}