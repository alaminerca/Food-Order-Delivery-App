package com.example.foodorderapp.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.CartDAO;
import com.example.foodorderapp.database.entities.CartItemEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartDAO cartDAO;
    private LiveData<List<CartItemEntity>> allCartItems;
    private LiveData<Double> cartTotal;
    private LiveData<Integer> cartItemCount;
    private ExecutorService executorService;

    public CartRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartDAO = database.cartDAO();
        allCartItems = cartDAO.getAllCartItems();
        cartTotal = cartDAO.getCartTotal();
        cartItemCount = cartDAO.getCartItemCount();
        executorService = Executors.newSingleThreadExecutor();
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

    public void insert(CartItemEntity cartItem) {
        executorService.execute(() -> cartDAO.insert(cartItem));
    }

    public void update(CartItemEntity cartItem) {
        executorService.execute(() -> cartDAO.update(cartItem));
    }

    public void delete(CartItemEntity cartItem) {
        executorService.execute(() -> cartDAO.delete(cartItem));
    }

    public void clearCart() {
        executorService.execute(() -> cartDAO.clearCart());
    }

    public LiveData<CartItemEntity> getCartItemByMenuItemId(int menuItemId) {
        return cartDAO.getCartItemByMenuItemId(menuItemId);
    }
}