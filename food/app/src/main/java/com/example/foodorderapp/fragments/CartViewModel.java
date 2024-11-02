package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.CartItemEntity;
import com.example.foodorderapp.database.repositories.CartRepository;
import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository repository;
    private final LiveData<List<CartItemEntity>> allCartItems;
    private final LiveData<Double> cartTotal;
    private final LiveData<Integer> cartItemCount;

    public CartViewModel(Application application) {
        super(application);
        repository = new CartRepository(application);
        allCartItems = repository.getAllCartItems();
        cartTotal = repository.getCartTotal();
        cartItemCount = repository.getCartItemCount();
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
            repository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            repository.update(item);
        }
    }

    public void removeItem(CartItemEntity item) {
        repository.delete(item);
    }

    public void clearCart() {
        repository.clearCart();
    }
}