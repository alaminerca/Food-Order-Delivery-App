package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.CartItemEntity;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.entities.OrderItemEntity;
import com.example.foodorderapp.database.repositories.CartRepository;
import com.example.foodorderapp.database.repositories.OrderRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
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
                total,
                new Date().getTime(),
                "PENDING"
        );

        // Convert cart items to order items
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            OrderItemEntity orderItem = new OrderItemEntity(
                    0, // orderId will be set in repository
                    cartItem.getItemName(),
                    cartItem.getQuantity(),
                    cartItem.getPricePerItem()
            );
            orderItems.add(orderItem);
        }

        // Save order and items
        orderRepository.insertOrder(order, orderItems);

        // Clear the cart
        clearCart();
    }
}