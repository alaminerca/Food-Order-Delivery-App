package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.entities.CartItemEntity;
import com.example.foodorderapp.database.repositories.MenuRepository;
import com.example.foodorderapp.database.repositories.CartRepository;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private MenuRepository menuRepository;
    private CartRepository cartRepository;
    private final LiveData<List<MenuItemEntity>> allMenuItems;

    public MenuViewModel(Application application) {
        super(application);
        menuRepository = new MenuRepository(application);
        cartRepository = new CartRepository(application);
        allMenuItems = menuRepository.getAllMenuItems();
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItems() {
        return allMenuItems;
    }

    public void addToCart(MenuItemEntity menuItem) {
        CartItemEntity cartItem = new CartItemEntity(
                menuItem.getId(),
                1, // Initial quantity
                menuItem.getPrice(),
                menuItem.getName()
        );
        cartRepository.insert(cartItem);
    }
}