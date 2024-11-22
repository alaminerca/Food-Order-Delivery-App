package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.CartItemEntity;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.CartRepository;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private final LiveData<List<CartItemEntity>> allCartItems;
    private final LiveData<Double> cartTotal;
    private final LiveData<Integer> cartItemCount;

    public CartViewModel(Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        orderRepository = new OrderRepository(application);
        allCartItems = cartRepository.getAllCartItems();
        cartTotal = cartRepository.getCartTotal();
        cartItemCount = cartRepository.getCartItemCount();
    }

    public LiveData<List<CartItemEntity>> getAllCartItems() {
        return allCartItems;
    }

    public LiveData<Double> getCartTotal() {
        return cartTotal;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }

    public void updateQuantity(CartItemEntity item, int newQuantity) {
        if (newQuantity <= 0) {
            cartRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartRepository.update(item);
        }
    }

    public void removeItem(CartItemEntity item) {
        cartRepository.delete(item);
    }

    public void clearCart() {
        cartRepository.clearCart();
    }

    public void processCheckout(List<CartItemEntity> cartItems, double total) {
        // Create new order
        OrderEntity order = new OrderEntity(
                getCurrentUserId(),
                total,
                "PENDING"
        );

        // Create order and handle response
        orderRepository.createOrder(order, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // After successful order creation, clear the cart
                clearCart();
            }

            @Override
            public void onError(String message) {
                // Handle order creation error
            }
        });
    }

    private int getCurrentUserId() {
        // TODO: Implement proper user session management
        return 1; // Temporary return for testing
    }
}